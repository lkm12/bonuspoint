package com.fuzamei.bonuspoint.entity.dto.user;

import com.fuzamei.bonuspoint.validation.group.User;
import com.fuzamei.common.model.dto.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
*@author lkm
*@create 2018年4月20日
*
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class PagePointDTO extends PageDTO {
	/**用户的上级uid*/
	private Long pId;
	/**用户id*/
	private Long id;
	/** 用户名*/
	@NotBlank(message = "USERNAME_BLANK",groups = {User.class})
	private String username;
	/** 集团id*/
	private Long groupId;
	/** 查询条数*/
	private Integer searchNum;
	/** 平台id*/
	private Long platformId;
	/**手机*/
	private String mobile;
	/**集团名称*/
	private String companyName;

	private Integer begin;

	private Long platformUid;

	private String platformName;


}
