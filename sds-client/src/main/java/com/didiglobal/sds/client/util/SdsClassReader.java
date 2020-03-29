package com.didiglobal.sds.client.util;

import com.didiglobal.sds.client.bean.SdsClassDefine;

import java.io.IOException;
import java.io.InputStream;

/**
 * 字节码类定义读取
 * <p>
 * 注意：该实现参考自Spring
 *
 * @author manzhizhen
 * @version $Id: SdsClassReader.java, v 0.1 2016年1月10日 上午11:12:18 Administrator Exp $
 */
public class SdsClassReader {

    /**
     * The type of CONSTANT_Class constant pool items.
     */
    static final int CLASS = 7;

    /**
     * The type of CONSTANT_Fieldref constant pool items.
     */
    static final int FIELD = 9;

    /**
     * The type of CONSTANT_Methodref constant pool items.
     */
    static final int METH = 10;

    /**
     * The type of CONSTANT_InterfaceMethodref constant pool items.
     */
    static final int IMETH = 11;

    /**
     * The type of CONSTANT_String constant pool items.
     */
    static final int STR = 8;

    /**
     * The type of CONSTANT_Integer constant pool items.
     */
    static final int INT = 3;

    /**
     * The type of CONSTANT_Float constant pool items.
     */
    static final int FLOAT = 4;

    /**
     * The type of CONSTANT_Long constant pool items.
     */
    static final int LONG = 5;

    /**
     * The type of CONSTANT_Double constant pool items.
     */
    static final int DOUBLE = 6;

    /**
     * The type of CONSTANT_NameAndType constant pool items.
     */
    static final int NAME_TYPE = 12;

    /**
     * The type of CONSTANT_Utf8 constant pool items.
     */
    static final int UTF8 = 1;

    /**
     * The type of CONSTANT_MethodType constant pool items.
     */
    static final int MTYPE = 16;

    /**
     * The type of CONSTANT_MethodHandle constant pool items.
     */
    static final int HANDLE = 15;

    /**
     * The type of CONSTANT_InvokeDynamic constant pool items.
     */
    static final int INDY = 18;

    /**
     * 从字节码的InputStream对象中获取对象的部分定义
     * 目前只需要取得全路径名
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static SdsClassDefine getClassDefine(InputStream inputStream) throws IOException {
        byte[] bytes = readClass(inputStream, true);

        int off = 0;
        int[] items;
        int maxStringLength; //  Maximum length of the strings contained in the constant pool of the class.
        int header; // Start index of the class header information (access, name...) in {@link #bytes bytes}.
        /**
         * parses the constant pool,返回常量池中常量个数
         * 常量池入口的第一个2字节数据标识常量池中常量的个数，注意，常量池区域是在class字节码中的第8个字节部分开始的，前面8字节是0xCAFEBABE和次、主版本号
         */
        items = new int[readUnsignedShort(bytes, off + 8)];
        int n = items.length;
        int max = 0;
        // index为10，表明是从第11个字节开始，这正好是第一个常量的tag字节。
        int index = off + 10;
        for (int i = 1; i < n; ++i) {
            // items记录的是每个常量的数据部分的开始字节index（即tag后的第一个字节）
            items[i] = index + 1;
            int size; // 常量数据部分的字节数
            // 此时的index正好位于某个常量的tag字节处
            switch (bytes[index]) {
                case FIELD:
                case METH:
                case IMETH:
                case INT:
                case FLOAT:
                case NAME_TYPE:
                case INDY:
                    size = 5;
                    break;
                case LONG:
                case DOUBLE:
                    size = 9;
                    ++i;
                    break;
                case UTF8:
                    // 字符串数据字节数+3，为什么要加3？因为字符串的tag后面有3个字节，第1和第2个字节用来记录字符串的length，第3个字节记录的是bytes
                    size = 3 + readUnsignedShort(bytes, index + 1);
                    if (size > max) {
                        max = size;
                    }
                    break;
                case HANDLE:
                    size = 4;
                    break;
                default:
                    size = 3;
                    break;
            }
            index += size;
        }
        maxStringLength = max;
        // the class header information starts just after the constant pool
        header = index;

        /**
         * 开始查询类/接口定义
         */
        // 跳过常量池部分和后面紧跟的2字节访问标记（access_flags），来到了类/接口定义部分
        int classDefineIndex = header + 2;
        // classDefineIndex开始的两字节是一个类/接口定义的utf8字符串的在常量池中的位置，和items的index吻合。
        int classDefineRefIndex = items[readUnsignedShort(bytes, classDefineIndex)];
        char[] buf = new char[maxStringLength];

