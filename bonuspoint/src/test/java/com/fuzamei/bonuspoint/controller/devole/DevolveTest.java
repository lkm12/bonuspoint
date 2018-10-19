package com.fuzamei.bonuspoint.controller.devole;

import com.fuzamei.bonuspoint.dao.good.GoodDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.good.GoodPO;
import com.fuzamei.bonuspoint.service.point.CompanyPointService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.bean.FastDFSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**s
 * @author lmm
 * @description 转移数据库记录
 * @create 2018/8/17 10:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DevolveTest {
    @Autowired
    private CompanyPointService companyPointService;

    private DataSource dataSource = null;

    private final String SOURSE_HTTP_URL = "http://www.wtccoin.cn/api/uploads/";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GoodDao goodDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Value("${fdfs.request.url}")
    private String fdfsRequestUrl;


    @Before
    public void setup() throws SQLException {
//        UnpooledDataSource dataSource = new UnpooledDataSource();
//        dataSource.setUrl("jdbc:mysql://47.88.230.136/db_pointmail");
//        dataSource.setUsername("root");
//        dataSource.setPassword("cga@009");
//        dataSource.setDriver("com.mysql.jdbc.Driver");
//        dataSource.setAutoCommit(true);
//        this.dataSource = dataSource;
    }

    /**
     * 集团申请积分转移
     */

    @Test
    public void applyPoint() {
        log.info("转移集团申请积分————————————————————————————》》》》》》》》》》");
        String sql = "SELECT company.id AS gid,IFNULL(SUM(chains.total_group_points - chains.used_group_points),0) AS sums FROM ld_company_info AS company\n" +
                "LEFT JOIN ld_blockbind AS block ON block.uid = company.uid\n" +
                "LEFT JOIN chain33_groups AS chains ON chains.addr = block.publickey\n" +
                "GROUP BY company.id";
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> result : results) {
            Long companyId = Long.valueOf(result.get("gid").toString());
            BigDecimal sums = new BigDecimal(result.get("sums").toString());
            log.info("{ company = " + companyId + " , sums = " + sums + " }");
            companyPointService.tranterReleasePoint(companyId, sums);
        }
    }

    /**
     * 转移商品信息
     */
    @Test
    public void goodDevolve() throws Exception {
        log.info("开始转储商品信息");
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql ="SELECT good.id, (SELECT company.id FROM ld_company_info AS company WHERE company.uid =good.gid ) AS gid , good.`name`, good.price, good.global_price, good.num, good.num_used, good.worth, ( SELECT GROUP_CONCAT(img.img) FROM ld_img AS img WHERE FIND_IN_SET(img.id, good.img_ids)) AS img_src,good.details , good.order_level, good.is_life, good.start_at * 1000 AS start_at, good.end_at * 1000 AS end_at, CASE good.`status` WHEN - 1 THEN 4 ELSE good.`status` END AS `status`, good.created_at * 1000 AS created_at, good.updated_at * 1000 AS updated_at FROM ld_good AS good  \n" ;
        List<GoodPO> goods = jdbcTemplate.query(sql, new GoodRowMapper());
        for (GoodPO good : goods) {
//            log.info("————————————————————————————————》\n" + good.toString());
//            good.setSid(1L);
//            transerImage(good);
//            resolveDetailImg(good);
//            resloveGoodLife(good);
//            goodDao.savaGoodWithOutKey(good);
            GoodPO updateGood = new GoodPO();
            updateGood.setId(good.getId());
//            resolveDetailImg(good);
//            log.info("old details:{}",good.getDetails());
            updateGood.setGid(good.getGid());
            goodDao.updateGood(updateGood);
//            log.info("update good : {}",updateGood.toString());
        }

    }



    /**
     * 转移用户头像
     */
    @Test
    public void userHeadImage() throws Exception {
        log.info("开始转移用户头像");
        String sql = "SELECT uuser.id AS uid, img.img FROM ld_user AS uuser LEFT JOIN ld_img AS img ON uuser.headimgurl = img.id";
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> result : results) {
            Long uid = Long.valueOf(result.get("uid").toString());
            String image = (String) result.get("img");
            if (image == null || StringUtil.isBlank(image)) {
                log.info("用户" + uid + "头像为空，continue...");
                continue;
            }
            log.info("转移用户头像 uid = " + uid);
            String path = SOURSE_HTTP_URL + image;
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String newUrl = fastDFSClient.uploadFile(IOUtils.toByteArray(connection.getInputStream()), image);
            if (newUrl == null) {
                throw new Exception("上传文件出错");
            }
            log.info("{ old = " + path + " , new = " + newUrl + "}");
            UserDTO userDTO = new UserDTO();
            userDTO.setId(uid);
            userDTO.setHeadimgurl(newUrl);
            userDao.updateUser(userDTO);
        }

    }

    /**
     * 解析商品时间
     * @param goodPO 商品信息
     */
    private  void  resloveGoodLife(GoodPO goodPO){
        if (goodPO.getStartAt() != null || goodPO.getEndAt() !=null){
            goodPO.setIsLife(true);
        }
        if (goodPO.getStartAt() < 0){
            goodPO.setStartAt(0L);
        }
        if (goodPO.getEndAt() < 0){
            goodPO.setEndAt(0L);
        }
    }
    /**
     * 转换图像
     */
    private void transerImage(GoodPO goodPO) {
        String allImage = goodPO.getImgSrc();
        if (allImage == null) {
            return;
        }
        log.info("old image: " + goodPO.getImgSrc());
        String[] imgs = allImage.split(",");
        StringBuffer newImages = new StringBuffer("");
        for (String img : imgs) {
            try {
                String path = SOURSE_HTTP_URL + img;
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String newUrl = fastDFSClient.uploadFile(IOUtils.toByteArray(connection.getInputStream()), img);
                if (newImages == null) {
                    throw new Exception();
                }
                if (!"".equals(newImages.toString())) {
                    newImages.append(",");
                }
                newImages.append(newUrl);
            } catch (Exception e) {
                continue;
            }
        }
        log.info("new image: " + newImages.toString());
        goodPO.setImgSrc(newImages.toString());
    }


    private void resolveDetailImg(GoodPO goodPO) {
        if (goodPO.getDetails() == null) {
            return;
        }
        log.info("old datails : " + goodPO.getDetails());
        String details = goodPO.getDetails();
        String pattern = "https://www.wtccoin.cn/api/uploads/\\S*(.jpg|.png)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(details);
        StringBuffer stringBuffer = new StringBuffer();
        while (m.find()) {
            try {
                String httpUrl = "http://" + m.group().substring(8);
                URL url = new URL(httpUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                String newUrl = fastDFSClient.uploadFile(IOUtils.toByteArray(connection.getInputStream()), m.group());
                m.appendReplacement(stringBuffer, fdfsRequestUrl + newUrl);
            } catch (Exception e) {
                continue;
            }
        }
        m.appendTail(stringBuffer);
        log.info("new details :" + stringBuffer.toString());
        goodPO.setDetails(stringBuffer.toString());
    }


    private  class GoodRowMapper implements RowMapper<GoodPO> {

        @Override
        public GoodPO mapRow(ResultSet resultSet, int i) throws SQLException {
            GoodPO goodPO = new GoodPO();
            goodPO.setId(resultSet.getLong("id"));
            goodPO.setGid(resultSet.getLong("gid"));
            goodPO.setName(resultSet.getString("name"));
            goodPO.setPrice(resultSet.getBigDecimal("price"));
            goodPO.setGlobalPrice(resultSet.getBigDecimal("global_price"));
            goodPO.setNum(resultSet.getInt("num"));
            goodPO.setNumUsed(resultSet.getInt("num_used"));
            goodPO.setWorth(resultSet.getBigDecimal("worth"));
            goodPO.setImgSrc(resultSet.getString("img_src"));
            goodPO.setDetails(resultSet.getString("details"));
            goodPO.setIsLife(resultSet.getBoolean("is_life"));
            goodPO.setStatus(resultSet.getInt("status"));
            goodPO.setOrderLevel(resultSet.getInt("order_level"));
            goodPO.setStartAt(resultSet.getLong("start_at"));
            goodPO.setEndAt(resultSet.getLong("end_at"));
            goodPO.setCreatedAt(resultSet.getLong("created_at"));
            goodPO.setUpdatedAt(resultSet.getLong("updated_at"));
            return goodPO;
        }
    }


}
