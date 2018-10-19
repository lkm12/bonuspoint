package com.fuzamei.bonuspoint.sql.account;


import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

/**
 * @program: bonus-point-cloud
 * @description: 用户安全模块SQL
 * @author: WangJie
 * @create: 2018-06-25 17:20
 **/
@Slf4j
public class AccountSqlFactory {

    public String updateUserSql(AccountPO accountPO) {

        SQL sql = new SQL() {
            {
                UPDATE("bp_user");
                if (accountPO.getRole() != null) {
                    SET("role=" + accountPO.getRole());
                }
                if (accountPO.getPId() != null) {
                    SET("p_id=" + accountPO.getPId());
                }
                if (accountPO.getUsername() != null) {
                    SET("username='" + accountPO.getUsername() + "'");
                }
                if ((accountPO.getPasswordHash() != null)) {
                    SET("password_hash='" + accountPO.getPasswordHash() + "'");
                }
                if (accountPO.getPaywordHash() != null) {
                    SET("payword_hash='" + accountPO.getPaywordHash() + "'");
                }
                if (accountPO.getCountry() != null) {
                    SET("country='" + accountPO.getCountry() + "'");
                }
                if (accountPO.getMobile() != null) {
                    SET("mobile='" + accountPO.getMobile() + "'");
                }
                if (accountPO.getEmail() != null) {
                    SET("email='" + accountPO.getEmail() + "'");
                }
                if (accountPO.getNickname() != null) {
                    SET("nickname='" + accountPO.getNickname() + "'");
                }
                if (accountPO.getHeadimgurl() != null) {
                    SET("headimgurl='" + accountPO.getHeadimgurl()+"'");
                }
                if (accountPO.getQrCode() != null) {
                    SET("qr_code='" + accountPO.getQrCode() + "'");
                }
                if (accountPO.getDefaultAddress() != null) {
                    SET("default_address=" + accountPO.getDefaultAddress());
                }
                if (accountPO.getStatus() != null) {
                    SET("status=" + accountPO.getStatus());
                }
                if (accountPO.getIsInitialize() != null) {
                    SET("is_initialize=" + accountPO.getIsInitialize());
                }
                if(accountPO.getPublicKey() != null){
                    SET("public_key='" + accountPO.getPublicKey()+"'");
                }
                if(accountPO.getPrivateKey() != null){
                    SET("private_key='" + accountPO.getPrivateKey()+"'");
                }
                if (accountPO.getCreatedAt() != null) {
                    SET("created_at=" + accountPO.getCreatedAt());
                }
                if (accountPO.getUpdatedAt() != null) {
                    SET("updated_at=" + accountPO.getUpdatedAt());
                }
                if (accountPO.getPaywordAt() != null) {
                    SET("payword_at=" + accountPO.getPaywordAt());
                }
                WHERE("id=" + accountPO.getId());
            }
        };
        log.info("\nsql:\n" + sql.toString());
        return sql.toString();
    }

    public String countUserSql(AccountPO accountPO){
        SQL sql = new SQL() {
            {
                SELECT("count(*)");
                FROM("bp_user");
                if (accountPO.getPId() != null) {
                    WHERE("p_id =" + accountPO.getPId());
                }
                if (accountPO.getRole() != null) {
                    WHERE("role =" + accountPO.getRole());
                }
                if (accountPO.getMobile() != null) {
                    WHERE("mobile='" + accountPO.getMobile() + "'");
                }
                if (accountPO.getLoginAt() != null) {
                    WHERE("login_at >" + accountPO.getLoginAt());
                }
                if (accountPO.getCreatedAt() != null) {
                    WHERE("created_at >" + accountPO.getCreatedAt());
                }
                if (accountPO.getUsername() != null) {
                    WHERE("username='" + accountPO.getUsername() + "'");
                }
                if (accountPO.getPublicKey()!=null){
                    WHERE("public_key = '"+accountPO.getPublicKey()+"'");
                }
            }
        };
        log.info("\nsql--------------------\n" + sql.toString());
        return sql.toString();
    }

    public String addAccountSql(AccountPO accountPO){
        SQL sql = new SQL(){
            {
                INSERT_INTO("bp_user");
                VALUES("p_id", "#{pId}");
                VALUES("role", "#{role}");
                VALUES("status", "#{status}");
                //VALUES("username", "#{username}");
                VALUES("username", "'"+accountPO.getUsername()+"'");
                VALUES("mobile","#{mobile}");
                VALUES("email","#{email}");
                VALUES("password_hash", "#{passwordHash}");
                VALUES("payword_hash", "#{paywordHash}");
                VALUES("is_initialize", "#{isInitialize}");
                VALUES("created_at", "#{createdAt}");
                VALUES("public_key", "#{publicKey}");
                VALUES("private_key", "#{privateKey}");
            }
        };
        log.info("\nsql--------------------\n" + sql.toString());
        return sql.toString();
    }
}
