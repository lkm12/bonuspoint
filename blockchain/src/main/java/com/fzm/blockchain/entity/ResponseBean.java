package com.fzm.blockchain.entity;

import lombok.Data;

/**
 * 响应bean
 *
 * @author wangtao
 * @create 2018/6/15
 */
@Data
public class ResponseBean<T> {
    private Integer id;
    private T result;
    private String error;
}
