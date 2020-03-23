package com.didiglobal.sds.bootstrap.transformer;

import com.didiglobal.sds.bootstrap.SdsBootStrap;
import com.didiglobal.sds.client.SdsClient;
import com.didiglobal.sds.client.annotation.SdsDowngradeMethod;
import com.didiglobal.sds.client.contant.ExceptionCode;
import com.didiglobal.sds.client.exception.SdsException;
import com.didiglobal.sds.client.log.SdsLoggerFactory;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.slf4j.Logger;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manzhizhen on 17/5/1.
 */
public class SdsClassFileTransformer implements ClassFileTransformer {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    /**
     * 可能使用sds的包前缀
     */
    private static List<String> includeClassList = new ArrayList<>();

    /**
     * @param packages 包路径，用分号分隔
     */
    public SdsClassFileTransformer(String packages) {
        String[] pags = packages.split(";");
        for (String pag : pags) {
            includeClassList.add(pag.trim());
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {

        /**
         * 排除掉一些类
         */
        boolean has = false;
        String realClass = className.replace('/', '.');
        for (String includeClass : includeClassList) {

            if (realClass.startsWith(includeClass)) {
                has = true;
                break;
            }
        }

        if (!has) {
            return classfileBuffer;
        }

        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.importPackage(SdsDowngradeMethod.class.getName());
            classPool.importPackage(SdsBootStrap.class.getName());
            classPool.importPackage(SdsClient.class.getName());
            classPool.importPackage(ExceptionCode.class.getName());
            classPool.importPackage(SdsException.class.getName());
            classPool.importPackage(Exception.class.getName());

            CtClass ctClass = classPool.get(realClass);

            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();

            // 该类的方法是否有降级注解
            boolean hasAnnotation = false;
            for (CtMethod declaredMethod : declaredMethods) {
                SdsDowngradeMethod sdsDowngradeMethod = (SdsDowngradeMethod) declaredMethod.
                        getAnnotation(SdsDowngradeMethod.class);

                if (sdsDowngradeMethod != null) {
                    // 在方法体前后加上降级判断的逻辑, 如果被降级，则抛出降级异常
                    declaredMethod.insertBefore(String.format("SdsClient __sdsClient = SdsBootStrap.getClient(); " +
                                    "if (__sdsClient != null && __sdsClient.shouldDowngrade(\"%s\")) {  " +
                                    "  throw new SdsException(\"%s\", ExceptionCode.DOWNGRADE); } ",
                            sdsDowngradeMethod.point(), sdsDowngradeMethod.point()));

                    // 记录异常数
                    CtClass exceptionType = ClassPool.getDefault().get("java.lang.Exception");
                    declaredMethod.addCatch(String.format("{ " +
                            "SdsClient ___sdsClient = SdsBootStrap.getClient(); " +
                            "if(___sdsClient != null && !($e instanceof SdsException)) " +
                            "{ ___sdsClient.exceptionSign(\"%s\", $e); }  " +
                            "throw $e;  }", sdsDowngradeMethod.point()), exceptionType);

                    // 在finally中调用downgradeFinally方法
                    declaredMethod.insertAfter(String.format("{ SdsClient ____sdsClient = SdsBootStrap.getClient(); " +
                            "if( ____sdsClient != null ) {____sdsClient.downgradeFinally(\"%s\");} }",
                            sdsDowngradeMethod.point()), true);

                    hasAnnotation = true;
                }
            }

            if (hasAnnotation) {
                logger.info(realClass + " already add downgrade code.");

            }

            return ctClass.toBytecode();

        } catch (Exception e) {
            logger.error(realClass + " add downgrade code has exception:" + e.getMessage(), e);
        }

        return classfileBuffer;
    }

}