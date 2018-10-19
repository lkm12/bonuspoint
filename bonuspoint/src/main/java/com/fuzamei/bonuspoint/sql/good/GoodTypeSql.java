package com.fuzamei.bonuspoint.sql.good;

import org.apache.ibatis.jdbc.SQL;

import com.fuzamei.bonuspoint.entity.po.good.GoodSubTypePO;
import com.fuzamei.bonuspoint.entity.po.good.GoodTypePO;
import com.fuzamei.bonuspoint.util.StringUtil;

/**
 * 商品分类和子分类Sql
 * @author liumeng
 * @create 2018年4月24日
 */
public class GoodTypeSql {
    private GoodTypeSql() {
        throw new AssertionError("不能实例化 GoodTypeSql");
    }

    /**
     * 更新新子分类信息SQL
     * @param goodSubTypePO 子分类信息
     * @return sql
     */
    public static String updateSubType(GoodSubTypePO goodSubTypePO) {
        return new SQL() {
            {
                UPDATE("bp_good_subtype");
                if (goodSubTypePO.getPid() != null) {
                    SET("pid" + " = " + goodSubTypePO.getPid());
                }
                if (!StringUtil.isBlank(goodSubTypePO.getName())) {
                    SET("name" + " = '" + goodSubTypePO.getName() + "'");
                }
                //更新分类图像输入空格为删除
                if (goodSubTypePO.getImg() != null) {
                    if ("".equals(goodSubTypePO.getImg())) {
                        SET("img" + " = NULL ");
                    } else {
                        SET("img" + " = '" + goodSubTypePO.getImg() + "'");
                    }
                }

                if (goodSubTypePO.getCreateAt() != null) {
                    SET("create_at" + " = " + goodSubTypePO.getCreateAt().toString());
                }
                if (goodSubTypePO.getUpdateAt() != null) {
                    SET("update_at" + " = " + goodSubTypePO.getUpdateAt().toString());
                }
                WHERE("id" + " = " + goodSubTypePO.getId());
            }
        }.toString();
    }
    /**
     * 更新商品分类SQL
     * @param goodTypePO 分类信息
     * @return sql
     */
    public static String updateGoodType(GoodTypePO goodTypePO) {
        return new SQL() {
            {
                UPDATE("bp_good_type");
                if (!StringUtil.isBlank(goodTypePO.getName())) {
                    SET("name" + " = '" + goodTypePO.getName() + "'");
                }
                //更新时输入空字符串表示删除图像
                if (goodTypePO.getImg() != null){
                    if ("".equals(goodTypePO.getImg())){
                        SET("img" + " = NULL " );
                    }else{
                        SET("img" + " = '" + goodTypePO.getImg() + "'");
                    }
                }

                WHERE("id" + " = " + goodTypePO.getId());
            }
        }.toString();
    }
}
