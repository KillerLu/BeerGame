package com.jnu.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：Killer
 * @date ：Created in 19-7-4 上午9:33
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class CloneUtil {

    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 对象值属性赋值,空值在忽略
     * @param src
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }



    /**
     * 拷贝对象
     * @param source  被拷贝的对象
     * @param classType  拷贝后对象的类型
     * @return
     */
    public static <T, E> E clone(T source, Class<E> classType) {

        if (source == null) {
            return null;
        }
        E targetInstance = null;
        try {
            targetInstance = classType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(source, targetInstance);
        return targetInstance;
    }



    /**
     * 拷贝集合对象
     * @param sourceList
     * @param classType
     * @return
     */
    public static <T, E> List<E> batchClone(List<T> sourceList, Class<E> classType) {
        if (sourceList == null) {
            return new ArrayList<E>();
        }
        List<E> result = new ArrayList<E>();
        int size = sourceList.size();
        for (int i = 0; i < size; i++) {
            result.add(clone(sourceList.get(i), classType));
        }
        return result;
    }

}
