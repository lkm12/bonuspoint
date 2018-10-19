package com.fuzamei.bonuspoint.service.impl.good;

import com.fuzamei.bonuspoint.constant.CompanyStatus;
import com.fuzamei.bonuspoint.constant.GoodRebateConstant;
import com.fuzamei.bonuspoint.constant.GoodStatusConstant;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.dao.common.mapper.CompanyInfoMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.GoodRebateMapper;
import com.fuzamei.bonuspoint.dao.good.GoodDao;
import com.fuzamei.bonuspoint.dao.user.CompanyInfoDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.good.CompanyGoodsInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.good.GoodDTO;
import com.fuzamei.bonuspoint.entity.dto.good.GoodExchangeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryGoodDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodPO;
import com.fuzamei.bonuspoint.entity.po.good.GoodRebatePO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.vo.good.GoodExchangeVO;
import com.fuzamei.bonuspoint.entity.vo.good.GoodVO;
import com.fuzamei.bonuspoint.entity.vo.good.TypeGoodVO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.GoodResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.good.GoodService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.fuzamei.common.bean.FastDFSClient;
import com.fuzamei.common.model.vo.ResponseVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liumeng
 * @create 2018年4月18日
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodServiceImpl implements GoodService {


    private static final String ID = "id";

    private final GoodDao goodDao;

    private final CompanyInfoDao companyInfoDao;

    private final UserDao userDao;

    private final CompanyInfoMapper companyInfoMapper;
    private final GoodRebateMapper goodRebateMapper;

    @Autowired
    public GoodServiceImpl(GoodRebateMapper goodRebateMapper, CompanyInfoMapper companyInfoMapper, GoodDao goodDao, CompanyInfoDao companyInfoDao, UserDao userDao) {
        super();
        this.goodDao = goodDao;
        this.companyInfoDao = companyInfoDao;
        this.userDao = userDao;

        this.companyInfoMapper = companyInfoMapper;
        this.goodRebateMapper = goodRebateMapper;
    }

    /**
     * 上架商品
     *
     * @param uid     用户id
     * @param goodDTO 商品信息
     * @return
     */
    @Override
    public ResponseVO addGood(Long uid, GoodDTO goodDTO) {
        //获取用户集团信息
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfo(uid);
        //检查商户是否被关闭
        if (companyInfoPO.getCompanyStatus() != null && companyInfoPO.getCompanyStatus().equals(CompanyStatus.COMPANY_SHOT_DOWN)) {
            return new ResponseVO(UserResponseEnum.COMPANY_HAS_BEEN_DELETED);
        }
        GoodPO goodPO = new GoodPO();
        goodPO.setGid(companyInfoPO.getId());
        goodPO.setSid(goodDTO.getSid());
        goodPO.setName(goodDTO.getName());
        goodPO.setPrice(goodDTO.getPrice());
        goodPO.setGlobalPrice(goodDTO.getGlobalPrice());
        goodPO.setNum(goodDTO.getNum());
        goodPO.setWorth(goodDTO.getWorth());
        goodPO.setDetails(goodDTO.getDetails());
        goodPO.setTopLevel(goodDTO.getTopLevel());
        goodPO.setOrderLevel(goodDTO.getOrderLevel());

        if (goodDTO.getStartAt() != null || goodDTO.getEndAt() != null) {
            goodPO.setIsLife(true);
        }
        if (goodDTO.getEndAt() != null) {
            if (goodDTO.getEndAt() < System.currentTimeMillis()) {
                return new ResponseVO(GoodResponseEnum.GOOD_LIFE_ERROR);
            }
            if ((goodDTO.getStartAt() != null) && goodDTO.getEndAt() < goodDTO.getStartAt()) {
                return new ResponseVO(GoodResponseEnum.GOOD_LIFE_ERROR);
            }
        }

        //设置开始时间
        goodPO.setStartAt(goodDTO.getStartAt());
        //设置结束时间
        goodPO.setEndAt(goodDTO.getEndAt());
        goodPO.setStatus(GoodStatusConstant.SALE);
        goodPO.setImgSrc(goodDTO.getImageUrls());
        Map<String, String> map = new HashMap<>(16);
        int result = goodDao.savaGood(goodPO);
        if (result == 1) {
            if (goodDTO.getRate() != null) {
                GoodRebatePO goodRebatePO = new GoodRebatePO();
                goodRebatePO.setGoodId(goodPO.getId());
                goodRebatePO.setStatus(GoodRebateConstant.PROCESSING);
                goodRebatePO.setRate(goodDTO.getRate());
                goodRebatePO.setCreatedAt(TimeUtil.timestamp());
                goodRebateMapper.insertSelective(goodRebatePO);
            }
            map.put(ID, goodPO.getId().toString());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
        } else {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
    }

    /**
     * 更新商品
     *
     * @param uid     用户id
     * @param goodDTO 商品信息
     * @return
     */
    @Override
    public ResponseVO updateGood(Long uid, GoodDTO goodDTO) {
        //获取用户集团信息
        GoodPO goodPO = new GoodPO();
        if (goodDTO.getId() == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ID);
        }
        Long selectUid = goodDao.getGoodUid(goodDTO.getId());
        if (!uid.equals(selectUid)) {
            return new ResponseVO(UserResponseEnum.USER_NOT_ROOT);
        }
        GoodPO selectGoodPo = goodDao.getGood(goodDTO.getId());
        goodPO.setId(goodDTO.getId());
        //      goodPO.setGid(goodDTO.getGid());
        goodPO.setSid(goodDTO.getSid());
        goodPO.setName(goodDTO.getName());
        goodPO.setPrice(goodDTO.getPrice());
        goodPO.setGlobalPrice(goodDTO.getGlobalPrice());
        goodPO.setNum(goodDTO.getNum());
        goodPO.setWorth(goodDTO.getWorth());
        goodPO.setDetails(goodDTO.getDetails());
        goodPO.setTopLevel(goodDTO.getTopLevel());
        goodPO.setOrderLevel(goodDTO.getOrderLevel());
        if (goodDTO.getStartAt() != null || goodDTO.getEndAt() != null) {
            goodPO.setIsLife(true);
        }
        if (goodDTO.getEndAt() != null) {
            if (goodDTO.getEndAt() < System.currentTimeMillis()) {
                return new ResponseVO(GoodResponseEnum.GOOD_LIFE_ERROR);
            }
            if (goodDTO.getStartAt() == null) {
                if (selectGoodPo.getStartAt() != null && goodDTO.getEndAt() < selectGoodPo.getStartAt()) {
                    return new ResponseVO(GoodResponseEnum.GOOD_LIFE_ERROR);
                }
            } else {
                if (goodDTO.getEndAt() < goodDTO.getStartAt()) {
                    return new ResponseVO(GoodResponseEnum.GOOD_LIFE_ERROR);
                }
            }
        }

        goodPO.setStartAt(goodDTO.getStartAt()); //设置开始时间
        goodPO.setEndAt(goodDTO.getEndAt());  //设置结束时间
        //获取原来图像url
        String oldSrc = selectGoodPo.getImgSrc();
        if (StringUtil.isNotBlank(goodDTO.getImageUrls()) && !goodDTO.getImageUrls().equals(oldSrc)) {
            if (StringUtil.isNotBlank(oldSrc)) {
                String[] oldUrls = oldSrc.split(",");
                for (String oldUrl : oldUrls) {
                    if (!FastDFSClient.deleteFile(oldUrl)) {
                        return new ResponseVO(GoodResponseEnum.GOOD_IMAGE_DELETE);
                    }
                }
            }
            goodPO.setImgSrc(goodDTO.getImageUrls());
        }
        Map<String, Long> map = new HashMap<>(16);
        int i = goodDao.updateGood(goodPO);
        if (i == 1) {
            GoodRebatePO goodRebatePO = new GoodRebatePO();
            goodRebatePO.setGoodId(goodPO.getId());
            goodRebatePO.setStatus(GoodRebateConstant.PROCESSING);
            GoodRebatePO goodRebatePOOld = goodRebateMapper.selectOne(goodRebatePO);
            if (goodRebatePOOld != null) {
                goodRebatePOOld.setStatus(GoodRebateConstant.END);
                goodRebateMapper.updateByPrimaryKeySelective(goodRebatePOOld);
            }
            //如果商品设置分红
            if (goodDTO.getRate() != null) {
                goodRebatePO.setRate(goodDTO.getRate());
                goodRebatePO.setCreatedAt(TimeUtil.timestamp());
                goodRebateMapper.insertSelective(goodRebatePO);
            }
            map.put(ID, goodPO.getId());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
        } else {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
    }

    /**
     * 下架商品
     *
     * @param id  商品标识
     * @param uid 用户标识
     * @return
     */
    @Override
    public ResponseVO dropGood(Long id, Long uid) {
        GoodPO goodPO = goodDao.getGood(id);
        if (goodPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ID);
        }
        /**验证用户是否操作权限 */
        Boolean isPlatformAdmin = userDao.isPlatformAdmin(uid);
        if (!isPlatformAdmin) {
            Long rootId = goodDao.getGoodUid(id);
            if (uid == null || !uid.equals(rootId)) {
                return new ResponseVO(UserResponseEnum.USER_NOT_ROOT);
            }
        }


        if (GoodStatusConstant.DELETE == goodPO.getStatus()) {
            return new ResponseVO(GoodResponseEnum.GOOD_HAVING_DELETE);
        }
        GoodPO newGood = new GoodPO();
        newGood.setId(id);
        newGood.setStatus(GoodStatusConstant.DROP);
        int i = goodDao.updateGood(newGood);
        Map<String, Long> data = new HashMap<>(4);
        if (i == 1) {
            data.put(ID, newGood.getId());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, data);
        } else {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
    }

    /**
     * 获取指定商品信息
     *
     * @param id 商品标识
     * @return
     */
    @Override
    public ResponseVO getGoodInfo(Long id) {
        if (id == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ID);
        }
        GoodPO goodPO = goodDao.getGoodInfo(id);
        GoodVO goodVO = new GoodVO();
        BeanUtils.copyProperties(goodPO, goodVO);
        if (goodPO != null) {
            GoodRebatePO goodRebatePO = new GoodRebatePO();
            goodRebatePO.setGoodId(goodPO.getId());
            goodRebatePO.setStatus(GoodRebateConstant.PROCESSING);
            goodRebatePO = goodRebateMapper.selectOne(goodRebatePO);
            if (goodRebatePO!= null ) {
                goodVO.setRate(goodRebatePO.getRate());
            }
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, goodVO);
        }
        return new ResponseVO(GoodResponseEnum.GOOD_NOT_MATCH);


    }

    /**
     * 根据查询获取商品信息
     *
     * @param queryGoodDTO
     * @return
     */
    @Override
    public ResponseVO queryGood(QueryGoodDTO queryGoodDTO) {
        if (queryGoodDTO.getCurrentPage() == null || queryGoodDTO.getPageSize() == null) {
            List<GoodVO> goods = goodDao.queryGood(queryGoodDTO);
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, goods);
        } else {
            PageHelper.startPage(queryGoodDTO.getCurrentPage(), queryGoodDTO.getPageSize());
            List<GoodVO> goods = goodDao.queryGood(queryGoodDTO);
            PageInfo<GoodVO> pageInfo = new PageInfo<>(goods);
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageInfo);
        }
    }

    /**
     * 集团查询商品（本集团）
     *
     * @param uid          uid
     * @param queryGoodDTO 查询条件
     * @return
     */
    @Override
    public ResponseVO companyQueryGood(Long uid, QueryGoodDTO queryGoodDTO) {
        // 获取用户管理集团ID
        Long gid = companyInfoDao.queryUserManagerCompanyId(uid);
        if (gid == null) {
            return new ResponseVO(UserResponseEnum.USER_NOT_ROOT);
        }
        queryGoodDTO.setGid(gid);
        if (queryGoodDTO.getCurrentPage() == null || queryGoodDTO.getPageSize() == null) {
            List<GoodVO> goods = goodDao.queryGood(queryGoodDTO);
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, goods);
        } else {
            PageHelper.startPage(queryGoodDTO.getCurrentPage(), queryGoodDTO.getPageSize());
            List<GoodVO> goods = goodDao.queryGood(queryGoodDTO);
            PageInfo<GoodVO> pageInfo = new PageInfo<>(goods);
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageInfo);
        }
    }

    /**
     * 上架商品
     *
     * @param id  商品ID
     * @param uid 操作员id
     * @return
     */

    @Override

    public ResponseVO shelfGood(Long id, Long uid) {
        GoodPO goodPO = goodDao.getGood(id);
        if (goodPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ID);
        }
        CompanyInfoPO companyInfoPO = new CompanyInfoPO();
        companyInfoPO.setUid(goodPO.getGid());
        companyInfoPO = companyInfoMapper.selectOne(companyInfoPO);
        if (companyInfoPO.getCompanyStatus() != null && companyInfoPO.getCompanyStatus().equals(CompanyStatus.COMPANY_SHOT_DOWN)) {
            return new ResponseVO(UserResponseEnum.COMPANY_HAS_BEEN_DELETED);
        }
        /**验证用户是否操作权限 */
        Boolean isPlatformAdmin = userDao.isPlatformAdmin(uid);
        if (!isPlatformAdmin) {
            Long rootId = goodDao.getGoodUid(id);
            if (uid == null || !uid.equals(rootId)) {
                return new ResponseVO(UserResponseEnum.USER_NOT_ROOT);
            }
        }


        if (GoodStatusConstant.DELETE == goodPO.getStatus()) {
            return new ResponseVO(GoodResponseEnum.GOOD_HAVING_DELETE);
        }
        //判断商品有效期
        if (goodPO.getIsLife()) {
            if (goodPO.getEndAt() != null && goodPO.getEndAt() > 0L && goodPO.getEndAt() < System.currentTimeMillis()) {
                return new ResponseVO(GoodResponseEnum.GOOD_LIFE_TIME_ERROR);
            }
        }


        /** 设置商品正常销售*/
        GoodPO newGood = new GoodPO();
        newGood.setId(goodPO.getId());
        newGood.setStatus(GoodStatusConstant.SALE);
        int i = goodDao.updateGood(newGood);
        Map<String, Long> data = new HashMap<>(4);
        if (i == 1) {
            data.put(ID, newGood.getId());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, data);
        } else {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
    }

    /**
     * 删除商品
     *
     * @param id  商品id
     * @param uid 操作员id
     * @return
     */
    @Override
    public ResponseVO deleteGood(Long id, Long uid) {
        GoodPO goodPO = goodDao.getGood(id);
        if (goodPO == null) {
            return new ResponseVO(GoodResponseEnum.GOOD_ID);
        }

        /**验证用户是否操作权限 */
        Boolean isPlatformAdmin = userDao.isPlatformAdmin(uid);
        if (!isPlatformAdmin) {
            Long rootId = goodDao.getGoodUid(id);
            if (uid == null || !uid.equals(rootId)) {
                return new ResponseVO(UserResponseEnum.USER_NOT_ROOT);
            }
        }
        /** 删除商品*/
        GoodPO newGood = new GoodPO();
        newGood.setId(goodPO.getId());
        newGood.setStatus(GoodStatusConstant.DELETE);
        int i = goodDao.updateGood(newGood);
        Map<String, Long> data = new HashMap<>(4);
        if (i == 1) {
            data.put(ID, newGood.getId());
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, data);
        } else {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }
    }

    /**
     * 过期商品
     */
    @Override
    public void autoOutTImeGood() {
        // 查询正在销售的有有效期的商品
        List<GoodPO> goods = goodDao.getLivingGood();
        for (GoodPO good : goods) {
            if (good.getEndAt() <= 0L) {
                continue;
            }
            if (good.getEndAt() < System.currentTimeMillis()) {
                goodDao.setOutTime(good.getId());
            }
        }

    }

    /**
     * 获取商品状态列表
     */
    @Override
    public ResponseVO ListGoodStatus() {
        Map<String, Integer> status = new HashMap<>();
        status.put("上架", 1);
        status.put("下架", 0);
        status.put("删除", 3);
        status.put("售完", 2);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, status);
    }

    /**
     * 查询商品兑换信息
     */
    @Override
    public ResponseVO queryGoodExchanges(Long uid, GoodExchangeDTO goodExchangeDTO) {
        //获取用户信息
        UserPO userPO = userDao.getUserById(uid);
        //普通用户无权访问
        if (Roles.MEMBER.equals(userPO.getRole())) {
            return new ResponseVO(UserResponseEnum.USER_NOT_ROOT);
        }
        //集团管理用户管理用户
        if (Roles.COMPANY.equals(userPO.getRole())) {
            //获取用户管理的集团信息
            Long companyId = companyInfoDao.queryUserManagerCompanyId(uid);
            goodExchangeDTO.setCompanyId(companyId);
        }
        if (goodExchangeDTO.getCurrentPage() != null && goodExchangeDTO.getPageSize() != null) {
            // 开启分页
            PageHelper.startPage(goodExchangeDTO.getCurrentPage(), goodExchangeDTO.getPageSize());
            List<GoodExchangeVO> datas = goodDao.queryGoodExchange(goodExchangeDTO);
            PageInfo<GoodExchangeVO> pageInfo = new PageInfo<>(datas);
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageInfo);

        } else {
            // 没有开启分页
            List<GoodExchangeVO> datas = goodDao.queryGoodExchange(goodExchangeDTO);
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, datas);
        }
    }


    /**
     * app 首页展示
     *
     * @param size 展示数量
     * @return
     */
    @Override
    public ResponseVO appshow(Integer size, Long pid) {
        List<GoodVO> goods = goodDao.appshow(size, pid);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, goods);
    }

    /**
     * app 分类预览
     *
     * @param pid 平台id
     * @return
     */
    @Override
    public ResponseVO previewTypeGood(Long pid) {
        //获取商品分类
        List<TypeGoodVO> typeGoods = goodDao.previewTypeGood();
        List<TypeGoodVO> reutnGoods = new ArrayList<>();
        for (TypeGoodVO typeGood : typeGoods) {
            // 获取分类预览信息
            List<GoodVO> goodVOS = goodDao.previewGood(typeGood.getId(), pid);
            typeGood.setGoods(goodVOS);
            if (CollectionUtils.isNotEmpty(typeGood.getGoods())) {
                reutnGoods.add(typeGood);
            }
        }

        return new ResponseVO<>(CommonResponseEnum.SUCCESS, reutnGoods);
    }

    /**
     * app 预览商家品牌
     *
     * @param pid 平台id
     * @return
     */
    @Override
    public ResponseVO previewCompany(Long pid) {
        List<CompanyInfoPO> companys = companyInfoDao.previewCompany(pid);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, companys);
    }

    @Override
    @Cacheable(value = "goods" , key = "'company_goods_info_'+#companyId")
    public ResponseVO getCompanyGoodsInfo(Long companyId) {
        CompanyGoodsInfoDTO companyGoodsInfoDTO = goodDao.companyGoodsInfo(companyId);
        return new ResponseVO(CommonResponseEnum.QUERY_SUCCESS,companyGoodsInfoDTO);
    }
}
