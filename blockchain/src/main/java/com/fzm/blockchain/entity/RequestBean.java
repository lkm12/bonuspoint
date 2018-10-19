package com.fzm.blockchain.entity;

import com.fzm.blockchain.constant.BlankChainUrlConst;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求bean
 *
 * @author wangtao
 * @create 2018/6/15
 */
@Data
public class RequestBean<T> {
    private String jsonrpc;
    private String method;
    private Object id;
    private List<T> params;

    public RequestBean(T params){
        this.jsonrpc = "2.0";
        this.method = BlankChainUrlConst.SEND_TRANSACTION;
        this.id = null;
        this.params = new ArrayList<>();
        this.params.add(params);
    }

    public RequestBean(T params, String blankChainUrlConst){
        this.jsonrpc = "2.0";
        this.method = blankChainUrlConst;
        this.id = null;
        this.params = new ArrayList<>();
        this.params.add(params);
    }

}
