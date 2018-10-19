package com.fuzamei.bonuspoint.dao.user;

import com.fuzamei.bonuspoint.entity.dto.block.BlockInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.user.InvitePO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPrivatePo;
import com.fuzamei.bonuspoint.entity.vo.user.APP.InviteAPPVO;
import com.fuzamei.bonuspoint.entity.vo.user.APP.UserInfoAPPVO;
import com.fuzamei.bonuspoint.sql.user.UserSqlFactory;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {

    @Select("select mobile from bp_user where id=#{uid}")
    String getMobileById(Long uid);

    @Select("select email from bp_user where id=#{uid}")
    String getEmaileById(Long uid);

    @UpdateProvider(type = UserSqlFactory.class, method = "updateUserSql")
    Integer updateUser(UserDTO userDTO);


    @SelectProvider(type = UserSqlFactory.class, method = "getUserSql")
    UserPO getUser(List<String> fields, UserDTO userDTO);


    @Select("select username , public_key , headimgurl from bp_user  where id= #{id} ")
    UserDTO getUserInfoById(Long id);

    /**
     * 通过role、pId、注册时间、上次登录时间等动态统计活跃度，累计注册等
     *
     * @param userDTO
     * @return
     * @author wangjie
     */
    @SelectProvider(type = UserSqlFactory.class, method = "countUserSql")
    int countUser(UserDTO userDTO);

    /**
     * @author lkm
     */
    @Select("SELECT bp_user.id AS uid ,role,username,mobile,bp_user.created_at," +
            " (CASE WHEN payword_hash IS NOT NULL THEN '1' ELSE '0' END) AS payword ,is_initialize,public_key, " +
            " (CASE WHEN role = 2 THEN bp_company_info.company_name ELSE bp_platform_info.platform_name END) name," +
            " (CASE WHEN role = 2 THEN bp_company_info.company_address ELSE bp_platform_info.platform_address END) address FROM bp_user " +
            " LEFT JOIN bp_company_info ON bp_company_info.uid = bp_user.id " +
            " LEFT JOIN bp_platform_info ON bp_platform_info.uid = bp_user.id " +
            " WHERE bp_user.id = #{id} and bp_user.status = 1")
    UserPO findUserInfoById(Long id);


    @Select("SELECT * FROM bp_relation WHERE invite_code = #{inviteCode}")
    InviteAPPVO findChainAndUidByInviteCode(String inviteCode);


    @Select("SELECT bp_user.id AS uid,public_key,CASE WHEN role=1 THEN bp_platform_info.platform_name WHEN role=2 THEN " +
            "             bp_company_info.company_name ELSE bp_user.nickname  END AS NAME FROM " +
            "             bp_user LEFT JOIN bp_platform_info ON bp_user.id = bp_platform_info.uid LEFT JOIN " +
            "             bp_company_info ON bp_user.id = bp_company_info.uid  " +
            "             WHERE bp_user.id = #{id} and bp_user.status = 1")
    UserPO findQrcodeById(Long id);


    /**
     * 根据用户标识获取用户信息（刘蒙蒙）
     *
     * @param id 用户标识
     * @return
     */
    @Select("SELECT * FROM bp_user WHERE id = #{id}")
    UserPO getUserById(Long id);

    @InsertProvider(type = UserSqlFactory.class, method = "addBlockInfo")
    Integer addBlockInfo(BlockInfoDTO blockInfoDTO);

    @Update("UPDATE bp_company_info SET amount = #{amount} WHERE id = #{groupId}")
    Integer updateCompanyAmountByCompanyId(ExchangePointDTO exchangeDTO);


    @Select("<script>" +
            "SELECT bp_user.id,nickname,username,bp_user.mobile,email,bp_user_address.area_detail, " +
            "cn_district.area_name districtName , cn_city.area_name cityName , cn_province.area_name provinceName , cn_city.zip_code  " +
            "             FROM bp_user " +
            "             LEFT JOIN bp_user_address ON bp_user_address.id = bp_user.default_address " +
            "             LEFT JOIN cn_district ON cn_district.code = bp_user_address.district_code " +
            "             left JOIN cn_city ON cn_city.code = cn_district.city_code  " +
            "             left JOIN cn_province ON cn_province.code = cn_district.province_code  " +
            "<where> " +
            "p_id = #{id} and bp_user.status = 1 and role = 4 " +
            "<if test = 'fuzzyMatch != null'>" +
            "AND (nickname LIKE CONCAT('%',#{fuzzyMatch},'%') OR bp_user.mobile LIKE CONCAT('%',#{fuzzyMatch},'%')) " +
            "</if>" +
            "</where> " +
            "</script>")
    List<UserPO> findUserInfoFromPlatform(PagePointDTO pagePointDTO);

    @Select("<script>" +
            " SELECT bp_user.id,nickname,username,bp_user.mobile,email,bp_user_address.area_detail ,cn_district.area_name districtName ," +
            " cn_city.area_name cityName , cn_province.area_name provinceName , cn_city.zip_code FROM " +
            " bp_user " +
            " LEFT JOIN bp_user_address ON bp_user.default_address = bp_user_address.id " +
            " LEFT JOIN cn_district ON cn_district.code = bp_user_address.district_code " +
            " left JOIN cn_city ON cn_city.code = cn_district.city_code " +
            " left JOIN cn_province ON cn_province.code = cn_district.province_code " +
            "<where> " +
            "<if test = 'platformUid != null'>" +
            " p_id = #{platformUid} and " +
            "</if>" +
            "<if test = 'fuzzyMatch != null'>" +
            " (nickname LIKE CONCAT('%',#{fuzzyMatch},'%') OR bp_user.mobile LIKE CONCAT('%',#{fuzzyMatch},'%')) and " +
            "</if>" +
            " bp_user.status = 1 and bp_user.role = 4" +
            "</where> " +
            "</script>")
    List<UserPO> findUserInfoFromBigPlatform(PagePointDTO pagePointDTO);

    /**
     * 获取用户私密信息
     *
     * @param id 用户id
     * @return
     */
    @Select("select id, public_key,private_key from bp_user where id = #{id}")
    UserPrivatePo getUserPrivateInfo(Long id);


    /**
     * 查询平台下的邀请码信息
     *
     * @return
     */
    @Select("SELECT bp_user.id,mobile,public_key,(SELECT COUNT(*) FROM bp_relation WHERE bp_relation.p_id = a.uid " +
            " ) AS recommendNum,platform_point FROM bp_user inner JOIN (SELECT * FROM bp_relation WHERE p_id = 0) a ON " +
            "bp_user.id = a.uid AND bp_user.p_id = #{id}")
    List<InvitePO> findInviteList(PagePointDTO pagePointDTO);



    @Select("SELECT bp_user.id AS uid ,role,username,mobile,bp_user.created_at," +
            " (case when payword_hash IS NOT NULL then '1' else '0' end) as payword ,is_initialize,public_key,bp_relation.invite_code " +
            "  FROM bp_user LEFT JOIN bp_relation ON bp_relation.uid = " +
            " bp_user.id  " +
            "  WHERE bp_user.id = #{id}")
    UserInfoAPPVO findUserInfoByIdAPP(Long id);

    /**
     * 判断用户是否是集团所属平台的管理人员
     * @param uid uid
     * @return
     */
    @Select("SELECT COUNT(*) FROM bp_user WHERE id = #{uid} AND role = 1 ")
    Boolean isPlatformAdmin(Long uid);
    @Insert("insert into bp_user (ids) 1")
    void insert(Long id);

    @Select("SELECT uid as platformUid,platform_name FROM bp_platform_info WHERE uid != #{id}")
    List<PagePointDTO> selectPlatformUids(PagePointDTO pagePointDTO);

    @Select("SELECT COUNT(*) FROM bp_relation WHERE p_id = #{pid}")
    Integer selectInviteNum(Long pid);
}
