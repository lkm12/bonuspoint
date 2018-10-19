package com.fuzamei.bonuspoint.dao.good;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.fuzamei.bonuspoint.entity.po.good.GoodSubTypePO;
import com.fuzamei.bonuspoint.sql.good.GoodSqlFactory;
import org.springframework.stereotype.Repository;

/**
 * 商品子类持久操作
 * @author liumeng
 * @create 2018年4月24日
 */
@Mapper
@Repository
public interface GoodSubTypeDao {
    /**
     * 保存商品分类
     * @param goodSubTypePO 商品分类信息
     * @return
     */
    @Insert("insert into bp_good_subtype(pid,name,img,create_at,update_at) values(#{pid},#{name},#{img},#{createAt},#{updateAt})")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    int saveSubType(GoodSubTypePO goodSubTypePO);

    /**
     * 更新商品子分类信息
     * @param goodSubTypePO 商品子分类信息
     * @return
     */
    @UpdateProvider(type = GoodSqlFactory.class, method = "updateSubType")
    int updateSubType(GoodSubTypePO goodSubTypePO);

    /**
     * 根据子分类标识获取子分类信息
     * @param id 子分类标识
     * @return 子分类信息
     */
    @Select("SELECT id , pid , name ,img ,create_at ,update_at from bp_good_subtype where id = #{id}")
    GoodSubTypePO getSubTypeById(Long id);

    /**
     * 根据父分类标识获取对应子分类
     * @param pid 父分类标识
     * @return
     */
    @Select("SELECT id , pid , name ,img ,create_at ,update_at  from bp_good_subtype where pid = #{pid}")
    List<GoodSubTypePO> getSubTypeByPid(Long pid);

    /**
     * 根据子分类id删除子分类
     * @param id
     * @return
     */
    @Delete("delete from bp_good_subtype where id = #{id}")
    int deleteSubType(Long id);
    /**
     * 商品子分类是否使用
     * @param id 子分类id
     * @return
     */
    @Select("SELECT COUNT(*) > 0  FROM bp_good WHERE sid = #{id}")
    boolean isSubtypeUsed(Long id);



}
