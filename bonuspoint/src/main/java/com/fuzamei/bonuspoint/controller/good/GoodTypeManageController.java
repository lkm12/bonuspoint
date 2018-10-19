package com.fuzamei.bonuspoint.controller.good;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.good.GoodSubTypeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.GoodTypeDTO;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import com.fuzamei.bonuspoint.service.good.GoodSubTypeService;
import com.fuzamei.bonuspoint.service.good.GoodTypeService;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品分类(父分类和子分类)操作Controller
 *
 * @author liumeng
 * @create 2018年4月24日
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/goodtype")
public class GoodTypeManageController {

    @Autowired
    private GoodTypeService goodTypeService;
    @Autowired
    private GoodSubTypeService goodSubTypeService;

    /**
     * 添加商品父分类
     * @param goodTypeDTO 分类信息
     * @return
     */
    @PostMapping("/types")
    public ResponseVO saveGoodType(@RequestAttribute("token") Token token, @RequestBody GoodTypeDTO goodTypeDTO) {
        log.info("添加商品父分类");
        if (goodTypeDTO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_TYPE_FAILED);
        }

        return goodTypeService.saveGoodType(token.getUid(), goodTypeDTO);
    }

    @DeleteMapping("/platform/types/delete/{id}")
    public ResponseVO deleteGoodType(@RequestAttribute("token") Token token,@PathVariable("id") Long id){
        return goodTypeService.deleteGoodType(id);
    }
    @DeleteMapping("/platform/subTypes/delete/{id}")
    public ResponseVO deleteGoodSubType(@RequestAttribute("token") Token token,@PathVariable("id") Long id){
        return goodSubTypeService.deleteGoodSubType(id);
    }

    /**
     * 修改商品父分类
     *
     * @param goodTypeDTO 分类信息
     * @param id          分类标识
     * @return
     */
    @PutMapping("/types/{id}")
    public ResponseVO updateGoodType(@PathVariable Long id, @RequestBody(required = false) GoodTypeDTO goodTypeDTO) {
        log.info("修改商品父分类信息");

        if (goodTypeDTO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_TYPE_FAILED);
        }
        goodTypeDTO.setId(id);
        return goodTypeService.updateGoodType(goodTypeDTO);
    }

    /**
     * 删除商品分类
     * @param id 分类id
     * @return
     */
    @DeleteMapping("/types/{id}")
    public ResponseVO deleteType(@PathVariable Long id){
        return goodTypeService.deleteGoodType(id);
    }


    /**
     * 添加子分类信息
     *
     * @param goodSubTypeDTO 子分类信息
     * @return
     */
    @PostMapping("/subtypes")
    public ResponseVO saveSubType(@RequestBody(required = false) GoodSubTypeDTO goodSubTypeDTO) {

        if (goodSubTypeDTO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_TYPE_FAILED);
        }
        return goodSubTypeService.saveSubType(goodSubTypeDTO);
    }

    /**
     * 修改商品子分类
     *
     * @param id          子类ID
     * @return
     */
    @PutMapping("/subtypes/{id}")
    public ResponseVO updateSubType(@PathVariable Long id,
            @RequestBody(required = false) GoodSubTypeDTO goodSubTypeDTO) {

        if (goodSubTypeDTO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_TYPE_FAILED);
        }
        goodSubTypeDTO.setId(id);
        return goodSubTypeService.updateSubType(goodSubTypeDTO);
    }




}
