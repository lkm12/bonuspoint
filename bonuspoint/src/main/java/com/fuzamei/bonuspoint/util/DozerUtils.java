package com.fuzamei.bonuspoint.util;

import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description: 将一个对象装配成另一种对象，复杂相同属性的成员变量值
 * @author: WangJie
 * @create: 2018-05-09 15:12
 **/
public class DozerUtils {
    private static DozerBeanMapper mapper = new DozerBeanMapper();

    /**
     * 单个对象数据转换
     * @param source
     * @param destinationClass
     * @param <T>
     * @return
     */
    public static <T> T convert(Object source, Class<T> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 列表数据转换
     * @param sourceList
     * @param destinationClass
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T,S> List<T> convertList(List<S> sourceList, Class<T> destinationClass) {
        if(CollectionUtils.isNotEmpty(sourceList)){
            List<T> retList = new ArrayList<T>();
            for(S source : sourceList){
                retList.add(mapper.map(source, destinationClass));
            }
            return retList;
        }
        return null;
    }
}
