package com.fuzamei.bonuspoint.sql.user;

import com.fuzamei.bonuspoint.entity.dto.block.BlockInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import lombok.extern.java.Log;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-04-20 10:29
 **/
@Log
public class UserSqlFactory {

    public String updateUserSql(UserDTO userDTO) {

        SQL sql = new SQL() {
            {
                UPDATE("bp_user");
                if (userDTO.getRole() != null) {
                    SET("role=" + userDTO.getRole());
                }
                if (userDTO.getPId() != null) {
                    SET("p_id=" + userDTO.getPId());
                }
                if (userDTO.getUsername() != null) {
                    SET("username='" + userDTO.getUsername() + "'");
                }
                if ((userDTO.getPasswordHash() != null)) {
                    SET("password_hash='" + userDTO.getPasswordHash() + "'");
                }
                if (userDTO.getPaywordHash() != null) {
                    SET("payword_hash='" + userDTO.getPaywordHash() + "'");
                }
                if (userDTO.getCountry() != null) {
                    SET("country='" + userDTO.getCountry() + "'");
                }
                if (userDTO.getEmail() != null) {
                    SET("email='" + userDTO.getEmail() + "'");
                }
                if (userDTO.getNickname() != null) {
                    SET("nickname='" + userDTO.getNickname() + "'");
                }
                if (userDTO.getHeadimgurl() != null) {
                    SET("headimgurl='" + userDTO.getHeadimgurl()+"'");
                }
                if (userDTO.getQrCode() != null) {
                    SET("qr_code='" + userDTO.getQrCode() + "'");
                }
                if (userDTO.getDefaultAddress() != null) {
                    SET("default_address=" + userDTO.getDefaultAddress());
                }
                if (userDTO.getStatus() != null) {
                    SET("status=" + userDTO.getStatus());
                }
                if (userDTO.getIsInitialize() != null) {
                    SET("is_initialize=" + userDTO.getIsInitialize());
                }
                if(userDTO.getPublickey() != null){
                    SET("public_key='" + userDTO.getPublickey()+"'");
                }
                if(userDTO.getPrivatekey() != null){
                    SET("private_key='" + userDTO.getPrivatekey()+"'");
                }

                if (userDTO.getCreatedAt() != null) {
                    SET("created_at=" + userDTO.getCreatedAt());
                }
                if (userDTO.getUpdatedAt() != null) {
                    SET("updated_at=" + userDTO.getUpdatedAt());
                }
                if (userDTO.getPaywordAt() != null) {
                    SET("payword_at=" + userDTO.getPaywordAt());
                }
                WHERE("id=" + userDTO.getId());

            }
        };
        log.info("\nsql:\n" + sql.toString());
        return sql.toString();
    }

    public String getUserSql(List<String> fields, UserDTO userDTO) {
        SQL sql = new SQL() {
            {
                for (String field : fields) {
                    SELECT("" + field);
                }
                FROM("bp_user");
                WHERE("id=" + userDTO.getId());
                if (userDTO.getRole() != null) {
                    WHERE("role =" + userDTO.getRole());
                }
                if (userDTO.getPaywordHash() != null) {
                    WHERE("payword_hash='" + userDTO.getPaywordHash() + "'");
                }
                if (userDTO.getPasswordHash() != null) {
                    WHERE("password_hash='" + userDTO.getPasswordHash() + "'");
                }
                if (userDTO.getPId() != null) {
                    WHERE("p_id=" + userDTO.getPId());
                }
                if (userDTO.getUsername() != null) {
                    WHERE("username='" + userDTO.getUsername() + "'");
                }

            }
        };
        log.info("\nsql-------------------------\n" + sql.toString());
        return sql.toString();
    }


    public String countUserSql(UserDTO userDTO) {
        SQL sql = new SQL() {
            {
                SELECT("count(id)");
                FROM("bp_user");
                if (userDTO.getPId() != null) {
                    WHERE("p_id =" + userDTO.getPId());
                }
                if (userDTO.getRole() != null) {
                    WHERE("role =" + userDTO.getRole());
                }
                if (userDTO.getMobile() != null) {
                    WHERE("mobile='" + userDTO.getMobile() + "'");
                }
                if (userDTO.getLoginAt() != null) {
                    WHERE("login_at >" + userDTO.getLoginAt());
                }
                if (userDTO.getCreatedAt() != null) {
                    WHERE("created_at >" + userDTO.getCreatedAt());
                }
                if (userDTO.getUsername() != null) {
                    WHERE("username='" + userDTO.getUsername() + "'");
                }
            }
        };
        log.info("\nsql--------------------\n" + sql.toString());
        return sql.toString();
    }
    public String addBlockInfo(BlockInfoDTO blockInfoDTO) {
        SQL sql = new SQL() {
            {
                INSERT_INTO("bp_block_info");
                if (blockInfoDTO.getUid() != null) {
                    VALUES("uid", "#{uid}");
                }
                if (blockInfoDTO.getOperationType() != null) {
                    VALUES("operation_type", "#{operationType}");
                }
                if (blockInfoDTO.getHeight() != null) {
                    VALUES("height", "#{height}");
                }
                if (blockInfoDTO.getHash() != null) {
                    VALUES("hash", "#{hash}");
                }
                if (blockInfoDTO.getCreatedAt() != null) {
                    VALUES("created_at", "#{createdAt}");
                }
            }
        };
        log.info("\nsql---------------------\n" + sql.toString());
        return sql.toString();
    }

}
