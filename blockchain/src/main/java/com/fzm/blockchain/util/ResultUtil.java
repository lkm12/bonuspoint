package com.fzm.blockchain.util;

import com.alibaba.fastjson.JSONObject;
import com.fzm.blockchain.entity.ResponseBean;

/**
 * 检查区块链返回结果
 *
 * @author wangtao
 * @create 2018/6/15
 */

public class ResultUtil {

    private ResultUtil() {
        throw new AssertionError("不能实例化 ResultUtil");
    }

    /**
     * 检查区块链返回的结果
     * @param result
     * @return
     * @throws Exception
     */
    public static Boolean checkResult(String result) throws Exception{
        return false;
    }

    /**
     * 获取返回信息
     * @param result
     * @return
     * @throws Exception
     */
    public static String resultMessage(String result) throws Exception{
        ResponseBean responseBean = JSONObject.parseObject(result, ResponseBean.class);

        String error = responseBean.getError();

        return null;
    }

    /**
     * 获取区块hash
     * @param result
     * @return
     * @throws Exception
     */
    public static String resultHash(String result) throws Exception{
        ResponseBean responseBean = JSONObject.parseObject(result, ResponseBean.class);

        return null;
    }

    /**
     * 获取区块高度
     * @param result
     * @return
     * @throws Exception
     */
    public static Integer resultHeight(String result) throws Exception{
        ResponseBean responseBean = JSONObject.parseObject(result, ResponseBean.class);

        return null;
    }

}
