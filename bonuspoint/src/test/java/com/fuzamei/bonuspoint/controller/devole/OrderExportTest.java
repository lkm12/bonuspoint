package com.fuzamei.bonuspoint.controller.devole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lmm
 * @description
 * @create 2018/9/12 14:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderExportTest {
    private DataSource dataSource;

    @Before
    public void setup() throws SQLException {
        UnpooledDataSource dataSource = new UnpooledDataSource();
        dataSource.setUrl("jdbc:mysql://47.88.230.136/db_pointmail");
        dataSource.setUsername("root");
        dataSource.setPassword("cga@009");
        dataSource.setDriver("com.mysql.jdbc.Driver");
        dataSource.setAutoCommit(true);
        this.dataSource = dataSource;
    }
    @Test
    public void export() throws  Exception{
        String sql = "SELECT orders.* ,company.id as companyId, uuser.username,uuser.mobile,company.company_name,good.`name` AS good_name FROM ld_good_orders AS orders\n" +
                "LEFT JOIN ld_good AS good ON good.id = orders.good_id\n" +
                "LEFT JOIN ld_user AS uuser ON uuser.id =orders.uid\n" +
                "LEFT JOIN ld_company_info AS company ON company.uid = orders.gid\n";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<OrderExport> goodExports = jdbcTemplate.query(sql,new OrderExportRowMapper());
        //创建excel
        Workbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("订单");
        Row row = sheet.createRow(0);
        //set head
        row.createCell(0).setCellValue("订单标识");
        row.createCell(1).setCellValue("用户标识");
        row.createCell(2).setCellValue("用户名称");
        row.createCell(3).setCellValue("用户手机号");
        row.createCell(4).setCellValue("集团标识");
        row.createCell(5).setCellValue("集团名称");
        row.createCell(6).setCellValue("商品标识");
        row.createCell(7).setCellValue("商品名称");
        row.createCell(8).setCellValue("购买数量");
        row.createCell(9).setCellValue("订单状态");
        row.createCell(10).setCellValue("支付方式");
        row.createCell(11).setCellValue("支付单价");
        row.createCell(12).setCellValue("支付金额");
        row.createCell(13).setCellValue("配送方式");
        row.createCell(14).setCellValue("买家留言");
        row.createCell(15).setCellValue("收件信息");
        row.createCell(16).setCellValue("物流订单号");
        row.createCell(17).setCellValue("退货物流订单号");
        row.createCell(18).setCellValue("退货理由");
        row.createCell(19).setCellValue("支付时间");
        row.createCell(20).setCellValue("成交时间");
        for (int i = 0; i < goodExports.size(); i++) {
            OrderExport orderExport = goodExports.get(i);
            row = sheet.createRow(i+1);
            //set head
            row.createCell(0).setCellValue(orderExport.getId());
            row.createCell(1).setCellValue(orderExport.getUid());
            row.createCell(2).setCellValue(orderExport.getUsername());
            row.createCell(3).setCellValue(orderExport.getMobile());
            row.createCell(4).setCellValue(orderExport.getGid());
            row.createCell(5).setCellValue(orderExport.getCompanyName());
            row.createCell(6).setCellValue(orderExport.getGoodId());
            row.createCell(7).setCellValue(orderExport.getGoodName());
            row.createCell(8).setCellValue(orderExport.getNum());
            row.createCell(9).setCellValue(orderExport.getStatus());
            row.createCell(10).setCellValue(orderExport.getPayModel());
            if (orderExport.getPrice() != null){
                row.createCell(11).setCellValue(orderExport.getPrice().doubleValue());
            }
            if (orderExport.getTotal() != null){
                row.createCell(12).setCellValue(orderExport.getTotal().doubleValue());
            }
            row.createCell(13).setCellValue(orderExport.getDistribution());
            row.createCell(14).setCellValue(orderExport.getMessage());
            row.createCell(15).setCellValue(orderExport.getAddressInfo());
            row.createCell(16).setCellValue(orderExport.getLogisticsInfo());
            row.createCell(17).setCellValue(orderExport.getBackLogisticsInfo());
            row.createCell(18).setCellValue(orderExport.getBack_memo());
            row.createCell(19).setCellValue(orderExport.getPayedAt());
            row.createCell(20).setCellValue(orderExport.getBargainAt());

        }

        try  (OutputStream fileOut = new FileOutputStream("订单导出.xls")) {
            wb.write(fileOut);
        }

        log.info(">>>>>>>>>>>>>>>>>>\n" + goodExports.toString());

    }

    private class  OrderExportRowMapper implements RowMapper<OrderExport> {

        @Override
        public OrderExport mapRow(ResultSet resultSet, int i) throws SQLException {
            OrderExport orderExport = new OrderExport();
            orderExport.setId(resultSet.getLong("id"));
            orderExport.setUid(resultSet.getLong("uid"));
            orderExport.setUsername(resultSet.getString("username"));
            orderExport.setMobile(resultSet.getString("mobile"));
            orderExport.setGid(resultSet.getLong("companyId"));
            orderExport.setCompanyName(resultSet.getString("company_name"));
            orderExport.setGoodId(resultSet.getLong("good_id"));
            orderExport.setGoodName(resultSet.getString("good_name"));
            Object payModel = resultSet.getObject("pay_mode");
            if (payModel != null ){
                orderExport.setPayModel(payModel.toString().equals("1") ? "会员积分" : "通用积分");
            }
            orderExport.setPrice(resultSet.getBigDecimal("price"));
            orderExport.setNum(resultSet.getLong("num"));
            orderExport.setTotal(resultSet.getBigDecimal("total"));
            orderExport.setDistribution(resultSet.getString("distribution"));
            orderExport.setMessage(resultSet.getString("message"));
            orderExport.setAddressInfo(resultSet.getString("address_info"));
            int status =resultSet.getInt("status");
            String statusName = null;
            switch (status){
                case 0 :
                    statusName = "待结算";
                    break;
                case 1 :
                    statusName = "待发货";
                    break;
                case 2 :
                    statusName = "运输中";
                    break;
                case 3 :
                    statusName = "待确认收货";
                    break;
                case 4 :
                    statusName = "成功交易";
                    break;
                case 5 :
                    statusName = "退货中待集团确认";
                    break;
                case 6 :
                    statusName = "退货成功";
                    break;
                case 99 :
                    statusName = "买家关闭交易";
                    break;
                case 100 :
                    statusName = "超时失效";
                    break;
                case 101 :
                    statusName = "隐藏订单";
                    break;
                case 102 :
                    statusName = "取消订单";
                    break;

            }
            orderExport.setStatus(statusName);
            orderExport.setLogisticsInfo(resultSet.getString("logistics_info"));
            orderExport.setBackLogisticsInfo(resultSet.getString("back_logistics_info"));
            orderExport.setBack_memo(resultSet.getString("back_memo"));
            Object payedat = resultSet.getObject("payed_at");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (payedat != null){
                orderExport.setPayedAt(sdf.format(new Date(((Long) payedat) * 1000)));
            }
            Object bargainAt = resultSet.getObject("bargain_at");
            if (bargainAt != null){
                orderExport.setBargainAt(sdf.format(new Date(((Long) bargainAt) * 1000) ));
            }
            return orderExport;
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class OrderExport{
        private Long id;
        private Long uid;
        private String username;
        private String mobile;
        private Long gid;
        private String companyName;
        private Long goodId;
        private String goodName;
        private Long num ;
        private String status;
        private String payModel;
        private BigDecimal price;
        private BigDecimal total;
        private String distribution;
        private String message;
        private String addressInfo;
        private String logisticsInfo;
        private String backLogisticsInfo;
        private String back_memo;
        private String payedAt;
        private String bargainAt;
    }


}
