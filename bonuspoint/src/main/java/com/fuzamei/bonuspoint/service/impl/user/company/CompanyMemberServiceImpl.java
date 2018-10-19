package com.fuzamei.bonuspoint.service.impl.user.company;

import com.fuzamei.bonuspoint.dao.user.CompanyMemberDao;

import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.user.company.CompanyMemberService;
import com.fuzamei.common.model.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: bonus-point-cloud
 * @description: 集团的会员
 * @author: WangJie
 * @create: 2018-07-24 14:35
 **/
@Service
public class CompanyMemberServiceImpl implements CompanyMemberService {
    private final CompanyMemberDao companyMemberDao;

    @Autowired
    public CompanyMemberServiceImpl(CompanyMemberDao companyMemberDao) {
        this.companyMemberDao = companyMemberDao;
    }

    @Override
    public ResponseVO getMemberActivity(Long companyId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long timeMillis = calendar.getTimeInMillis();

        //日新增会员
        int newMemberToday = companyMemberDao.countMember(companyId,timeMillis);

        //统计日活量
        int activityMemberToday = companyMemberDao.countActivityMember(companyId,timeMillis);

        //统计月活量
        calendar.set(Calendar.DATE, 1);
        timeMillis = calendar.getTimeInMillis();
        int activityMemberOfTheMonth = companyMemberDao.countActivityMember(companyId,timeMillis);

        //通过 =role、=pId条件统计累计会员量
        int totalMember = companyMemberDao.countMember(companyId,0L);
        Map<String, Object> map = new HashMap<>(16);
        map.put("newMemberToday", newMemberToday);
        map.put("activityMemberToday", activityMemberToday);
        map.put("activityMemberOfTheMonth", activityMemberOfTheMonth);
        map.put("totalMember", totalMember);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, map);
    }
}
