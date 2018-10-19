package com.fuzamei.bonuspoint.service.impl.good;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fuzamei.bonuspoint.dao.good.GoodSubTypeDao;
import com.fuzamei.bonuspoint.entity.po.good.GoodSubTypePO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.good.GoodTypeDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.good.GoodTypeDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodTypePO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.entity.vo.good.GoodTypeVO;
import com.fuzamei.bonuspoint.service.good.GoodTypeService;
import com.fuzamei.bonuspoint.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品分类service
 *
 * @author liumeng
 * @create 2018年4月17日
 */
@Slf4j
@Service
public class GoodTypeServiceImpl implements GoodTypeService {

    private static final String ID = "id";

    private final GoodTypeDao goodTypeDao;
    private final GoodSubTypeDao goodSubTypeDao;


    @Autowired
    public GoodTypeServiceImpl(GoodTypeDao goodTypeDao, GoodSubTypeDao goodSubTypeDao) {
        this.goodTypeDao = goodTypeDao;
        this.goodSubTypeDao = goodSubTypeDao;
    }




    /**
     * 保存商品分类信息
     * @param goodTypeDTO 商品分类信息
     * @param uid 操作员id
     * @throws Exception 
     */
    @Override
    @Transactional
    public ResponseVO saveGoodType(Long uid, GoodTypeDTO goodTypeDTO) {
        if (StringUtil.isBlank(goodTypeDTO.getName())) {
            return new ResponseVO(GoodResponseEnum.GOOD_TYPE_NAME);
        }
        //向数据库插入商品
        GoodTypePO goodTypePO = new GoodTypePO();
        goodTypePO.setName(goodTypeDTO.getName());
        goodTypePO.setImg(goodTypeDTO.getImg());
        if (goodTypeDao.saveGoodType(goodTypePO) != 1) {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
        Map<String, String> map = new HashMap<>();
        map.put(ID, goodTypePO.getId().toString());
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }

    /**
     * 获取商品分类信息
     *
     * @param id 分类标识
     * @return 分类信息
     */
    @Override
    public ResponseVO getGoodType(Long id) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_TYPE_ID);
        }
        GoodTypePO goodTypePO = goodTypeDao.getGoodType(id);
        log.info("获取商品分类信息");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, goodTypePO);
    }

    /**
     * 获取所有分类信息
     *
     * @return 分类集合
     */
    @Override
    public ResponseVO listGoodType() {
        List<GoodTypePO> goodTypePOs = goodTypeDao.listGoodType();
        log.info("获取所有商品分类信息");
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, goodTypePOs);
    }

    /**
     * 更新商品类型信息
     *
     * @param goodTypeDTO 商品类型信息
     * @return
     */
    @Override
    public ResponseVO updateGoodType(GoodTypeDTO goodTypeDTO) {
        if (goodTypeDTO.getId() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_TYPE_ID);
        }
        GoodTypePO goodTypePO = new GoodTypePO();
        goodTypePO.setId(goodTypeDTO.getId());
        goodTypePO.setName(goodTypeDTO.getName());
        goodTypePO.setImg(goodTypeDTO.getImg());
        int i = goodTypeDao.updateGoodType(goodTypePO);
        Map<String, String> map = new HashMap<>(16);
        map.put(ID, i == 1 ? goodTypePO.getId().toString() : null);
        log.info("更新商品父分类信息");
        return new ResponseVO<>(i == 1 ? CommonResponseEnum.SUCCESS : CommonResponseEnum.FAILURE, map);
    }



    /**
     * 获取所有商品分类
     */
    @Override
    public ResponseVO listAllTypes() {
        List<GoodTypeVO> types = goodTypeDao.listAllTypes();
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, types);
    }

    @Override
    public ResponseVO deleteGoodType(Long id) {
        List<GoodSubTypePO> goodSubTypePOS = goodSubTypeDao.getSubTypeByPid(id);
        if (goodSubTypePOS==null|| goodSubTypePOS.size()<1){
            goodTypeDao.deleteGoodType(id);
            return new ResponseVO(CommonResponseEnum.DELETE_SUCCESS);
        }
        else {
            return new ResponseVO(GoodResponseEnum.GOOD_DELETE_SUB_TYPE_FIRST);
        }
    }
}
