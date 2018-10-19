package com.fuzamei.bonuspoint.dao.data.version;

import com.fuzamei.bonuspoint.entity.po.data.version.AppVersioinPO;
import com.fuzamei.common.mapper.TkMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AppVersionMapper extends TkMapper<AppVersioinPO> {

    @Select("select * from bp_app_version where system = 'IOS' order by created_at desc limit 0 , 1")
    AppVersioinPO getNewestIOSVersion();

    @Select("select * from bp_app_version where system = 'Android' order by created_at desc limit 0 , 1")
    AppVersioinPO getNewestAndroidVersion();
}
