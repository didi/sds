package com.didiglobal.sds.admin.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过CGLib方式来做对象拷贝
 *
 * Created by manzhizhen on 17/8/29.
 */
public class FastBeanUtil {

    private static ConcurrentHashMap<ClassPair, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    /**
     * 浅拷贝
     * 对象内的属性只会复制引用，比如Map、List都是拷贝引用，并不会重新创建
     *
     * @param source
     * @param target
     */
    public static void copy(Object source, Object target) {
        if(source == null || target == null) {
            return ;
        }

        ClassPair classPair = new ClassPair(source.getClass(), target.getClass());
        BeanCopier beanCopier = beanCopierMap.get(classPair);

        if(beanCopier == null) {
            BeanCopier newBeanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            BeanCopier oldBeanCopier = beanCopierMap.putIfAbsent(classPair, newBeanCopier);

            beanCopier = oldBeanCopier != null ? oldBeanCopier : newBeanCopier;
        }

        beanCopier.copy(source, target, null);
    }

    /**
     * 同{@link #copy(Object, Object)}}，只是用起来更方便
     *
     * @param source
     * @param target
     */
    public static <S, T> T copyForNew(S source, T target) {
        copy(source, target);

        return target;
    }


    private static class ClassPair {
        private Class<?> sourceClass;
        private Class<?> targetClass;

        public ClassPair(Class<?> sourceClass, Class<?> targetClass) {
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
        }

        public Class<?> getSourceClass() {
            return sourceClass;
        }

        public void setSourceClass(Class<?> sourceClass) {
            this.sourceClass = sourceClass;
        }

        public Class<?> getTargetClass() {
            return targetClass;
        }

        public void setTargetClass(Class<?> targetClass) {
            this.targetClass = targetClass;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("ClassPair{");
            sb.append("sourceClass=").append(sourceClass);
            sb.append(", targetClass=").append(targetClass);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassPair classPair = (ClassPair) o;

            if (sourceClass != null ? !sourceClass.equals(classPair.sourceClass) : classPair.sourceClass != null)
                return false;
            return targetClass != null ? targetClass.equals(classPair.targetClass) : classPair.targetClass == null;
        }

        @Override
        public int hashCode() {
            int result = sourceClass != null ? sourceClass.hashCode() : 0;
            result = 31 * result + (targetClass != null ? targetClass.hashCode() : 0);
            return result;
        }
    }
}
