package com.fuzamei.bonuspoint.controller.good;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.good.GoodSubTypeService;
import com.fuzamei.bonuspoint.service.good.GoodTypeService;

/**
 * 商品分类(父分类和子分类)查询Controller
 *
 * @author liumeng
 * @create 2018年4月24日
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/goodtype")
public class GoodTypeQueryController {

    private final GoodTypeService goodTypeService;

    private final GoodSubTypeService goodSubTypeService;

    @Autowired
    public GoodTypeQueryController(GoodTypeService goodTypeService, GoodSubTypeService goodSubTypeService) {
        this.goodTypeService = goodTypeService;
        this.goodSubTypeService = goodSubTypeService;
    }

    /**
     * 获取对应分类信息
     *
     * @param id 分类ID
     * @return
     */
    @GetMapping("/types/{id}")
    public ResponseVO getGoodType(@PathVariable Long id) {
        log.info("获取商品分类信息");
        return goodTypeService.getGoodType(id);
    }

    /**
     * 获取所有父分类
     *
     * @return
     */
    @GetMapping("/types")
    public ResponseVO listTypes() {
        log.info("获取所有商品分类信息");
        return goodTypeService.listGoodType();
    }

    /**
     * 获取对应子分类
     *
     * @param id 子分类ID
     * @return
     */
    @GetMapping("/subtypes/{id}")
    public ResponseVO getSubtype(@PathVariable Long id) {
        log.info("获取子分类信息");
        return goodSubTypeService.getSubTypeById(id);
    }

    /**
     * 获取对应子分类信息
     *
     * @param pid 父分类标识
     * @return
     */
    @GetMapping("/types/{pid}/subtypes")
    public ResponseVO listSubType(@PathVariable Long pid) {
        log.info("获取指定父分类子类信息");
        return goodSubTypeService.getSubTypeByPid(pid);
    }
    /**
     * 获取所有分类信息
     */
    @GetMapping("/alltypes/list")
    public ResponseVO listAllTypes() {
        log.info("获取所有分类信息");
        return goodTypeService.listAllTypes();
    }
}
