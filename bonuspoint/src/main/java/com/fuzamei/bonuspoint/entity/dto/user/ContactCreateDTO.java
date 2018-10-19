package com.fuzamei.bonuspoint.entity.dto.user;

import com.fuzamei.bonuspoint.validation.Phone;
import com.fuzamei.bonuspoint.validation.group.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
*@author lkm
*@create 2018年4月20日
*
*/
@Log
@Data
@NoArgsConstructor
public class ContactCreateDTO {
	/** 用户的id*/
	private Long uid;
	/** 联系人的手机*/
	@NotBlank(message = "MOBILE_BLANK",groups = {User.CreateContact.class})
	@Phone(message = "MOBILE_FORMAT_ERROR",groups = {User.CreateContact.class})
	private String mobile;
	/** 联系人的公钥,用它去查联系人的uid*/
	@NotBlank(message = "USER_PUBLICKEY_NULL",groups = {User.CreateContact.class})
	private String publickey;
	/** 备注*/
	@NotBlank(message = "REMARK_BLANK",groups = {User.CreateContact.class})
	private String remark;
	/** 对方uid*/
	private Long opUid;
	/** 创建时间*/
	private Long createdAt;
	/** 修改时间*/
	private Long updatedAt;
	/** 联系人id*/
	@NotNull(message = "PARAMETER_BLANK",groups = {User.DeleteContact.class})
	private Long contactsId;
}

