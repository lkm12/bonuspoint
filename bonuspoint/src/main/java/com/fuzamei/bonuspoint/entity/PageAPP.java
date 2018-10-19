package com.fuzamei.bonuspoint.entity;

import lombok.Data;

/**
 * 分页
 */
@Data
public class PageAPP {

    /** 分页大小 */
    protected String size;
    /** 当前页数 */
    protected String page;
    /** 查询开始行号 */
    protected Integer begin;

    /** 自动设置 */
    public Integer pageSize;
    public Integer pageNO;

    public void initPage(){
        this.pageSize = Integer.valueOf(size);
        this.pageNO = Integer.valueOf(page);
        this.begin = (this.pageNO - 1) * this.pageSize;
    }
}
