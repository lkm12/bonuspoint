package com.fuzamei.bonuspoint.dao.location;

import com.fuzamei.bonuspoint.entity.po.location.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description: 省市县三级dao
 * @author: WangJie
 * @create: 2018-04-20 19:00
 **/
@Mapper
@Repository
public interface LocationDao {
    /**
     * 通过省编码得到省份
     * @param provinceCode 省编码
     * @return
     */
    @Select("select code , area_name from cn_province where province_id=#{provinceCode}")
    ProvincePO getProvinceByProvinceId(Long provinceCode);

    /**
     * 获取所有省份
     * @return
     */
    @Select("select code , area_name from cn_province ")
    List<ProvincePO> getProvinceList();

    /**
     * 通过市编码得到市信息
     * @param cityCode 市编码
     * @return
     */
    @Select("select code , area_name , province_code from cn_city where code=#{cityCode}")
    CityPO getCityByCityId(Long cityCode);

    /**
     * 通过省编码获取下属的所有市
     * @param provinceCode 省编码
     * @return
     */
    @Select("select code , area_name , province_code from cn_city where province_code=#{provinceCode}")
    List<CityPO> getCityListByProvinceId(Long provinceCode);

    /**
     * 通过县编码获取县信息
     * @param districtCode 区县编码
     * @return
     */
    @Select("select code , area_name , city_code , zip_code from cn_district where code=#{districtCode}")
    DistrictPO getAreaByAreaId(Long districtCode);

    /**
     * 通过市编码获取所有下属县信息
     * @param cityCode 市编码
     * @return
     */
    @Select("select code , area_name , city_code , zip_code from cn_district where city_code=#{cityCode}")
    List<DistrictPO> getDistrictListByCityId(Long cityCode);


    /**
     * 通过街道编码获取街道信息
     * @param streetCode 街道编码
     * @return
     */
    @Select("select code , area_name , district_code from cn_street where code = #{streetCode}")
    StreetPO getStreetByStreetCode(Long streetCode);

    /**
     * 通过区县编码获取下属街道
     * @param districtCode
     * @return
     */
    @Select("select code , area_name , district_code from cn_street where district_code=#{districtCode} ")
    List<StreetPO> getStreetListByDistrictCode(Long districtCode);

    /**
     * 通过街道编码获得地址
     * @param streetCode 街道编码
     * @return
     */
    @Select("select cn_street.area_name streetName , cn_district.area_name districtName , cn_city.area_name cityName , cn_province.area_name provinceName , cn_city.zip_code " +
            " from cn_street" +
            " inner join cn_district on cn_district.code = cn_street.district_code " +
            " inner join cn_city on cn_city.code = cn_street.city_code " +
            " inner join cn_province on cn_province.code = cn_street.province_code " +
            " where cn_street.code = #{sterrtCode} ")
    LocationPO getLocationByStreetCode(Long streetCode);

    /**
     * 通过区县编码获得地址
     * @param districtCode 区县编码
     * @return
     */
    @Select("select  cn_district.area_name districtName , cn_city.area_name cityName , cn_province.area_name provinceName , cn_city.zip_code " +
            " from cn_district" +
            " inner join cn_city on cn_city.code = cn_district.city_code " +
            " inner join cn_province on cn_province.code = cn_district.province_code " +
            " where cn_district.code = #{districtCode} ")
    LocationPO getLocationByDistrictCode(Long districtCode);
    /**
     * 是否是真实存在的街道编码
     * @param streetCode
     * @return
     */
    @Select("select count(*) from cn_street where code = #{streetCode}")
    boolean isRealStreet(Long streetCode);

    /**
     * 是否是真实存在的区县编码
     * @param districtCode
     * @return
     */
    @Select("select count(*) from cn_district where code = #{districtCode}")
    boolean isRealDistrict(Long districtCode);

    /**
     * 获取所有市
     * @return
     */
    @Select("select code , area_name , province_code from cn_city ")
    List<CityPO> getAllCities();


    /**
     * 获取所有县信息
     * @return
     */
    @Select("select code , area_name , city_code , zip_code from cn_district ")
    List<DistrictPO> getAllDistricts();



}
