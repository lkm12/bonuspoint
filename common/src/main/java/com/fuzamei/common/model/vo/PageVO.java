package com.fuzamei.common.model.vo;

import lombok.Data;


/**
 * @author qbanxiaoli
 * @description
 * @create 2018/7/3 13:54
 */
@Data
public class PageVO {
    /** 模糊匹配信息*/
    private String fuzzyMatch;
    /** 分页大小*/
    private Integer pageSize;
    /** 当前页数*/
    private Integer page;

    /** 起始时间 */
    private Long startTime;
    /** 结束时间 */
    private Long endTime;
}
