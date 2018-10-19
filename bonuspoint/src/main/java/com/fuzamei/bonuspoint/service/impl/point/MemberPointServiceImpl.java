/**
 * FileName: MemberPointServiceImpl
 * Author: wangtao
 * Date: 2018/4/24 21:01
 * Description:
 */
package com.fuzamei.bonuspoint.service.impl.point;

import com.fuzamei.bonuspoint.constant.PointInfoConstant;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.dao.common.mapper.*;
import com.fuzamei.bonuspoint.dao.point.MemberPointDao;
import com.fuzamei.bonuspoint.dao.user.ContactDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.point.ExchangePointDTO;
import com.fuzamei.bonuspoint.entity.dto.point.PointRecordDTO;
import com.fuzamei.bonuspoint.entity.dto.point.PointRelationDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.dto.user.QueryUserDTO;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.data.excel.ExcelPO;
import com.fuzamei.bonuspoint.entity.po.point.MemberPointPO;
import com.fuzamei.bonuspoint.entity.po.point.PointPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.entity.vo.point.APP.CompanyPointAPPVO;
import com.fuzamei.bonuspoint.entity.vo.point.APP.GeneralPointRecordAPPVO;
import com.fuzamei.bonuspoint.entity.vo.point.APP.MemberPointAPPVO;
import com.fuzamei.bonuspoint.entity.vo.point.APP.PointRecordAPPVO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.PointResponseEnum;
import com.fuzamei.bonuspoint.service.point.MemberPointService;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.vo.ResponseVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangtao
 * @create 2018/4/24
 */
