package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.good.GoodExchangeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryGoodDTO;
import com.fuzamei.bonuspoint.service.good.GoodService;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品Controller
 *
 * @author liumeng
 * @create 2018年4月17日
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/good")
public class GoodQueryController {

    private final GoodService goodService;

    @Autowired
    public GoodQueryController(GoodService goodService) {
        this.goodService = goodService;
    }

    /**
     * 获取指定商品信息
     *
     * @param id 商品id
     * @return
     */
    @GetMapping("/get/{id}")
    public ResponseVO getGood(@PathVariable Long id) {

        return goodService.getGoodInfo(id);
    }

    /**
     * 查询商品信息
     * @param queryGoodDTO 查询条件
     * @return
     */
    @PostMapping("/query")
    public ResponseVO queryGood(@RequestBody QueryGoodDTO queryGoodDTO) {
        return goodService.queryGood(queryGoodDTO);
    }

    /**
     * 集团用户查询商品信息（只显示本集团）
     * @param token
     * @param queryGoodDTO
     * @return
     */
    @PostMapping("/company/query")
    public ResponseVO companyQueryGood(@RequestAttribute("token") Token token, @RequestBody QueryGoodDTO queryGoodDTO) {
        return goodService.companyQueryGood(token.getUid(), queryGoodDTO);
    }

    /**
     * 查询商品状态
     * @return
     */
    @GetMapping("/status/list")
    public ResponseVO listGoodStatus() {
        return goodService.ListGoodStatus();
    }

    /**
     * 查询商品兑换信息
     * @param exchangeDTO 查询条件
     * @return
     */
    @PostMapping("/exchanges")
    public ResponseVO queryGoodExchanges(@RequestAttribute("token") Token token, @RequestBody GoodExchangeDTO exchangeDTO) {
        log.info("查询商品兑换信息");
        return goodService.queryGoodExchanges(token.getUid(), exchangeDTO);
    }

    /**
     * app 首页显示
     * @param size 首页显示条数
     * @param pid 平台id
     * @return
     */
    @GetMapping("/appshow")
    public ResponseVO appshow(@RequestParam(required = false, defaultValue = "40") Integer size,
                              @RequestParam (required = false,defaultValue = "1") Long pid) {
        return goodService.appshow(size,pid);
    }

    /**
     * app 分类预览
     * @return
     */
    @GetMapping("/typeshow")
    public ResponseVO appTypeShow(@RequestParam (required = false,defaultValue = "1" ) Long pid) {
        return goodService.previewTypeGood(pid);
    }

    /**
     * app 预览商家品牌
     * @return
     */
    @GetMapping("/companyshow")
    public ResponseVO previewCompany(@RequestParam (required = false,defaultValue = "1" ) Long pid){
        return goodService.previewCompany(pid);
    }

    @GetMapping("/company-goods-info/{companyId}")
    public ResponseVO getCompanyGoodsInfo(@PathVariable("companyId") Long companyId){
        return goodService.getCompanyGoodsInfo(companyId);
    }

}
