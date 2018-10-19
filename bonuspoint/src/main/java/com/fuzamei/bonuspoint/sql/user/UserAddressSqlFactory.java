package com.fuzamei.bonuspoint.sql.user;

import com.fuzamei.bonuspoint.entity.dto.user.UserAddressDTO;
import lombok.extern.java.Log;
import org.apache.ibatis.jdbc.SQL;

/**
 * @program: bonus-point-cloud
 * @description: 用户收货地址的sql生成类
 * @author: WangJie
 * @create: 2018-04-20 15:49
 **/
@Log
public class UserAddressSqlFactory {

    public String getUserAddress(UserAddressDTO userAddressDTO){
        SQL sql = new SQL(){
            {

                SELECT("*");
                FROM("bp_user_address");
                WHERE("uid="+userAddressDTO.getUid());

            }
        };
        log.info("\nsql--------------------------\n"+sql.toString());
        return sql.toString();
    }

    public String saveUserAddress(UserAddressDTO userAddressDTO){
        SQL sql = new SQL(){
            {
                INSERT_INTO("bp_user_address");
                VALUES("uid","'"+userAddressDTO.getUid()+"'");
                VALUES("receiver","'"+userAddressDTO.getReceiver()+"'");
                VALUES("mobile",""+userAddressDTO.getMobile());
                VALUES("district_code",""+userAddressDTO.getDistrictCode());
                VALUES("area_detail","'"+userAddressDTO.getAreaDetail()+"'");
                VALUES("created_at","'"+userAddressDTO.getCreatedAt()+"'");
                if (userAddressDTO.getUpdateAt()!=null){
                    VALUES("updated_at","'"+userAddressDTO.getUpdateAt()+"'");

                }
            }
        };
        log.info("\nsql--------------------------\n"+sql.toString());
        return sql.toString();
    }

    public String updateUserAddress(UserAddressDTO userAddressDTO){
        SQL sql = new SQL(){
            {
                UPDATE("bp_user_address");
                if (userAddressDTO.getReceiver()!=null){
                    SET("receiver='"+userAddressDTO.getReceiver()+"'");
                }
                if (userAddressDTO.getMobile()!=null){
                    SET("mobile='"+userAddressDTO.getMobile()+"'");
                }

                if (userAddressDTO.getDistrictCode()!=null){
                    SET("district_code="+userAddressDTO.getDistrictCode());
                }
                if (userAddressDTO.getAreaDetail()!=null){
                    SET("area_detail='"+userAddressDTO.getAreaDetail()+"'");
                }
                SET("updated_at="+userAddressDTO.getUpdateAt());
                WHERE("id="+userAddressDTO.getId());
                WHERE("uid="+userAddressDTO.getUid());
            }
        };
        log.info("\nsql----------------------\n"+sql.toString());
        return sql.toString();
    }

    public String deleteUserAddress(UserAddressDTO userAddressDTO){
        SQL sql = new SQL(){
            {
                DELETE_FROM("bp_user_address");
                WHERE("id="+userAddressDTO.getId());
                WHERE("uid="+userAddressDTO.getUid());
            }
        };
        log.info("\nsql---------------------\n"+sql.toString());
        return sql.toString();
    }
}
