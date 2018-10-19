package com.fuzamei.bonuspoint.controller.point;

import com.alibaba.fastjson.JSON;
import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.point.PointInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.point.MemberPointService;
import com.fuzamei.bonuspoint.service.point.PlatformPointService;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/9 10:36
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/point/platform")
public class PlatformPointQueryController {

    @Value("${page.pageSize}")
    private Integer size;

    @Value("${page.maxSize}")
    private Integer maxSize;

    private final PlatformPointService platformPointService;
    private final UserService userService;
    private final MemberPointService memberPointService;

    @Autowired
    public PlatformPointQueryController(PlatformPointService platformPointService,UserService userService,MemberPointService memberPointService) {
        this.platformPointService = platformPointService;
        this.userService = userService;
        this.memberPointService = memberPointService;
    }

    /**
     * 平台查看发行审批积分记录
     *
     * @param token    token&uid
     * @param queryDTO {
     *                 startTime:                 起始时间           非必需
     *                 endTime：                  结束时间           非必需
     *                 fuzzyMatch                 模糊查询词         非必需
     *                 page：                     当前页             非必需
     *                 pageSize：                 页大小             非必需
     *                 status:                   积分状态           非必需
     *                 }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "平台查看发行审批记录")
    @PostMapping("point-issue-list")
    public ResponseVO<PageBean<PointInfoDTO>> getIssuePointList(@RequestAttribute("token") Token token, @RequestBody QueryPointDTO queryDTO) {
        // 设置默认值
        if (queryDTO.getPage() == null || queryDTO.getPage() <= 0) {
            queryDTO.setPage(1);
        }
        // 设置默认值
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() <= 0) {
            queryDTO.setPageSize(size);
        }
        if (queryDTO.getPageSize() > maxSize) {
            queryDTO.setPageSize(maxSize);
        }

        queryDTO.setIssuePlatform(token.getUid());

        return platformPointService.pointIssueList(queryDTO);
    }

    /**
     * 平台查看积分发放记录
     * @param token
     * @param queryDTO{
     *                 startTime:                 起始时间           非必需
     *                 endTime：                  结束时间           非必需
     *                 mobile                     模糊查询手机号      非必需
     *                 page：                     当前页             非必需
     *                 pageSize：                 页大小             非必需
     * @return
     */
    @LogAnnotation(note = "平台查看积分发放记录")
    @PostMapping("point-grant-list")
    public ResponseVO getGrantPointList(@RequestAttribute("token") Token token, @RequestBody QueryPointDTO queryDTO){

        // 设置默认值
        if (queryDTO.getPage() == null || queryDTO.getPage() <= 0) {
            queryDTO.setPage(1);
        }
        // 设置默认值
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() <= 0) {
            queryDTO.setPageSize(size);
        }
        if (queryDTO.getPageSize() > maxSize) {
            queryDTO.setPageSize(maxSize);
        }
        queryDTO.setIssuePlatform(token.getUid());
        queryDTO.setUid(token.getUid());
        return platformPointService.pointGrantList(queryDTO);
    }

    /**
     * 获取积分兑换明细（平台）
     * lkm
     *
     * @param jsonData 接收参数
     *                 {
     *                 平台uid：平台的uid（token中获取）
     *                 mobile:用户手机（非必需）
     *                 statTime:开始时间（非必需）
     *                 endTime:结束时间（非必需）
     *                 companyName:商户名称（非必需）
     *                 }
     * @return 响应
     */
    @PostMapping("exchangeGeneralPlatform")
    public ResponseVO exchangeGeneralPlatform(@RequestAttribute("token") Token token, @RequestBody String jsonData) {


        PagePointDTO pagePointDTO;
        // 将接收的json数据转换为数据传输对象
        try {
            pagePointDTO = JSON.parseObject(jsonData, PagePointDTO.class);
        } catch (Exception e) {
            log.info("参数错误");
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }
        pagePointDTO.setId(token.getUid());
        return memberPointService.exchangeGeneralPlatform(pagePointDTO);
    }

    /**
     * 积分使用详情（总平台，平台）
     * lkm
     *
     * @param jsonData 接收参数
     *                 {
     *
     *                 id:用户id
     *
     *                 }
     * @return 响应
     */
    @PostMapping("userPointInfo")
    public ResponseVO userPointInfo(@RequestAttribute("token") Token token, @RequestBody String jsonData) {


        PagePointDTO pagePointDTO;
        // 将接收的json数据转换为数据传输对象
        try {
            pagePointDTO = JSON.parseObject(jsonData, PagePointDTO.class);
        } catch (Exception e) {
            log.info("参数错误");
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }
        if(StringUtil.isBlank(String.valueOf(pagePointDTO.getId()))){
            return new ResponseVO(CommonResponseEnum.PARAMETER_BLANK);
        }
        return memberPointService.userPointInfo(pagePointDTO);
    }

}
