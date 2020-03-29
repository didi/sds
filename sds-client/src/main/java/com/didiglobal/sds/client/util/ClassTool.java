/**
 * Kuaidadi.com Inc.
 * Copyright (c) 2012-2016 All Rights Reserved.
 */
package com.didiglobal.sds.client.util;

import com.didiglobal.sds.client.bean.SdsClassDefine;

import java.io.IOException;
import java.io.InputStream;


/**
 * 字节码解析工具类
 *
 * 参见 org.springframework.asm.ClassReader
 * 
 * @author manzhizhen
 * @version $Id: ClassTool.java, v 0.1 2016年1月9日 下午5:34:40 Administrator Exp $
 */
public class ClassTool {

    /**
     * 通过字节码获取部分类定义
     * 
     * @param inputStream
     * @return
     * @throws IOException 
     */
    public static SdsClassDefine getClassDefine(InputStream inputStream) throws IOException {
        return SdsClassReader.getClassDefine(inputStream);
    }
}
