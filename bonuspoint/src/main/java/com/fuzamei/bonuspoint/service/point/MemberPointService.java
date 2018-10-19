/**
 * FileName: MemberService
 * Author: wangtao
 * Date: 2018/4/24 21:00
 * Description:
 */
package com.fuzamei.bonuspoint.service.point;

import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.QueryUserDTO;
import com.fuzamei.bonuspoint.entity.po.point.MemberPointPO;
import com.fuzamei.common.model.vo.ResponseVO;

import java.util.List;

/**
 *
 *
 * @author wangtao
 * @create 2018/4/24
 *
 */

public interface MemberPointService {
    /**
     * 获取会员积分明细，支持分页
     *
     * @param queryDTO 数据传输类
     * @return 响应类
     */
    ResponseVO memberPointListDetail(QueryPointDTO queryDTO);


    /**
     * 兑换通用积分的积分列表获取
     *
     * @param queryPointDTO 数据传输类
     * @return 响应类
     */
    ResponseVO memberPointListRelation(QueryPointDTO queryPointDTO);

    /**获取结算信息*/
    ResponseVO memberExchangeInfo(PagePointDTO pagePointDTO);

    /**获取可转积分列表*/
    ResponseVO memberTranPointList(ExchangePointDTO exchangeDTO);



    /**
     * 商家获取会员信息
     * @param queryUserDTO
     * @return
     */
    ResponseVO memberPointInfo(QueryUserDTO queryUserDTO);

    List<MemberPointPO> getAllMemberPointInfo(QueryUserDTO queryUserDTO);

    /**获取积分兑换明细（平台）*/
    ResponseVO exchangeGeneralPlatform(PagePointDTO pagePointDTO);


    /**积分使用详情（总平台，平台）*/
    ResponseVO userPointInfo(PagePointDTO pagePointDTO);

    /** 获取积分使用明细详情（用户）*/
    ResponseVO pointListInfo(ExchangePointDTO exchangePointDTO);


}