        String className = readUTF8(bytes, items, classDefineRefIndex, buf);

        SdsClassDefine classDefine = new SdsClassDefine();
        classDefine.setFullName(className.replaceAll("/", "."));

        return classDefine;

    }

    /**
     * 从byte数组中读取一个无符号短整型
     *
     * @param bytes class file byte array
     * @param index the start index of the value to be read in bytes.
     * @return the read value.
     */
    private static int readUnsignedShort(final byte[] bytes, final int index) {
        return ((bytes[index] & 0xFF) << 8) | (bytes[index + 1] & 0xFF);
    }

    /**
     * 从class文件读取字节码
     * Reads the bytecode of a class.
     *
     * @param is    an input stream from which to read the class.
     * @param close true to close the input stream after reading.
     * @return the bytecode read from the given input stream.
     * @throws IOException if a problem occurs during reading.
     */
    private static byte[] readClass(final InputStream is, boolean close) throws IOException {
        if (is == null) {
            throw new IOException("Class not found");
        }
        try {
            byte[] bytes = new byte[is.available()];
            int len = 0;
            while (true) {
                int n = is.read(bytes, len, bytes.length - len);
                if (n == -1) {
                    if (len < bytes.length) {
                        byte[] c = new byte[len];
                        System.arraycopy(bytes, 0, c, 0, len);
                        bytes = c;
                    }
                    return bytes;
                }
                len += n;
                if (len == bytes.length) {
                    int last = is.read();
                    if (last < 0) {
                        return bytes;
                    }
                    byte[] c = new byte[bytes.length + 1000];
                    System.arraycopy(bytes, 0, c, 0, len);
                    c[len++] = (byte) last;
                    bytes = c;
                }
            }
        } finally {
            if (close) {
                is.close();
            }
        }
    }

    /**
     * Reads an UTF8 string constant pool item in bytes. <i>This method
     * is intended for Attribute sub classes, and is normally not needed
     * by class generators or adapters.</i>
     *
     * @param bytes bytecode byte array
     * @param items constant pool start index array
     * @param index the start index of an unsigned short value in bytes,
     *              whose value is the index of an UTF8 constant pool item.
     * @param buf   buffer to be used to read the item. This buffer must be
     *              sufficiently large. It is not automatically resized.
     * @return the String corresponding to the specified UTF8 item.
     */
    private static String readUTF8(byte[] bytes, int[] items, int index, final char[] buf) {
        int item = readUnsignedShort(bytes, index);
        if (index == 0 || item == 0) {
            return null;
        }

        index = items[item];
        return readUTF(bytes, index + 2, readUnsignedShort(bytes, index), buf);
    }

    /**
     * Reads UTF8 string in bytes.
     *
     * @param index  start offset of the UTF8 string to be read.
     * @param utfLen length of the UTF8 string to be read.
     * @param buf    buffer to be used to read the string. This buffer must be
     *               sufficiently large. It is not automatically resized.
     * @return the String corresponding to the specified UTF8 string.
     * @parm bytes
     * bytecode byte array
     */
    private static String readUTF(final byte[] bytes, int index, final int utfLen, final char[] buf) {
        int endIndex = index + utfLen;
        byte[] b = bytes;
        int strLen = 0;
        int c;
        int st = 0;
        char cc = 0;
        while (index < endIndex) {
            c = b[index++];
            switch (st) {
                case 0:
                    c = c & 0xFF;
                    if (c < 0x80) { // 0xxxxxxx
                        buf[strLen++] = (char) c;
                    } else if (c < 0xE0 && c > 0xBF) { // 110x xxxx 10xx xxxx
                        cc = (char) (c & 0x1F);
                        st = 1;
                    } else { // 1110 xxxx 10xx xxxx 10xx xxxx
                        cc = (char) (c & 0x0F);
                        st = 2;
                    }
                    break;

                case 1: // byte 2 of 2-byte char or byte 3 of 3-byte char
                    buf[strLen++] = (char) ((cc << 6) | (c & 0x3F));
                    st = 0;
                    break;

                case 2: // byte 2 of 3-byte char
                    cc = (char) ((cc << 6) | (c & 0x3F));
                    st = 1;
                    break;
                default:
                    break;
            }
        }
        return new String(buf, 0, strLen);
    }

}
