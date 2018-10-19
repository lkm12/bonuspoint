package com.fuzamei.bonuspoint.service.good;

import com.fuzamei.bonuspoint.entity.dto.good.GoodTypeDTO;
import com.fuzamei.common.model.vo.ResponseVO;

/**
 * 商品分类操作接口
 * @author liumeng
 * @create 2018年4月17日
 */
public interface GoodTypeService {
    /**
     * 保存商品父分类信息
     * @param goodTypeDTO 商品父分类信息
     * @param uid 操作id
     */
    ResponseVO saveGoodType(Long uid,GoodTypeDTO goodTypeDTO);

    /**
     * 更新商品父类型信息
     * @param goodTypeDTO 商品父类型信息
     * @return
     */
    ResponseVO updateGoodType(GoodTypeDTO goodTypeDTO);


    /**
     * 获取商品分类信息
     * @param id 分类标识
     * @return 分类信息
     */
    ResponseVO getGoodType(Long id);

    /**
     * 获取所有分类信息
     * @return 分类集合
     */
    ResponseVO listGoodType();
    /**
     * 显示所有商品分类
     * @return
     */
    ResponseVO listAllTypes();

    /**
     * 删除分类
     * @param id
     * @return
     */
    ResponseVO deleteGoodType(Long id);
}
