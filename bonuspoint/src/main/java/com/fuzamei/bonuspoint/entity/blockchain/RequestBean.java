/**
 * FileName: RequestBean
 * Author: wangtao
 * Date: 2018/5/3 14:30
 * Description:
 */
package com.fuzamei.bonuspoint.entity.blockchain;

import com.fuzamei.bonuspoint.constant.BlankChainUrlConst;
import lombok.Data;

/**
 *
 *
 * @author wangtao
 * @create 2018/5/3
 *
 */
@Data
public class RequestBean {

    private String jsonrpc;
    private String method;
    private Object id;
    private String[] params;

    public RequestBean(String[] params){
        this.jsonrpc = "2.0";
        this.method = BlankChainUrlConst.BROADCAST_TX_COMMIT;
        this.id = null;
        this.params = params;
    }
}
