/**
 * FileName: PlatformService
 * Author: 平台
 * Date: 2018/4/27 16:21
 * Description:
 */
package com.fuzamei.bonuspoint.service.point;

import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointInfoPO;
import com.fuzamei.common.model.vo.ResponseVO;

/**
 * 平台
 *
 * @author wangtao
 * @create 2018/4/27
 *
 */

public interface PlatformPointService {

    /**
     * 同意发放积分
     * @param uid uid
     * @param id id
     * @return
     */
    ResponseVO reviewPoint(Long uid, Long id);

    /**
     * 审核拒绝发放积分
     * @param uid 用户id
     * @param id  申请记录
     * @param reason 拒绝原因
     * @return
     */
    ResponseVO refusePoint(Long uid, Long id, String reason);

    /**
     * 平台查询积分审核列表
     * @param queryPointDTO
     * @return
     */
    ResponseVO pointIssueList(QueryPointDTO queryPointDTO);

    /**
     * 平台查看积分发放记录
     * @param queryDTO{
     *                 startTime:                 起始时间           非必需
     *                 endTime：                  结束时间           非必需
     *                 mobile                     模糊查询手机号      非必需
     *                 page：                     当前页             非必需
     *                 pageSize：                 页大小             非必需
     * @return
     */
    ResponseVO pointGrantList(QueryPointDTO queryDTO);

    /**
     * 通过平台id获取平台通用积分
     * @param platformId
     * @return
     */
    GeneralPointInfoPO getGeneralPointInfoByPlatformId(Long platformId);
}
