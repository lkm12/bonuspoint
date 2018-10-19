package com.fuzamei.bonuspoint.dao.good;

import java.util.List;

import org.apache.ibatis.annotations.*;
import com.fuzamei.bonuspoint.entity.po.good.GoodTypePO;
import com.fuzamei.bonuspoint.entity.vo.good.GoodTypeVO;
import com.fuzamei.bonuspoint.sql.good.GoodTypeSql;
import org.springframework.stereotype.Repository;


/**
 * 商品父分类Dao
 * @author liumeng
 * @create 2018年4月17日
 */
@Mapper
@Repository
public interface GoodTypeDao {
    /**
     * 保存商品父分类信息
     * @param goodTypePO 商品分类信息
     * @return 记录数
     */
    @Insert("insert into bp_good_type (name,img) VALUES (#{name},#{img})")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    int saveGoodType(GoodTypePO goodTypePO);

    /**
     * 更新商品分类信息
     * @param goodTypePO 商品分类信息
     * @return
     */
    @UpdateProvider(type=GoodTypeSql.class,method="updateGoodType")
    int updateGoodType(GoodTypePO goodTypePO);

    /**
     * 分类是否被使用
     * @param id 分类id
     * @return
     */
    @Select("SELECT COUNT(*) > 0  FROM bp_good_subtype WHERE pid = #{id}")
    boolean typeIsUesd(Long id);

    @Delete("DELETE FROM bp_good_type WHERE id = #{id}")
    int deleteType(Long id);

    /**
     * 根据商品分类标识返回分类信息
     * @param id 商品分类标识
     * @return 分类信息
     */
    @Select("select id , name,img from bp_good_type  where id = #{id}")
    GoodTypePO getGoodType(Long id);

    /**
     * 查询所有商品分类信息
     * @return 分类信息集合
     */
    @Select("select  id , name,img from bp_good_type")
    List<GoodTypePO> listGoodType();
    /**
     * 获取所有商品分类和子分类信息
     * @return
     */
    @Select("select id , name,img from bp_good_type")
    @Results({
        @Result(id=true,column="id",property="id"),
        @Result(column="id",property="subTypes",javaType=List.class,
        many=@Many(select="com.fuzamei.bonuspoint.dao.good.GoodSubTypeDao.getSubTypeByPid"))
    })  
    List<GoodTypeVO> listAllTypes();

    /**
     * 删除分类
     * @param id
     * @return
     */
    @Delete("delete from bp_good_type where id = #{id}")
    int deleteGoodType(Long id);
}
