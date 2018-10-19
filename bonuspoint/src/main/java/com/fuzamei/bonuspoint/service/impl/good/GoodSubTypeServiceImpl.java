package com.fuzamei.bonuspoint.service.impl.good;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fuzamei.bonuspoint.dao.good.GoodDao;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuzamei.bonuspoint.dao.good.GoodSubTypeDao;
import com.fuzamei.bonuspoint.dao.good.GoodTypeDao;
import com.fuzamei.bonuspoint.entity.dto.good.GoodSubTypeDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodSubTypePO;
import com.fuzamei.bonuspoint.entity.po.good.GoodTypePO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import com.fuzamei.bonuspoint.service.good.GoodSubTypeService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品子分类实现类
 *
 * @author liumeng
 * @create 2018年4月24日
 */
@Slf4j
@Service
public class GoodSubTypeServiceImpl implements GoodSubTypeService {

    private static final String ID = "id";

    private final GoodTypeDao goodTypeDao;

    private final GoodSubTypeDao goodSubTypeDao;

    private final GoodDao goodDao;

    @Autowired
    public GoodSubTypeServiceImpl(GoodTypeDao goodTypeDao, GoodSubTypeDao goodSubTypeDao, GoodDao goodDao) {
        this.goodTypeDao = goodTypeDao;
        this.goodSubTypeDao = goodSubTypeDao;
        this.goodDao = goodDao;
    }

    /**
     * 添加商品子分类
     *
     * @param goodSubTypeDTO 商品子分类信息
     * @return
     */
    @Override
    public ResponseVO saveSubType(GoodSubTypeDTO goodSubTypeDTO) {
        GoodSubTypePO goodSubTypePO = new GoodSubTypePO();
        if (goodSubTypeDTO.getPid() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_SUB_TYPE_PID);
        }
        GoodTypePO goodTypePO = goodTypeDao.getGoodType(goodSubTypeDTO.getPid());
        if (goodTypePO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_SUB_TYPE_PID_NOT_EXIT);
        }
        if (StringUtil.isBlank(goodSubTypeDTO.getName())) {
            return new ResponseVO(GoodResponseEnum.GOOD_SUB_TYPE_NAME);
        }
        goodSubTypePO.setPid(goodSubTypeDTO.getPid());
        goodSubTypePO.setName(goodSubTypeDTO.getName());
        goodSubTypePO.setImg(goodSubTypeDTO.getImg());
        goodSubTypePO.setCreateAt(System.currentTimeMillis());
        goodSubTypePO.setUpdateAt(System.currentTimeMillis());
        int i = goodSubTypeDao.saveSubType(goodSubTypePO);
        if (i == 0) {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        } else {
            Map<String, Long> map = new HashMap<>(4);
            map.put(ID, goodSubTypePO.getId());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
        }
    }

    /**
     * 更新商品子分类信息
     *
     * @param goodSubTypeDTO 商品子分类信息
     * @return
     */
    @Override
    public ResponseVO updateSubType(GoodSubTypeDTO goodSubTypeDTO) {
        if (goodSubTypeDTO.getId() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_SUB_TYPE_ID);
        }
        if (goodSubTypeDTO.getPid() != null) {
            GoodTypePO goodTypePO = goodTypeDao.getGoodType(goodSubTypeDTO.getPid());
            if (goodTypePO == null) {
                return new ResponseVO(GoodResponseEnum.GOOD_SUB_TYPE_PID_NOT_EXIT);
            }
        }
        GoodSubTypePO goodSubTypePO = new GoodSubTypePO();
        goodSubTypePO.setId(goodSubTypeDTO.getId());
        goodSubTypePO.setPid(goodSubTypeDTO.getPid());
        goodSubTypePO.setName(goodSubTypeDTO.getName());
        goodSubTypePO.setImg(goodSubTypeDTO.getImg());
        goodSubTypePO.setUpdateAt(System.currentTimeMillis());
        int i = goodSubTypeDao.updateSubType(goodSubTypePO);
        if (i == 0) {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        } else {
            Map<String, Long> map = new HashMap<>(4);
            map.put(ID, goodSubTypePO.getId());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
        }
    }

    /**
     * 根据标识获取子分类信息
     *
     * @param id 子分类标识
     * @return
     */
    @Override
    public ResponseVO getSubTypeById(Long id) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_SUB_TYPE_ID);
        }
        GoodSubTypePO goodSubTypePO = goodSubTypeDao.getSubTypeById(id);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, goodSubTypePO);
    }

    /**
     * 根据父id获取对应的子分类
     *
     * @param pid 父分类id
     * @return
     */
    @Override
    public ResponseVO getSubTypeByPid(Long pid) {
        if (pid == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_SUB_TYPE_PID);
        }
        List<GoodSubTypePO> goodSubTypePOs = goodSubTypeDao.getSubTypeByPid(pid);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, goodSubTypePOs);

    }

    @Override
    public ResponseVO deleteGoodSubType(Long id) {
        int count = goodDao.countGoodsBySubType(id);
        if (count >0){
            return new ResponseVO(GoodResponseEnum.SUB_TYPE_HAS_GOODS);
        }
        goodSubTypeDao.deleteSubType(id);
        return new ResponseVO(CommonResponseEnum.DELETE_SUCCESS);
    }
}
