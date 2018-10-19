package com.fuzamei.bonuspoint.service.impl.data.version;

import com.fuzamei.bonuspoint.dao.data.version.AppVersionMapper;
import com.fuzamei.bonuspoint.entity.dto.data.version.AppVersioinDTO;
import com.fuzamei.bonuspoint.entity.po.data.version.AppVersioinPO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.service.data.version.AppVersionService;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-08-02 15:09
 **/
@Service
public class AppVersionServiceImpl implements AppVersionService {
    private final AppVersionMapper appVersionMapper;
    @Autowired
    public AppVersionServiceImpl(AppVersionMapper appVersionMapper) {
        this.appVersionMapper = appVersionMapper;
    }

    @Override
    public ResponseVO addAppVersion(AppVersioinDTO appVersioinDTO) {
        AppVersioinPO appVersioinPO = new AppVersioinPO();
        BeanUtils.copyProperties(appVersioinDTO,appVersioinPO);
        appVersioinPO.setCreatedAt(System.currentTimeMillis());
        int result = appVersionMapper.insertSelective(appVersioinPO);
        if (result==1) {
            return new ResponseVO(CommonResponseEnum.ADD_SUCCESS);
        }
        return new ResponseVO(CommonResponseEnum.ADD_FAIL);
    }

    @Override
    public ResponseVO getNewestAppVersionForAndroid() {
        AppVersioinPO appVersioinPO = appVersionMapper.getNewestAndroidVersion();
        if (appVersioinPO == null){
            return new ResponseVO(CommonResponseEnum.QUERY_FAIL);
        }
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,appVersioinPO);
    }

    @Override
    public ResponseVO getNewestAppVersionForIOS() {
        AppVersioinPO appVersioinPO = appVersionMapper.getNewestIOSVersion();
        if (appVersioinPO==null){
            return new ResponseVO(CommonResponseEnum.QUERY_FAIL);
        }
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,appVersioinPO);
    }

    @Override
    public ResponseVO listAppVersion(PageDTO pageDTO) {

        Example example = new Example(AppVersioinPO.class);
        Example.Criteria criteria = example.createCriteria();
        if (pageDTO.getFuzzyMatch()!=null) {
            criteria.andEqualTo("system", pageDTO.getFuzzyMatch());
        }
        example.setOrderByClause("created_at desc");
        Page page = PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize());
        List<AppVersioinPO> appVersionPOList =appVersionMapper.selectByExample(example);
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(appVersionPOList, pageDTO.getPage(), pageDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,pageBean);
    }
}
