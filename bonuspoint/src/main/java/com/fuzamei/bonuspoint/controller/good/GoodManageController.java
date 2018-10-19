package com.fuzamei.bonuspoint.controller.good;


import com.fuzamei.bonuspoint.aop.annotation.LogAnnotation;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.good.GoodDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import com.fuzamei.bonuspoint.service.good.GoodService;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品Controller
 *
 * @author liumeng
 * 2018年4月17日
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/good")
public class GoodManageController {

    private final GoodService goodService;

    @Autowired
    public GoodManageController(GoodService goodService) {
        this.goodService = goodService;
    }

    /**
     * 添加商品
     *
     * @param goodDTO 商品信息
     * @return
     */
    @PostMapping("/add")
    @LogAnnotation(note = "添加商品")
    public ResponseVO saveGood(@RequestAttribute("token") Token token, @RequestBody GoodDTO goodDTO) {
        if (goodDTO.getSid() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_SID);
        }
        if (goodDTO.getName() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_NAME);
        }
        if (goodDTO.getPrice() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_PRICE);
        }
        if (goodDTO.getGlobalPrice() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_GOLABEL_PRICE);
        }
        if (goodDTO.getNum() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_NUM);
        }
        if (goodDTO.getWorth() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_WORTH);
        }
        if (goodDTO.getRate()!=null&&goodDTO.getRate()<=0){
            return new ResponseVO(CommonResponseEnum.PARAMETER_ERROR);
        }

        return goodService.addGood(token.getUid(), goodDTO);
    }

    /**
     * 编辑商品信息
     *
     * @param goodDTO 商品信息
     * @return
     */
    @PostMapping("/update")
    public ResponseVO updateGood(@RequestAttribute("token") Token token, @RequestBody GoodDTO goodDTO) {
        log.info("修改商品信息");

        return goodService.updateGood(token.getUid(), goodDTO);
    }

    /**
     * 下架商品
     *
     * @param id 商品标识id
     * @return
     */
    @GetMapping("/drop/{id}")
    public ResponseVO dropGood(@RequestAttribute("token") Token token, @PathVariable Long id) {
        log.info("下架商品");
        return goodService.dropGood(id, token.getUid());
    }

    /**
     * 上架商品
     *
     * @param id 商品标识id
     * @return
     */
    @GetMapping("/shelf/{id}")
    public ResponseVO shelfGood(@RequestAttribute("token") Token token, @PathVariable Long id) {
        log.info("上架商品");
        return goodService.shelfGood(id, token.getUid());
    }

    /**
     * 删除商品
     *
     * @param id 商品标识id
     * @return
     */
    @GetMapping("/delete/{id}")
    public ResponseVO deleteGood(@RequestAttribute("token") Token token, @PathVariable Long id) {
        log.info("删除商品");

        return goodService.deleteGood(id, token.getUid());

    }
}
