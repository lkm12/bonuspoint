package com.fuzamei.bonuspoint.dao.user;

import com.fuzamei.bonuspoint.entity.dto.user.UserAddressDTO;
import com.fuzamei.bonuspoint.entity.po.user.UserAddressPO;
import com.fuzamei.bonuspoint.entity.vo.user.AddressDetailVo;
import com.fuzamei.bonuspoint.sql.user.UserAddressSqlFactory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-20 15:48
 **/
@Mapper
@Repository
public interface UserAddressDao {

    @Select("select * from bp_user_address where uid = #{uid} and is_delete = 0 order by created_at  ")
    List<UserAddressPO> getUserAddressList(Long  uid);

    @InsertProvider(type = UserAddressSqlFactory.class, method = "saveUserAddress")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    int saveUserAddress(UserAddressDTO userAddressDTO);
/*

    @UpdateProvider(type = UserAddressSqlFactory.class, method = "updateUserAddress")
    int updateUserAddress(UserAddressDTO userAddressDTO);

    @Delete("delete from bp_user_address where id = #{addressId} and uid = #{uid}")
    int deleteUserAddress(@Param("addressId") Long addressId ,@Param("uid") Long uid);
*/

    /**
     * 设置地址是否失效
     * @param id 地址id
     * @return
     */
    @Update("update bp_user_address set is_delete = 1 where id = #{id}")
    int setIsDelete(Long id);

    /**
     * 判断收货地址是否存在(刘蒙)
     *
     * @param id 地址标识
     * @return
     */
    @Select("select count(id) from bp_user_address where id = #{id} and uid= #{uid}")
    boolean isExit(@Param("id") Long id, @Param("uid") Long uid);

    /**
     * 获取用户的默认收货地址
     * @param uid uid
     * @return
     */
    @Select("SELECT default_address  FROM bp_user  WHERE  id = #{uid}")
    Long getDafultAddressId(Long uid);

    /**
     * 获取用户地址信息
     * @param uid 用户收货地址
     * @param addressId 地址信息
     * @return
     */
    @Select("SELECT address.id AS address_id,address.receiver AS address_name ,address.mobile AS address_mobile,details.whole_name AS address_district,address.area_detail AS address_detail\n" +
            "FROM bp_user_address AS  address\n" +
            "LEFT JOIN cn_district AS details ON address.district_code = details.`code`\n" +
            "WHERE address.id = #{addressId}\n" +
            "AND address.uid  = #{uid}")
    AddressDetailVo getAdressDetail(@Param("uid") Long uid, @Param("addressId") Long addressId);

    @Select("select * from bp_user_address where id = #{id}")
    UserAddressPO getAddressById(Long id);

}
