package com.fuzamei.bonuspoint.service.location;


import java.util.List;

public interface LocationService {

    boolean isRealStreet(Long streetCode);

    boolean isRealDistrict(Long districtCode);

    List getProvinceList();

    List getCityListByProvinceCode(Long provinceCode);

    List getDistrictListByCityCode(Long cityCode);

    List getStreetListByDistrictCode(Long districtCode);


    List getAllCities();

    List getAllDistricts();
}
