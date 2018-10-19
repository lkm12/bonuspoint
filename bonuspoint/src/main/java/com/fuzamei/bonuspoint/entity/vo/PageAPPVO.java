package com.fuzamei.bonuspoint.entity.vo;

import com.fuzamei.bonuspoint.entity.PageAPP;
import lombok.Data;

/**
 * 分页
 * @param <T>
 */
@Data
public class PageAPPVO<T> {

    PageAPP pageAPP = new PageAPP();

    public PageAPPVO(Integer pageSize,Integer pageNO){
        pageAPP.setPageNO(pageNO);
        pageAPP.setPageSize(pageSize);
    }
    /** 总条数 */
    private String totalSize;
    /** 总页数 */
    private String totalPage;
    /** 是否有下一页 */
    private String isMore;
    /** 分页结果 */
    private T data;

   public String pageSize;
   public String pageNO;

    public String getPageSize() {
        return pageSize;
    }

    public void initPageVOByTotalSize(Integer totalSize){
        this.totalSize = String.valueOf(totalSize);
        this.totalPage = String.valueOf((totalSize % pageAPP.pageSize > 0) ? (totalSize / pageAPP.pageSize + 1) : (totalSize / pageAPP.pageSize));
        this.isMore =  String.valueOf ((pageAPP.pageNO < Integer.valueOf(totalPage)) ? 1 : 0);
        pageAPP = null;
    }
}
