package com.fuzamei.bonuspoint.dao.account;

import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.account.SecrecyDTO;
import com.fuzamei.bonuspoint.entity.dto.user.KeyDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.sql.account.AccountSqlFactory;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-06-25 15:37
 **/
@Mapper
@Repository
public interface AccountDao extends TkMapper<AccountPO> {

    @InsertProvider(type= AccountSqlFactory.class , method = "addAccountSql")
    @Options(keyColumn = "id",keyProperty = "id",useGeneratedKeys = true)
    int addAccount(AccountPO accountPO);

    @UpdateProvider(type=AccountSqlFactory.class,method = "updateUserSql")
    int updateUser(AccountPO accountPO);

    @Update("update bp_user set password_hash = #{passwordHash} , updated_at = #{updatedAt}, is_initialize = #{isInitialize}  where id = #{id} ")
    int updatePassword(SecrecyDTO  secrecyDTO);

    @Update("update bp_user set payword_hash = #{paywordHash} , payword_at =#{paywordAt}  where id = #{id} ")
    int updatePayword(SecrecyDTO secrecyDTO);

    /**
     * 更新手机号
     * @param secrecyDTO
     * @return
     */
    @Update("update bp_user set mobile = #{mobile} , country = #{country} , updated_at = #{updatedAt} where id =#{id}")
    int updateMobile(SecrecyDTO secrecyDTO);

    /**
     * 修改邮箱
     * @param secrecyDTO
     * @return
     */
    @Update("update bp_user set email = #{email}  , updated_at = #{updatedAt} where id =#{id}")
    int updateEmail(SecrecyDTO secrecyDTO);

    @Select("select * from bp_user where id=#{id}")
    AccountPO getUserById(Long id);

    /**
     * 通过集团id获取集团所属用户的公私钥
     */
    @Select("select id , private_key , public_key from  bp_user where id = #{id}")
    KeyDTO getUserKeyById(Long id);

    /**
     * 通过userPO 的非空属性统计用户数 目前有 pid role mobile username loginAt createdat
     * 时间统计大于该时间的
     * @param accountPO
     * @return
     */
    @SelectProvider(type = AccountSqlFactory.class , method ="countUserSql" )
    int countUser(AccountPO accountPO);

    /**
     * 检查同一父级下用户输入的手机号是否已经被使用
     * @param pId 父级id
     * @param mobile 手机号
     * @return
     */
    @Select("select count(*) from bp_user where  mobile = #{mobile} and ( p_id = #{pId} or id = #{pId} )")
    int checkMobileAvailable(@Param("pId") Long pId, @Param("mobile") String mobile);




    /**
     * 通过公钥查用户
     * @param publicKey
     * @return
     */
    @Select("select * from bp_user where public_key=#{publicKey}")
    AccountPO getUserByPublicKey(String publicKey);

    /**
     * 更新头像
     * @param accountDTO
     */
    @Update("update bp_user set headimgurl = #{headimgurl} ,updated_at=#{updatedAt} where id = #{id}")
    int updateHeadimgurl(AccountDTO accountDTO);

    /**
     * 设置默认收货地址
     * @param uid
     * @param addressId
     * @return
     */
    @Update("update bp_user set default_address = #{addressId}  where id = #{uid}")
    int updateDefaultAddress(@Param("uid") Long uid,@Param("addressId") Long addressId);
}
