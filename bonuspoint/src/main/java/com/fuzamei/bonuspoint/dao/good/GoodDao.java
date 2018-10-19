package com.fuzamei.bonuspoint.dao.good;

import java.util.List;

import com.fuzamei.bonuspoint.entity.dto.good.CompanyGoodsInfoDTO;
import com.fuzamei.bonuspoint.entity.vo.good.TypeGoodVO;
import com.fuzamei.bonuspoint.validation.Phone;
import org.apache.ibatis.annotations.*;

import com.fuzamei.bonuspoint.entity.dto.good.GoodExchangeDTO;
import com.fuzamei.bonuspoint.entity.dto.good.QueryGoodDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodPO;
import com.fuzamei.bonuspoint.entity.vo.good.GoodExchangeVO;
import com.fuzamei.bonuspoint.entity.vo.good.GoodVO;
import com.fuzamei.bonuspoint.sql.good.GoodSqlFactory;
import org.springframework.stereotype.Repository;

/**
 * 鍟嗗搧鎿嶄綔鎺ュ彛绫�
 * @author liumeng
 * @create 2018骞�4鏈�18鏃�
 */
@Mapper
@Repository
public interface GoodDao {

    /**
     * 娣诲姞鍟嗗搧淇℃伅
     * @param goodPO    鍟嗗搧淇℃伅
     * @return 璁板綍鏁�
     */
    @InsertProvider(type = GoodSqlFactory.class, method = "savaGood")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    int savaGood(GoodPO goodPO);

    /**
     * 娣诲姞鍟嗗搧淇℃伅
     * @param goodPO    鍟嗗搧淇℃伅
     * @return 璁板綍鏁�
     */
    @InsertProvider(type = GoodSqlFactory.class, method = "savaGoodWithOutKey")
    int savaGoodWithOutKey(GoodPO goodPO);
    /**
     * 鏇存柊鍟嗗搧淇℃伅
     * @param goodPO 鍟嗗搧淇℃伅
     * @return
     */
    @UpdateProvider(type = GoodSqlFactory.class, method = "updateGood")
    int updateGood(GoodPO goodPO);

    /**
     * 批量下架商品
     * @param goodIdList
     * @return
     */
    @Update("<script> " +
            "update bp_good set status = 0 where id in (" +
            "<foreach collection = 'list' item = 'id' separator=','> " +
            "#{id} " +
            "</foreach> ) </script>")
    int dropGoods(List<Long> goodIdList);
    /**
     * 鏍规嵁鍟嗗搧鑾峰彇瀵瑰簲闆嗗洟鐢ㄦ埛
     * @param id 闆嗗洟id
     * @return
     */
    @Select("select company.uid from bp_company_info as company ,bp_good as good where good.gid = company.id and good.id =#{id}")
    Long getGoodUid(Long id);

    /**
     * 鏍规嵁鍟嗗搧id 鑾峰彇鍟� 鍝�
     * @param id 鍟嗗搧id 
     * @return
     */
    @Select("select * from bp_good where id = #{id}")
    GoodPO getGood(Long id);

    /**
     * 鏍规嵁鍟嗗搧鏍囪瘑鑾峰彇鍟嗗搧淇℃伅
     * @param id 鍟嗗搧鏍囪瘑
     * @return
     */
    @SelectProvider(type = GoodSqlFactory.class, method = "getGoodInfo")
    GoodPO getGoodInfo(Long id);

    /**
     * 鏍规嵁鏌ヨ鏉′欢鑾峰彇鍟嗗搧淇℃伅
     * @param queryGoodDTO
     * @return
     */
    @SelectProvider(type = GoodSqlFactory.class, method = "queryGood")
    List<GoodVO> queryGood(QueryGoodDTO queryGoodDTO);

    /**
     * 璁㈣喘鍟嗗搧涓嬪崟
     * @param id 鍟嗗搧id
     * @param num  璁㈣喘鏁伴噺
     * @return
     */
    @Update("update bp_good set num_used = num_used + #{num} , updated_at = unix_timestamp(now()) * 1000  where id = #{id} ")
    int orderGood(@Param("id") Long id, @Param("num") Integer num);

    /**
     * 鍙栨秷鍟嗗搧涓嬪崟
     * @param id 鍟嗗搧id
     * @param num  璁㈣喘鏁伴噺
     * @return
     */
    @Update("update bp_good set num_used = num_used - #{num}  , updated_at = unix_timestamp(now()) * 1000  where id = #{id} ")
    int cancelOrderGood(@Param("id") Long id, @Param("num") Integer num);

    /**
     * 鍟嗗搧鍏戞崲璇︽儏鏌ヨ
     * @param goodExchangeDTO 鏌ヨ鏉′欢
     * @return 鏌ヨ缁撴灉
     */
    @SelectProvider(type = GoodSqlFactory.class, method = "queryGoodExchange")
    List<GoodExchangeVO> queryGoodExchange(GoodExchangeDTO goodExchangeDTO);

    /**
     * app 首页展示
     * @param size
     * @param pid 平台id
     * @return
     */
    @Select("SELECT good.* FROM bp_good AS good\n" +
            "LEFT JOIN bp_company_info AS company ON company.id = good.gid\n" +
            "LEFT JOIN bp_user AS uuser ON uuser.id = company.uid\n" +
            "WHERE good.`status` = 1 \n" +
            "AND uuser.p_id = #{pid}\n" +
            "ORDER BY created_at DESC LIMIT #{size} ")
    List<GoodVO> appshow(@Param("size") Integer size,@Param("pid") Long pid);

    /**
     * 通过商品分类预览商品
     * @param tid 分类id
     * @param  pid 平台id
     * @return
     */
    @SelectProvider(type = GoodSqlFactory.class, method = "previewGood")
    List<GoodVO> previewGood(  Long tid , Long pid);


    /**
     * 分类商品预览
     * @return
     */
    @Select("SELECT id , `name` ,img FROM bp_good_type")
    List<TypeGoodVO> previewTypeGood();

    /**
     * 查询需要自动过期的商品
     * @return
     */
    @Select("SELECT * FROM bp_good AS good WHERE good.is_life = 1 AND `status` =1")
    List<GoodPO> getLivingGood();

    /**
     * 设置商品过期（即商品下架）
     * @param id 商品id
     */
    @Update("UPDATE bp_good AS good SET good.`status` = 0 , good.updated_at =unix_timestamp(now()) * 1000  WHERE good.id = #{id}")
    void setOutTime (Long id);

    /**
     * 统计子分类下的商品
     * @param id
     * @return
     */
    @Select("select count(*) from bp_good where sid = #{id} ")
    int countGoodsBySubType(Long id);

    /**
     * 通过店铺id获取店铺商品信息
     * @param companyId
     */
    @Select("SELECT count(id) goodsTotalNum , sum(num_used) goodsTotalUsed FROM `bp_good` where gid=#{companyId}")
    CompanyGoodsInfoDTO companyGoodsInfo(Long companyId);
}