@Slf4j
@Service
@RefreshScope
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class MemberPointServiceImpl implements MemberPointService {


    @Value("${page.pageSize}")
    private Integer pageSize;
    @Value("${blockChain.url}")
    private String blockChainUrl;
    @Value("${platform.publicKey}")
    private String platformPublicKey;
    private final UserDao userDao;

    private final MemberPointDao memberPointDao;

    private final ContactDao contactDao;

    private final PointRelationMapper pointRelationMapper;

    private final CompanyInfoMapper companyInfoMapper;

    private final PlatformInfoMapper platformInfoMapper;



    @Autowired
    public MemberPointServiceImpl(UserDao userDao, MemberPointDao memberPointDao, ContactDao contactDao,
                                  PointRelationMapper pointRelationMapper, PlatformInfoMapper platformInfoMapper,
                                  CompanyInfoMapper companyInfoMapper) {
        this.userDao = userDao;
        this.memberPointDao = memberPointDao;
        this.contactDao = contactDao;
        this.pointRelationMapper = pointRelationMapper;
        this.platformInfoMapper = platformInfoMapper;

        this.companyInfoMapper = companyInfoMapper;

    }

    /**
     * 获取会员积分明细，支持分页
     *
     * @param queryDTO 数据传输类
     * @return 响应类
     */
    @Override
    public ResponseVO memberPointListDetail(QueryPointDTO queryDTO) {
        Page page;
        List<PointRecordDTO> pointRecordDTOList = new ArrayList<>(1);
        if (queryDTO.getPointType() == PointInfoConstant.COMPANY_POINT) {
            page = PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
            pointRecordDTOList = memberPointDao.queryCompanyPointRecord(queryDTO);
        } else {
            page = PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
            pointRecordDTOList.addAll(memberPointDao.queryGeneralPointRecord(queryDTO));
        }

        UserDTO userDTO;
        for (PointRecordDTO p : pointRecordDTOList
                ) {
            userDTO = userDao.getUserInfoById(p.getOppositeUid());
            p.setOppsiteName(userDTO.getUsername());
            p.setOppsiteImg(userDTO.getHeadimgurl());
            p.setOppsitePublickey(userDTO.getPublickey());
        }
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(pointRecordDTOList, queryDTO.getPage(), queryDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);

    }

    /**
     * 兑换通用积分的积分列表获取
     *
     * @param queryPointDTO 数据传输类
     * @return 响应类
     */
    @Override
    public ResponseVO memberPointListRelation(QueryPointDTO queryPointDTO) {
        List<PointRelationDTO> pointRelationDTOList = pointRelationMapper.queryCompanyPointRelation(queryPointDTO);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pointRelationDTOList);
    }

    /**
     * 获取结算信息
     * lkm
     *
     * @param
     * @return 响应类
     */
    @Override
    public ResponseVO memberExchangeInfo(PagePointDTO pagePointDTO) {

        Example example = new Example(CompanyInfoPO.class);
        example.createCriteria().andEqualTo("uid", pagePointDTO.getId());
        //查询集团信息
        CompanyInfoPO companyInfoPO = companyInfoMapper.selectOneByExample(example);

        /**
         * 检查集团是否存在
         * */
        if (companyInfoPO == null) {
            return new ResponseVO(CommonResponseEnum.COMPANY_NOT_EXIST);
        }

        if (pagePointDTO.getPage() == null || pagePointDTO.getPage() < 1) {
            pagePointDTO.setPage(1);
        }

        if (pagePointDTO.getPageSize() == null) {
            pagePointDTO.setPageSize(pageSize);
        }

        Page page = PageHelper.startPage(pagePointDTO.getPage(), pagePointDTO.getPageSize());
        List<PointPO> pointList = memberPointDao.findexchangeInfoByUidAndGroupId(pagePointDTO);
        Integer total = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(pointList, pagePointDTO.getPage(), pagePointDTO.getPageSize(), total);
        return new ResponseVO<>(PointResponseEnum.POINT_FIND_SUCCESS, pageBean);
    }

    /**
     * 获取可转积分列表
     * lkm
     *
     * @param exchangeDTO 数据传输类
     * @return 响应类
     */
    @Override
    public ResponseVO memberTranPointList(ExchangePointDTO exchangeDTO) {

        Long uid = contactDao.findOpIdByPublickey(exchangeDTO.getOpPubKey());
        /**
         * 检查对方是否存在
         * */
        if (uid == null) {
            return new ResponseVO(PointResponseEnum.OTHER_NOT_EXIST);
        }
        UserPO userPO = memberPointDao.findUserById(uid);
        //定义封装数据的对象
        MemberPointAPPVO memberPointPO = new MemberPointAPPVO();
        if (userPO.getRole().equals(Roles.COMPANY)) {

            memberPointPO.setRole(String.valueOf(Roles.COMPANY));

            //根据uid查找集团名称
            String companyName = memberPointDao.findCompanyNameByUid(userPO.getId());
            memberPointPO.setCompanyName(companyName);
        }

        //查找用户的集团积分相关信息
        List<CompanyPointAPPVO> companyPointPOList = memberPointDao.findTranpointByUidCompany(exchangeDTO.getUid());
        if (companyPointPOList.size() != 0) {
            memberPointPO.setCompanyPointPOList(companyPointPOList);
        }

        //通过userId查找通用积分的数量
        String num = memberPointDao.findGeneralPointByUserId(exchangeDTO.getUid());
        memberPointPO.setGeneralPoint(num);

        return new ResponseVO<>(PointResponseEnum.POINT_FIND_SUCCESS, memberPointPO);


    }



    @Override
    public ResponseVO memberPointInfo(QueryUserDTO queryUserDTO) {
        if (queryUserDTO.getPage() == null || queryUserDTO.getPage() < 1) {
            queryUserDTO.setPage(1);
        }
        if (queryUserDTO.getPageSize() == null || queryUserDTO.getPageSize() < 1) {
            queryUserDTO.setPageSize(pageSize);
        }
        try {
            Page page = PageHelper.startPage(queryUserDTO.getPage(), queryUserDTO.getPageSize());
            List<MemberPointPO> memberPointPOList = memberPointDao.getMemberPointInfoListLikeMobile(queryUserDTO);
            int total = Integer.valueOf(String.valueOf(page.getTotal()));
            PageBean pageBean = new PageBean<>(memberPointPOList, queryUserDTO.getPage(), queryUserDTO.getPageSize(), total);
            return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS, pageBean);

        } catch (Exception e) {
            log.info("查询会员积分信息出错" + e.getMessage());
            return new ResponseVO(CommonResponseEnum.QUERY_FAIL);
        }

    }

    @Override
    public List<MemberPointPO> getAllMemberPointInfo(QueryUserDTO queryUserDTO) {
        return   memberPointDao.getMemberPointInfoListLikeMobile(queryUserDTO);
    }

    @Override
    public ResponseVO exchangeGeneralPlatform(PagePointDTO pagePointDTO) {

        Example example = new Example(PlatformInfoPO.class);
        example.createCriteria().andEqualTo("uid", pagePointDTO.getId());

        PlatformInfoPO platformInfoPO = platformInfoMapper.selectOneByExample(example);

        if (platformInfoPO == null) {
            return new ResponseVO(CommonResponseEnum.PLATFORM_NOT_EXIST);
        }

        if (pagePointDTO.getPage() == null || pagePointDTO.getPage() < 1) {
            pagePointDTO.setPage(1);
        }
        if (pagePointDTO.getPageSize() == null) {
            pagePointDTO.setPageSize(pageSize);
        }

        Page page = PageHelper.startPage(pagePointDTO.getPage(), pagePointDTO.getPageSize());
        List<ExcelPO> excelPOList = memberPointDao.findExchangeGeneralByPlatfromId(pagePointDTO);
        excelPOList.stream().forEach(e ->{
            if(e.getCashNum() != null){
                e.setCashNumStr("-"+e.getCashNum());
            }
        });

        Integer total = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(excelPOList, pagePointDTO.getPage(), pagePointDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.FIND_BUY_GENERAL_POINTS_SUCCESS, pageBean);

    }


    @Override
    public ResponseVO userPointInfo(PagePointDTO pagePointDTO) {
        if (pagePointDTO.getPage() == null || pagePointDTO.getPage() < 1) {
            pagePointDTO.setPage(1);
        }
        if (pagePointDTO.getPageSize() == null) {
            pagePointDTO.setPageSize(pageSize);
        }
        Page page = PageHelper.startPage(pagePointDTO.getPage(), pagePointDTO.getPageSize());
        List<ExchangePointDTO> userPointList = memberPointDao.findUserPointInfo(pagePointDTO);
        if (userPointList != null) {
            for (ExchangePointDTO ex : userPointList) {
                ex.setPointTypeStr(PointInfoConstant.COMPANY_POINT_STR);
                if (PointInfoConstant.POINT_OUT == ex.getType()) {
                    ex.setTypeStr(PointInfoConstant.POINT_OUT_STR);
                } else {
                    ex.setTypeStr(PointInfoConstant.POINT_IN_STR);
                }
            }
        }
        List<ExchangePointDTO> userGeneralPointList = memberPointDao.findUserGeneralPointInfo(pagePointDTO);
        if (userGeneralPointList != null) {
            for (ExchangePointDTO ex : userGeneralPointList) {
                ex.setPointTypeStr(PointInfoConstant.GENERAL_POINT_STR);
                if (PointInfoConstant.POINT_OUT == ex.getType()) {
                    ex.setTypeStr(PointInfoConstant.POINT_OUT_STR);
                } else {
                    ex.setTypeStr(PointInfoConstant.POINT_IN_STR);
                }
            }
        }
        userPointList.addAll(userGeneralPointList);
        Integer total = Integer.parseInt(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(userPointList, pagePointDTO.getPage(), pagePointDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    @Override
    public ResponseVO pointListInfo(ExchangePointDTO exchangePointDTO) {
        Integer pointType = exchangePointDTO.getPointType();
        //积分为集团积分的情况
        if (pointType == PointInfoConstant.COMPANY_POINT) {
            PointRecordAPPVO pointRecordVO = memberPointDao.findPointListInfoCompany(exchangePointDTO);
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, pointRecordVO);

        } else if (pointType == PointInfoConstant.GENERAL_POINT) {
            //积分为通用积分的情况
            GeneralPointRecordAPPVO generalPointRecordVO = memberPointDao.findPointListInfoGeneral(exchangePointDTO);
            return new ResponseVO<>(CommonResponseEnum.SUCCESS, generalPointRecordVO);
        } else {
            return new ResponseVO(CommonResponseEnum.FAILURE);
        }

    }
}