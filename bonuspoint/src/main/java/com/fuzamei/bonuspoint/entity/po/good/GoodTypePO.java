package com.fuzamei.bonuspoint.entity.po.good;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品父分类
 * @author liumeng
 * @create 2018年4月17日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodTypePO {
    /** 商品父分类标识 */
    private Long id;
    /** 商品父分类名称 */
    private String name;
    /** 图像url,多个逗号分割*/
    private String img;
}
