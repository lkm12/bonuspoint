package com.fuzamei.bonuspoint.entity.vo.good;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lmm
 * @description
 * @create 2018/7/18 16:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeGoodVO {
    /** 父分类id*/
    private Long id;
    /** 父分类名称*/
    private String name;
    /** 图像路径*/
    private String img;
    /** 预览商品信息*/
    List<GoodVO> goods;
}
