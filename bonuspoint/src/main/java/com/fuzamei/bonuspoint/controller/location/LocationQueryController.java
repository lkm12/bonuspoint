package com.fuzamei.bonuspoint.controller.location;

import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.location.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: bonus-point-cloud
 * @description: 省市县三级地址查询
 * @author: WangJie
 * @create: 2018-04-28 16:20
 **/
@Slf4j
@RestController
@RequestMapping("/bonus-point/location")
public class LocationQueryController {

    private final LocationService locationService;

    @Autowired
    public LocationQueryController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * 获取全国所有省信息
     *
     * @return
     */
    @GetMapping("/list-province")
    public ResponseVO getProvinceList() {
        List provinceList = locationService.getProvinceList();
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, provinceList);
    }

    @GetMapping("/list-all-city")
    public ResponseVO getAllCityList(){
        List cityList = locationService.getAllCities();
        return new ResponseVO(CommonResponseEnum.QUERY_SUCCESS, cityList);
    }

    @GetMapping("/list-all-district")
    public ResponseVO getAllDistrictList(){
        List districtList = locationService.getAllDistricts();
        return new ResponseVO(CommonResponseEnum.QUERY_SUCCESS, districtList);
    }


    /**
     * 通过省份id获取该省所辖市信息
     *
     * @param provinceCode 省份id
     * @return
     */
    @GetMapping("/list-city/{provinceCode}")
    public ResponseVO getCityList(@PathVariable Long provinceCode) {
        List cityList = locationService.getCityListByProvinceCode(provinceCode);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, cityList);
    }

    @GetMapping("/list-district/{cityCode}")
    public ResponseVO getDistrictList(@PathVariable Long cityCode) {
        List areaList = locationService.getDistrictListByCityCode(cityCode);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, areaList);
    }

    @GetMapping("/list-street/{districtCode}")
    public ResponseVO getStreetList(@PathVariable Long districtCode) {
        List areaList = locationService.getStreetListByDistrictCode(districtCode);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, areaList);
    }
}
