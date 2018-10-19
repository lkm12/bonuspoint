package com.fuzamei.bonuspoint.controller.data.excel;

import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.user.CompanyInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.user.QueryUserDTO;
import com.fuzamei.bonuspoint.entity.po.data.excel.MemberPointExcel;
import com.fuzamei.bonuspoint.entity.po.point.MemberPointPO;
import com.fuzamei.bonuspoint.service.point.MemberPointService;
import com.fuzamei.bonuspoint.service.user.company.CompanyInfoService;
import com.fuzamei.bonuspoint.util.ExcelUtil;
import com.fuzamei.bonuspoint.util.TimeUtil;
import com.xuxueli.poi.excel.ExcelExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 18519 on 2018/5/1.
 * lkm
 */
@Slf4j
@RestController
@RequestMapping("/bonus-point/excel")
public class ExcelQueryController {


    private final CompanyInfoService companyInfoService;
    private final MemberPointService memberPointService;

    @Autowired
    public ExcelQueryController(MemberPointService memberPointService,CompanyInfoService companyInfoService) {

        this.companyInfoService = companyInfoService;
        this.memberPointService = memberPointService;
    }

    @PostMapping("/company/download-member-point-info")
    public void downloadMemberPointInfo(HttpServletRequest request, HttpServletResponse response, @RequestAttribute("token") Token token, @RequestBody QueryUserDTO queryUserDTO) {
        queryUserDTO.setUid(token.getUid());
        Long companyId = companyInfoService.getCompanyIdByUid(token.getUid());
        queryUserDTO.setCompanyId(companyId);
        List<MemberPointPO> memberPointPOList = memberPointService.getAllMemberPointInfo(queryUserDTO);
        List<MemberPointExcel> memberPointExcelList = memberPointPOList.stream().map(memberPointPO -> {
            MemberPointExcel memberPointExcel = new MemberPointExcel();
            BeanUtils.copyProperties(memberPointPO,memberPointExcel);
            memberPointExcel.setCreatedAt(TimeUtil.transformTimeMillisToFormatTime(memberPointPO.getCreatedAt()));
            return memberPointExcel;
        }).collect(Collectors.toList());

        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream ;charset=utf-8 ");
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" +java.net.URLEncoder.encode("积分详情.xls", "UTF-8"));
            response.getOutputStream().write(ExcelExportUtil.exportToBytes(memberPointExcelList));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    @GetMapping("/platform/download-member-point-info")
    public void downloadMemeberPointInfo(HttpServletRequest request, HttpServletResponse response,@RequestAttribute("token") Token token){
        List<CompanyInfoDTO> companyInfoDTOList = companyInfoService.getAllCompanyInSamePlatform(token.getUid());
        Workbook workbook = new HSSFWorkbook();
        companyInfoDTOList.stream().map(companyInfoDTO -> {
            QueryUserDTO queryUserDTO = new QueryUserDTO();
            queryUserDTO.setCompanyId(companyInfoDTO.getId());
            List<MemberPointPO> memberPointPOList = memberPointService.getAllMemberPointInfo(queryUserDTO);
            List<MemberPointExcel> memberPointExcelList = memberPointPOList.stream().map(memberPointPO -> {
                MemberPointExcel memberPointExcel = new MemberPointExcel();
                BeanUtils.copyProperties(memberPointPO,memberPointExcel);
                memberPointExcel.setCreatedAt(TimeUtil.transformTimeMillisToFormatTime(memberPointPO.getCreatedAt()));
                return memberPointExcel;
            }).collect(Collectors.toList());
            ExcelUtil.exportWorkbook(workbook,memberPointExcelList,companyInfoDTO.getCompanyName());
            return null;
        }).toArray();

        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream ;charset=utf-8 ");
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" +java.net.URLEncoder.encode("积分详情.xls", "UTF-8"));
            response.getOutputStream().write(ExcelUtil.exportToBytes(workbook));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
