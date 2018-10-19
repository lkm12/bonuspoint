package com.fuzamei.bonuspoint.service.impl.location;

import com.fuzamei.bonuspoint.dao.location.LocationDao;
import com.fuzamei.bonuspoint.entity.po.location.CityPO;
import com.fuzamei.bonuspoint.entity.po.location.DistrictPO;
import com.fuzamei.bonuspoint.entity.po.location.ProvincePO;
import com.fuzamei.bonuspoint.service.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description: 省市县街道四级地理信息service
 * @author: WangJie
 * @create: 2018-04-20 18:42
 **/
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationDao locationDao;

    @Autowired
    public LocationServiceImpl(LocationDao locationDao) {
        this.locationDao = locationDao;
    }


    @Override
    public boolean isRealStreet(Long streetCode) {

        return locationDao.isRealStreet(streetCode);
    }

    @Override
    public boolean isRealDistrict(Long districtCode) {
        return locationDao.isRealDistrict(districtCode);
    }

    @Override
    public List<ProvincePO> getProvinceList() {
        return locationDao.getProvinceList();
    }

    @Override
    public List<CityPO> getCityListByProvinceCode(Long provinceCode) {
        return locationDao.getCityListByProvinceId(provinceCode);
    }

    @Override
    public List getDistrictListByCityCode(Long cityCode) {
        return locationDao.getDistrictListByCityId(cityCode);
    }

    @Override
    public List getStreetListByDistrictCode(Long districtCode) {
        return locationDao.getStreetListByDistrictCode(districtCode);
    }

    @Override
    public List<CityPO> getAllCities() {
        return locationDao.getAllCities();
    }

    @Override
    public List<DistrictPO> getAllDistricts() {
        return locationDao.getAllDistricts();
    }
}
