package com.fuzamei.bonuspoint.controller.point;

import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.data.excel.GoodExcelDTO;
import com.fuzamei.bonuspoint.entity.dto.point.*;
import com.fuzamei.bonuspoint.entity.dto.point.PointInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointRecordPO;
import com.fuzamei.bonuspoint.entity.vo.point.CompanyPointVO;
import com.fuzamei.bonuspoint.entity.form.point.QueryPointRecordFORM;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.point.CompanyPointService;
import com.fuzamei.bonuspoint.service.point.MemberPointService;
import com.fuzamei.bonuspoint.service.user.company.CompanyInfoService;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.common.bean.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fuzamei.common.model.vo.ResponseVO;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/5/7 10:33
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/point/company")
public class CompanyPointQueryController {

    @Value("${page.pageSize}")
    private Integer pageSize;

    @Value("${page.maxSize}")
    private Integer maxSize;

    private final CompanyPointService companyPointService;

    private final CompanyInfoService companyInfoService;

    private final UserService userService;

    private final MemberPointService memberPointService;

    @Autowired
    public CompanyPointQueryController(CompanyPointService companyPointService, CompanyInfoService companyInfoService,UserService userService,
                                       MemberPointService memberPointService) {
        this.companyPointService = companyPointService;
        this.companyInfoService = companyInfoService;
        this.userService = userService;
        this.memberPointService = memberPointService;
    }

    /**
     * @param token 令牌 (required = true)
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取通用积分结算信息接口
     */
    @GetMapping("/balance-info/get")
    public ResponseVO<CompanyPointVO> getBalanceInfoRecord(@RequestAttribute("token") Token token) {
        log.info("获取集团已兑换通用积分列表");
        // 请求服务
        log.info("参数校验正常");
        return companyPointService.getBalanceInfoRecord(token.getUid());
    }

    /**
     * @param token           令牌 (required = true)
     * @param companyPointDTO 请求参数
     *                        {
     *                        startTime: 查询起始时间 (required = false)
     *                        endTime: 查询截止时间 (required = false)
     *                        currentPage: 当前页 (required = false)
     *                        pageSize: 每页记录数 (required = false)
     *                        }
     * @return 请求响应
     * @author qbanxiaoli
     * @description 获取通用积分结算记录接口
     */
    @PostMapping("/point-exchange/list")
    public ResponseVO<PageBean<GeneralPointRecordPO>> listPointExchangeRecord(@RequestAttribute("token") Token token,
                                                                              @RequestBody CompanyPointDTO companyPointDTO) {
        log.info("获取通用积分结算记录");
        companyPointDTO.setUid(token.getUid());
        // 请求服务
        log.info("参数校验正常");
        return companyPointService.listPointExchangeRecord(companyPointDTO);
    }

    /**
     * 集团已发放积分列表
     *
     * @param queryPointRecordFORM 请求参数
     *                 {
     *                 mobile: 手机号
     *                 startTime: 起始时间
     *                 endTime: 结束时间
     *                 page: 当前页
     *                 pageSize: 每页记录数
     *                 }
     * @return 响应
     */
    @PostMapping("/grant-list")
    @LogAnnotation(note = "集团查看已发放积分列表")
    public ResponseVO grantPointList(@RequestAttribute("token") Token token, @RequestBody QueryPointRecordFORM queryPointRecordFORM) {
        // 设置默认值
        if (queryPointRecordFORM.getPage() == null || queryPointRecordFORM.getPage() <= 0) {
            queryPointRecordFORM.setPage(1);
        }
        // 设置默认值
        if (queryPointRecordFORM.getPageSize() == null || queryPointRecordFORM.getPageSize() <= 0) {
            queryPointRecordFORM.setPageSize(pageSize);
        }
        if (queryPointRecordFORM.getPageSize() > maxSize) {
            queryPointRecordFORM.setPageSize(maxSize);
        }

        PointRecordDTO pointRecordDTO = new PointRecordDTO();
        BeanUtils.copyProperties(queryPointRecordFORM,pointRecordDTO);
        pointRecordDTO.setUid(token.getUid());
        return companyPointService.grantPointList(token.getUid(), pointRecordDTO);
    }

