package com.fuzamei.bonuspoint.service.impl.user;


import com.fuzamei.bonuspoint.dao.user.ContactDao;
import com.fuzamei.bonuspoint.entity.PageAPP;
import com.fuzamei.bonuspoint.entity.dto.user.ContactCreateDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.vo.PageAPPVO;
import com.fuzamei.bonuspoint.entity.vo.user.ContactInfoVO;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.user.ContactService;
import com.fuzamei.common.model.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lkm
 * @create 2018年4月20日
 */
@Service
@RefreshScope
@Transactional(rollbackFor = Exception.class)
public class ContactServiceImpl implements ContactService {

    @Value("${page.pageSize}")
    private Integer pageSize;

    private final ContactDao contactDao;

    @Autowired
    public ContactServiceImpl(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Override
    public ResponseVO createContact(ContactCreateDTO contactCreateDTO1) {

        String publickey = contactCreateDTO1.getPublickey();

        //通过uid与publickey查询联系人表以确认不会重复添加
        Long opUid = contactDao.queryContactByPublickey(contactCreateDTO1);

        if (opUid != null) {
            return new ResponseVO(UserResponseEnum.CONTACT_EXIST_FAIL);
        }

        //通过公钥查找联系人的id
        opUid = contactDao.findOpIdByPublickey(publickey);
        if (opUid == null) {
            return new ResponseVO(UserResponseEnum.CONTACT_NOT_EXIST_FAIL);
        }

        Long currentTime = System.currentTimeMillis();

        contactCreateDTO1.setCreatedAt(currentTime);
        contactCreateDTO1.setUpdatedAt(currentTime);
        contactCreateDTO1.setOpUid(opUid);

        //添加联系人
        contactDao.saveContact(contactCreateDTO1);

        Map<String, Object> map = new LinkedHashMap<>(4);
        map.put("uid", String.valueOf(opUid));
        return new ResponseVO<>(UserResponseEnum.CONTACT_SAVE_SUCCESS, map);
    }

    @Override
    public ResponseVO updateContact(ContactCreateDTO contactCreateDTO1) {


        //通过uid与publickey查询联系人表
        Long opUid = contactDao.queryContactByPublickey(contactCreateDTO1);

        if (opUid == null) {
            return new ResponseVO(UserResponseEnum.CONTACT_NOT_EXIST_FAIL);
        }

        Long currentTime = System.currentTimeMillis();

        contactCreateDTO1.setUpdatedAt(currentTime);

        //根据用户id修改联系人
        contactDao.updateContact(contactCreateDTO1);
        Map<String, Object> map = new HashMap<>(16);
        map.put("uid", String.valueOf(opUid));
        return new ResponseVO<>(UserResponseEnum.CONTACT_UPDATE_SUCCESS, map);
    }

    @Override
    public ResponseVO deleteContact(Long jsonData) {

        UserPO userPO = contactDao.findContactByContactId(jsonData);
        if (userPO == null) {
            return new ResponseVO(UserResponseEnum.CONTACT_NOT_EXIST_FAIL);
        }

        contactDao.deleteContact(jsonData);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("uid", String.valueOf(userPO.getUid()));
        return new ResponseVO<>(UserResponseEnum.CONTACT_DELETE_SUCCESS, map);
    }

    @Override
    public ResponseVO findContactsList(PagePointDTO pagePointDTO) {
        PageAPP pageAPP = new PageAPP();

        if (pagePointDTO.getPage() == null || pagePointDTO.getPage() < 1) {
            pagePointDTO.setPage(1);
        }
        if (pagePointDTO.getPageSize() == null) {
            pagePointDTO.setPageSize(pageSize);
        }

        pageAPP.setPage(String.valueOf(pagePointDTO.getPage()));
        pageAPP.setSize(String.valueOf(pagePointDTO.getPageSize()));
        pageAPP.initPage();

        PageAPPVO pageAPPVO = new PageAPPVO(pageAPP.getPageSize(),pageAPP.getPageNO());
        pageAPPVO.setPageSize(String.valueOf(pageAPP.getPageSize()));
        pageAPPVO.setPageNO(String.valueOf(pageAPP.getPageNO()));

        pagePointDTO.setBegin(pageAPP.getBegin());
        List<ContactInfoVO> list = contactDao.findContactsList(pagePointDTO);
        Integer num = contactDao.findContactsListCount(pagePointDTO);
        pageAPPVO.initPageVOByTotalSize(num);

        pageAPPVO.setData(list);
        return new ResponseVO<>(UserResponseEnum.CONTACT_FIND_SUCCESS, pageAPPVO);
    }

}
