package com.fzm.blockchain.entity;

import lombok.Data;

/**
 * 请求bean
 *
 * @author wangtao
 * @create 2018/6/15
 */
@Data
public class DataBean {
    private Integer lang;
    private String seed;
    private String passwd;
    private String data;
    private String hash;

}