    /**
     * 获取管理集团资产信息
     *
     * @return
     */
    @GetMapping("/asset")
    public ResponseVO queryCompanyPointAsset(@RequestAttribute("token") Token token) {
        return companyPointService.companyPointAsset(token.getUid());
    }


    /**
     * 集团查看积分发行记录
     *
     * @param token    token&uid
     * @param queryDTO {
     *                 startTime:                 起始时间            非必需
     *                 endTime：                  结束时间            非必需
     *                 name：                     活动名，积分名称     非必需，支持模糊查询
     *                 page：                     当前页             非必需
     *                 pageSize：                 页大小             非必需
     *                 status:                    积分状态           非必需
     *                 }
     * @return
     * @author wangjie
     */
    @LogAnnotation(note = "集团查看积分发行记录")
    @PostMapping("point-issue-list")
    public ResponseVO<PageBean<PointInfoDTO>> getIssuePointList(@RequestAttribute("token") Token token, @RequestBody QueryPointDTO queryDTO) {

        // 设置默认值
        if (queryDTO.getPage() == null || queryDTO.getPage() <= 0) {
            queryDTO.setPage(1);
        }
        // 设置默认值
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() <= 0) {
            queryDTO.setPageSize(pageSize);
        }
        if (queryDTO.getPageSize() > maxSize) {
            queryDTO.setPageSize(maxSize);
        }
        Long companyId = companyInfoService.getCompanyIdByUid(token.getUid());
        if (companyId == 0) {
            return new ResponseVO<>(UserResponseEnum.NO_COMPANY_INFO);
        }
        queryDTO.setCompany(companyId);
        queryDTO.setUid(token.getUid());
        return companyPointService.pointIssueList(queryDTO);
    }

    /**
     * 显示集团活动信息
     * @param token token
     * @param showOutTime 是否显示过期活动
     * @return
     */
    @GetMapping("/activities")
    public ResponseVO listActivity(@RequestAttribute("token") Token token,
                                   @RequestParam(required = false,defaultValue = "0") Integer showOutTime) {
        log.info("获取集团进行的活动");
        Boolean isShow = showOutTime == 1 ? true : false;
        return companyPointService.listActivity(token.getUid(), isShow);
    }


    /**
     * 获取结算信息(集团)
     * lkm
     *
     * @param jsonData 接收参数
     *                 {
     *                 startTime:开始时间（非必需）
     *                 endTime:结束时间（非必需）
     *                 }
     * @return 响应
     */
    @PostMapping("exchange-info")
    public ResponseVO memberExchangeInfo(@RequestAttribute("token") Token token, @RequestBody String jsonData) {


        PagePointDTO pagePointDTO;
        // 将接收的json数据转换为数据传输对象
        try {
            pagePointDTO = JSON.parseObject(jsonData,  PagePointDTO.class);
        } catch (Exception e) {
            log.info("参数错误");
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }

        pagePointDTO.setId(token.getUid());

        return memberPointService.memberExchangeInfo(pagePointDTO);
    }

    /**
     * (集团)用户积分兑换记录
     * lkm
     * uid:集团的uid（从token中获取）,
     * <p>
     * <p>
     * startTime:开始时间，
     * endTime:结束时间，
     * mobile:手机号，
     * page:当前页数
     * pageSize:分页大小
     * @param jsonData 请求参数
     * @return 请求响应
     */
    @PostMapping("/exchangeGeneralCompany")
    public ResponseVO exchangeGeneralCompany(@RequestAttribute("token") Token token, @RequestBody String jsonData) {


        GoodExcelDTO goodExcelDTO;

        try {
            goodExcelDTO = JSON.parseObject(jsonData, GoodExcelDTO.class);
        } catch (Exception e) {
            log.info("参数错误");
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }

        log.info("参数校验正常");
        goodExcelDTO.setUid(token.getUid());
        return companyPointService.exchangeGeneralCompany(goodExcelDTO);

    }

}
