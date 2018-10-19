package com.fuzamei.bonuspoint.dao;

import com.fuzamei.bonuspoint.dao.common.dao.GeneralPointRelationDao;
import com.fuzamei.bonuspoint.dao.common.mapper.GoodMapper;
import com.fuzamei.bonuspoint.dao.data.version.AppVersionMapper;
import com.fuzamei.bonuspoint.dao.good.GoodDao;
import com.fuzamei.bonuspoint.entity.po.good.GoodPO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author qbanxiaoli
 * @description
 * @create 2018/6/28 18:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Daotest {

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private GeneralPointRelationDao generalPointRelationDao;

    @Autowired
    private AppVersionMapper appVersionMapper;
//    @Test
//    public void Test() {
//        generalPointRelationDao.updateGeneralPoint(250L, new BigDecimal(10));
//    }
//    @Test
//    public void testAppVersion(){
//        log.info(appVersionMapper.getNewestAndroidVersion().toString());
//    }

    @Test
    public void Test() {
        List<GoodPO> goodPOList = goodMapper.selectAll();
        for (GoodPO goodPO : goodPOList) {
            Pattern p = Pattern.compile("test/M00");
            String imgSrc = p.matcher(goodPO.getImgSrc()).replaceAll("group1/M00");
            log.info("图片路径：" + imgSrc);
            String goodDetails = p.matcher(goodPO.getDetails()).replaceAll("group1/M00");
            log.info("商品详情：" + goodDetails);
            goodPO.setImgSrc(imgSrc);
            goodPO.setDetails(goodDetails);
            goodMapper.updateByPrimaryKeySelective(goodPO);
        }
    }
}
