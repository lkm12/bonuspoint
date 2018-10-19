package com.fuzamei.bonuspoint.service.user;

import com.fuzamei.bonuspoint.entity.dto.user.ContactCreateDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.common.model.vo.ResponseVO;

/**
*@author lkm
*@create 2018年4月20日
*
*/
public interface ContactService {
	/**新增联系人*/
	ResponseVO createContact(ContactCreateDTO contactCreateDTO1);
	/**更新联系人*/
	ResponseVO updateContact(ContactCreateDTO contactCreateDTO1);
	/**删除联系人*/
	ResponseVO deleteContact(Long integer);
	/**获取联系人列表*/
	ResponseVO findContactsList(PagePointDTO pagePointDTO);

	

}
