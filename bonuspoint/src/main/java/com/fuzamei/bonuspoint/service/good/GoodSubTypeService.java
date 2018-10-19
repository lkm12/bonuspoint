package com.fuzamei.bonuspoint.service.good;

import com.fuzamei.bonuspoint.entity.dto.good.GoodSubTypeDTO;
import com.fuzamei.common.model.vo.ResponseVO;

/**
 * 商品子分类service
 * @author liumeng
 * @create 2018年4月24日
 */
public interface GoodSubTypeService {
    /**
     * 添加商品子分类
     * @param goodSubTypeDTO 商品子分类信息
     *  @return
     */
    ResponseVO saveSubType(GoodSubTypeDTO goodSubTypeDTO);

    /**
     * 更新商品子分类信息
     * @param goodSubTypeDTO 商品子分类信息
     * @return
     */
    ResponseVO updateSubType(GoodSubTypeDTO goodSubTypeDTO);

    /**
     * 根据标识获取子分类信息
     * @param id 子分类标识
     * @return
     */
    ResponseVO getSubTypeById(Long id);

    /**
     * 根据父id获取对应的子分类
     * @param pid 父分类id
     * @return
     */
    ResponseVO getSubTypeByPid(Long pid);

    /**
     * 删除子分类
     * @param id
     * @return
     */
    ResponseVO deleteGoodSubType(Long id);
}
