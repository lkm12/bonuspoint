package com.fuzamei.bonuspoint.dao.user;

import com.fuzamei.bonuspoint.entity.dto.user.ContactCreateDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.vo.user.ContactInfoVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lkm
 * @create 2018年4月20日
 */
@Mapper
@Repository
public interface ContactDao {

    @Select("SELECT id FROM bp_user WHERE public_key = #{publickey}")
    Long findOpIdByPublickey(String publickey);

    @Insert("INSERT INTO bp_contacts (uid,op_uid,mobile,publickey,remark,created_at,"
            + "updated_at) VALUES (#{uid},#{opUid},#{mobile},#{publickey},#{remark},"
            + "#{createdAt},#{updatedAt})")
    int saveContact(ContactCreateDTO contactCreateDTO1);

    @Select("SELECT op_uid FROM bp_contacts WHERE publickey = #{publickey} AND uid = #{uid}")
    Long queryContactByPublickey(ContactCreateDTO contactCreateDTO1);

    @Update("UPDATE bp_contacts SET mobile = #{mobile},remark = #{remark},updated_at = "
            + "#{updatedAt} WHERE uid = #{uid} AND publickey = #{publickey}")
    Integer updateContact(ContactCreateDTO contactCreateDTO1);

    @Select("SELECT * FROM bp_contacts WHERE id = #{parseInt}")
    UserPO findContactByContactId(Long parseInt);

    @Delete("DELETE FROM bp_contacts WHERE id = #{jsonData}")
    void deleteContact(Long jsonData);

    @Select("<script>" + "select bp_contacts.id,bp_contacts.mobile,publickey,"
            + " bp_contacts.remark,bp_contacts.created_at AS created_time,username FROM "
            + " bp_contacts LEFT JOIN bp_user ON bp_contacts.op_uid = bp_user.id " + " <where> "
            + " <if test = 'username != null '> username = #{username} </if> and uid = #{id}  </where> " +
            "  LIMIT #{begin},#{pageSize} " +
            "</script>")
    List<ContactInfoVO> findContactsList(PagePointDTO pagePointDTO);
    @Select("<script> select count(*) FROM " +
            "  bp_contacts LEFT JOIN bp_user ON bp_contacts.op_uid = bp_user.id  <where> " +
            "  <if test = 'username != null '> username = #{username} </if> and uid = #{id}  </where> " +
            "  LIMIT #{begin},#{pageSize} " +
            "</script>")
    Integer findContactsListCount(PagePointDTO pagePointDTO);

	/*@Select("<script>" + "select count(*) FROM "
			+ " bp_contacts LEFT JOIN bp_user ON bp_contacts.op_uid = bp_user.id "+" <where> "
			+ " <if test = 'username != null '> username = #{username} </if> AND bp_contacts.uid = "
			+ "#{id} " + " </where> </script>")
	Integer countContactsList(Map<String, Object> mapToService);*/

}
