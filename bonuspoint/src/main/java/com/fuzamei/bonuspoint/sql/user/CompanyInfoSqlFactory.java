package com.fuzamei.bonuspoint.sql.user;

import com.fuzamei.bonuspoint.constant.OrderTypeConstant;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.user.CompanyInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import lombok.extern.java.Log;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description: 集团（商户）sql工厂
 * @author: WangJie
 * @create: 2018-04-24 22:12
 **/
@Log
public class CompanyInfoSqlFactory {

    /**
     * @author wangjie
     */
    public String getCompanyInfoSql(List<String> fields, CompanyInfoDTO companyInfoDTO){
        SQL sql = new SQL(){
            {
                for(String field : fields){
                    SELECT(""+field);
                }
                FROM("bp_company_info");
                INNER_JOIN("bp_user on bp_user.id=bp_company_info.uid ");
                WHERE("company_status != 0");
                WHERE("p_id="+companyInfoDTO.getPId());
                WHERE("role = "+Roles.COMPANY);
                if (companyInfoDTO.getFuzzyMatch()!=null) {
                    WHERE("  (company_leader_mobile like '%" + companyInfoDTO.getFuzzyMatch() + "%'" + " or company_leader like '%" + companyInfoDTO.getFuzzyMatch() + "%'" + " or company_name like '%" + companyInfoDTO.getFuzzyMatch() + "%')");
                }
                if (companyInfoDTO.getCompanyName()!=null){
                    WHERE("company_name like '%"+companyInfoDTO.getCompanyName()+"%'");
                }
                if (companyInfoDTO.getOrderType()!=null){
                    if (companyInfoDTO.getOrderType().equals(OrderTypeConstant.ASC)){
                        ORDER_BY(companyInfoDTO.getOrderBy() + " ASC");
                    }
                    if (companyInfoDTO.getOrderType().equals(OrderTypeConstant.DESC)){
                        ORDER_BY(companyInfoDTO.getOrderBy() + " DESC");
                    }
                }
            }
        };
        log.info("\nsql------------------\n"+sql.toString());
        return sql.toString();
    }
    /**
     * 添加集团信息
     * @author wangjie
     */
    public String addCompanyInfoSql(CompanyInfoPO companyInfoPO){
        SQL sql = new SQL(){
            {
                INSERT_INTO("bp_company_info");
                VALUES("uid","#{uid}");
                VALUES("company_name","#{companyName}");
                VALUES("company_address","#{companyAddress}");
                VALUES("company_leader","#{companyLeader}");
                VALUES("company_leader_mobile","#{companyLeaderMobile}");
                VALUES("company_telephone","#{companyTelephone}");
                VALUES("company_email","#{companyEmail}");
                VALUES("cash_rate","#{cashRate}");
                VALUES("point_rate","#{pointRate}");
            }
        };
        log.info("\nsql-----------------\n"+sql.toString());
        return sql.toString();
    }
    /**
     * 修改集团信息
     */
    public String updateCompanyInfoSql(CompanyInfoDTO companyInfoDTO){
        SQL sql = new SQL(){
            {
                UPDATE("bp_company_info");

                if (companyInfoDTO.getCompanyName() != null){
                    SET("company_name='"+companyInfoDTO.getCompanyName()+"'");
                }
                if (companyInfoDTO.getCompanyLeader() != null){
                    SET("company_leader='"+companyInfoDTO.getCompanyLeader()+"'");
                }
                if (companyInfoDTO.getCompanyLeaderMobile() != null){
                    SET("company_leader_mobile='"+companyInfoDTO.getCompanyLeaderMobile()+"'");
                }
                if (companyInfoDTO.getCompanyTelephone() != null){
                    SET("company_telephone='"+companyInfoDTO.getCompanyTelephone()+"'");
                }
                if (companyInfoDTO.getCompanyEmail() != null){
                    SET("company_email='"+companyInfoDTO.getCompanyEmail()+"'");
                }
                if (companyInfoDTO.getBank() != null){
                    SET("bank='"+companyInfoDTO.getBank()+"'");
                }
                if (companyInfoDTO.getBankBranch() != null){
                    SET("bank_branch='"+companyInfoDTO.getBankBranch()+"'");
                }
                if (companyInfoDTO.getBankAccount() != null){
                    SET("bank_account='"+companyInfoDTO.getBankAccount()+"'");
                }
                if (companyInfoDTO.getBankNum() != null){
                    SET("bank_num='"+companyInfoDTO.getBankNum()+"'");
                }
                if (companyInfoDTO.getAmount() != null){
                    SET("amount="+companyInfoDTO.getAmount());
                }
                if (companyInfoDTO.getCashNum() !=null){
                    SET("cash_num='"+companyInfoDTO.getCashNum());
                }
                if (companyInfoDTO.getCashRate() !=null){
                    SET("cash_rate="+companyInfoDTO.getCashRate());
                }
                if (companyInfoDTO.getPointRate() != null){
                    SET("point_rate ="+companyInfoDTO.getPointRate());
                }
                if (companyInfoDTO.getLogoUrl()!=null){
                    SET("logo_url ='"+companyInfoDTO.getLogoUrl()+"'");
                }
                if (companyInfoDTO.getCompanyLeaderIdCard()!=null){
                    SET("company_leader_id_card = '"+companyInfoDTO.getCompanyLeaderIdCard()+"'");
                }

                WHERE("bp_company_info.id="+companyInfoDTO.getId());
            }
        };
        log.info("\nsql-----------------------\n"+sql.toString());
        return sql.toString();
    }
}
