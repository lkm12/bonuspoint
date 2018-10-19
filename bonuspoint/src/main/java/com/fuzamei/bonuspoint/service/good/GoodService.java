package com.fuzamei.bonuspoint.service.good;

import com.fuzamei.bonuspoint.entity.dto.good.GoodDTO;
import com.fuzamei.bonuspoint.entity.dto.good.GoodExchangeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryGoodDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import org.springframework.cache.annotation.Cacheable;

/**
 * 商品Service
 * @author liumeng
 * @create 2018年4月18日
 */
public interface GoodService {
    /**
     * 添加商品
     * @param uid 用户id
     * @param goodDTO 商品信息
     * @return
     */
    ResponseVO addGood(Long uid, GoodDTO goodDTO);

    /**
     * 编辑商品
     * @param uid 用户id
     * @param goodDTO 商品信息
     * @return
     */
    ResponseVO updateGood(Long uid, GoodDTO goodDTO);

    /**
     * 下架商品
     * @param id 商品标识
     * @param uid 用户id
     * @return
     */
    ResponseVO dropGood(Long id, Long uid);

    /**
     * 上架商品
     * @param id 商品ID
     * @param uid   操作员id
     * @return
     */
    ResponseVO shelfGood(Long id, Long uid);

    /**
     * 删除商品
     * @param id 商品id
     * @param uid 操作员id
     * @return
     */
    ResponseVO deleteGood(Long id, Long uid);

    /**
     * 扫描过期商品
     */
    void  autoOutTImeGood();


    /**
     * 获取指定商品信息
     * @param id 商品标识
     * @return
     */
    ResponseVO getGoodInfo(Long id);

    /**
     * 根据添加获取商品信息
     * @param queryGoodDTO
     * @return
     */
    ResponseVO queryGood(QueryGoodDTO queryGoodDTO);

    /**
     * 集团管理员查询本集团商品
     * @param uid
     * @param queryGoodDTO
     * @return
     */
    ResponseVO companyQueryGood(Long uid ,QueryGoodDTO queryGoodDTO);

    /**
     * 获取商品状态标识
     * @return
     */
    ResponseVO ListGoodStatus();

    /**
     * 查询商品兑换信息
     * @param uid   uid
     * @param goodExchangeDTO  查询条件
     * @return
     */
    ResponseVO queryGoodExchanges(Long uid, GoodExchangeDTO goodExchangeDTO);

    /**
     * app 首页展示
     * @param size 展示数量
     * @param pid 平台id
     * @return
     */
    ResponseVO appshow(Integer size,Long pid);

    /**
     * app 分类预览
     * @param  pid 平台id
     * @return
     */
    ResponseVO previewTypeGood(Long pid);

    /**
     * app 预览商家平拍
     * @param  pid 平台id
     * @return
     */
    ResponseVO previewCompany(Long pid);


    /**
     * 通过店铺id获取店铺商品信息
     * @param companyId
     * @return
     */
    ResponseVO getCompanyGoodsInfo(Long companyId);
}
