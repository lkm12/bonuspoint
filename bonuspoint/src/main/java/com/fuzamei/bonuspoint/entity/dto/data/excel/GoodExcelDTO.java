package com.fuzamei.bonuspoint.entity.dto.data.excel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

/**
 * Created by 18519 on 2018/5/12.
 * lkm
 */
@Log
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoodExcelDTO {

 /** 平台UID*/
 private Long platformUid;
 /** 平台ID*/
 private Long platformId;
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
 /** 当前页*/
 private Integer currentPage;
 /** 每页大小*/
 private Integer pageSize;

 private String url;
/**集团id*/
private Long groupId;

private Integer page;
/**起始时间*/
private Long startTime;

/**结束时间*/
private Long endTime;

 /**集团uid*/
 private Long uid;

}
