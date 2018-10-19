package com.fuzamei.bonuspoint.entity.po.good;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品子分类PO
 * @author liumeng
 * @create 2018年4月24日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodSubTypePO {
    /** 商品子分类ID */
    private Long id ;
    /** 商品父分类ID*/
    private Long pid;
    /** 子分类名称 */
    private String name;
    /** 图像url,多个逗号分割*/
    private String img;
    /** 创建时间*/
    private Long createAt;
    /** 更新时间*/
    private Long updateAt;
}
