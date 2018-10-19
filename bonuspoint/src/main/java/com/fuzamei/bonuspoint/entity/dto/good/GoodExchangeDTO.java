package com.fuzamei.bonuspoint.entity.dto.good;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品兑换查询DTO
 * @author liumeng
 * @create 2018年6月29日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodExchangeDTO {
    /** 平台ID*/
    private Long platformId;
    /** 集团id*/
    private Long companyId;
    /** 分类ID*/
    private Long typeId;
    /** 子分类ID*/
    private Long subTypeId;
    /** 开始时间戳*/
    private Long begin;
    /** 查询结束时间戳*/
    private Long end;
    /** 手机号*/
    private String mobile;
    /** 商品名称*/
    private String goodName;
    /** 集团名称*/
    private String companyName;
    /** 兑换时间*/
    private Long  updateTime;
    /** 当前页*/
    private Integer currentPage;
    /** 每页大小*/
    private Integer pageSize;
    
}
