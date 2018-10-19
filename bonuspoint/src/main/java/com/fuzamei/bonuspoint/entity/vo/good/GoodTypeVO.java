package com.fuzamei.bonuspoint.entity.vo.good;

import java.util.List;

import com.fuzamei.bonuspoint.entity.po.good.GoodSubTypePO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品分类VO
 * @author liumeng
 * @create 2018年6月20日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodTypeVO {
    /** 分类id */
    private Long id ;
    /** 分类名称*/
    private String name;
    /** 图像url,多个逗号分割*/
    private String img;
    /** 子分类**/
    List<GoodSubTypePO> subTypes;
}
