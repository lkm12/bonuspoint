/**
 * FileName: PlatformServiceImpl
 * Author: wangtao
 * Date: 2018/4/27 16:21
 * Description:
 */
package com.fuzamei.bonuspoint.service.impl.point;

import com.fuzamei.bonuspoint.constant.GeneralPointRecordConstant;
import com.fuzamei.bonuspoint.constant.PointInfoConstant;
import com.fuzamei.bonuspoint.constant.PointRecordConstant;
import com.fuzamei.bonuspoint.dao.common.mapper.PointRecordMapper;
import com.fuzamei.bonuspoint.dao.point.GeneralPointRecordDao;
import com.fuzamei.bonuspoint.dao.user.CompanyInfoDao;
import com.fuzamei.bonuspoint.entity.dto.point.PointRecordDTO;
import com.fuzamei.bonuspoint.entity.po.point.GeneralPointInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.PointRecordPO;
import com.fuzamei.bonuspoint.entity.po.user.CompanyInfoPO;
import com.fuzamei.bonuspoint.dao.point.PlatformPointDao;
import com.fuzamei.bonuspoint.dao.common.mapper.PointInfoMapper;
import com.fuzamei.bonuspoint.entity.dto.point.PointInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.point.QueryPointDTO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.PointResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fuzamei.bonuspoint.service.point.PlatformPointService;
import com.fuzamei.common.bean.PageBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangtao
 * @create 2018/4/27
 */
@Slf4j
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class PlatformPointServiceImpl implements PlatformPointService {

    private final PlatformPointDao platformDao;

    private final PointInfoMapper pointInfoDao;

    private final GeneralPointRecordDao generalPointRecordDao;

    private final CompanyInfoDao companyInfoDao;

    private final PointRecordMapper pointRecordMapper;

    @Autowired
    public PlatformPointServiceImpl(PlatformPointDao platformDao, PointInfoMapper pointInfoDao,
                                    GeneralPointRecordDao generalPointRecordDao, CompanyInfoDao companyInfoDao,
                                    PointRecordMapper pointRecordMapper) {
        this.platformDao = platformDao;
        this.pointInfoDao = pointInfoDao;
        this.generalPointRecordDao = generalPointRecordDao;
        this.companyInfoDao = companyInfoDao;
        this.pointRecordMapper = pointRecordMapper ;
    }


    /**
     * 同意发放积分
     * @param uid uid uid
     * @param id id id
     * @return
     */
    @Override
    @Transactional
    public ResponseVO reviewPoint(Long uid, Long id) {
        //判断用户权限
        Boolean isPlatformUser = platformDao.isPlatformUser(uid);
        if (isPlatformUser == null || !isPlatformUser) {
            return new ResponseVO(UserResponseEnum.USER_NOT_ROOT);
        }

        //检查积分状态
        PointInfoPO pointInfoPO = pointInfoDao.selectByPrimaryKey(id);
        if (pointInfoPO == null || PointInfoConstant.CHECK_PENDING != pointInfoPO.getStatus()) {
            return new ResponseVO(PointResponseEnum.EXAMINE_REPEAT);
        }

        //同意发放积分

        int info = platformDao.reviewPoint(pointInfoPO.getId());
        if (info != 1) {
            throw new RuntimeException("更新积分状态失败!");
        }

        //获取集团信息
        CompanyInfoPO companyInfoPO = companyInfoDao.getCompanyInfoById(pointInfoPO.getCompany());

        //插入积分交易记录
        PointRecordPO pointRecordPO = new PointRecordPO();
        pointRecordPO.setUid(companyInfoPO.getUid());
        pointRecordPO.setOppositeUid(uid);
        pointRecordPO.setType(PointRecordConstant.POINT_ADD);
        pointRecordPO.setNum(pointInfoPO.getNum());
        pointRecordPO.setCategory(PointRecordConstant.CATEGORY_GROUP_APPLY);
        pointRecordPO.setPointId(pointInfoPO.getId());
        pointRecordPO.setPointRate(companyInfoPO.getPointRate());
        pointRecordPO.setCreatedAt(System.currentTimeMillis());
        pointRecordPO.setUpdatedAt(System.currentTimeMillis());
        info = pointRecordMapper.insert(pointRecordPO);
        if (info != 1) {
            throw new RuntimeException("插入积分交易记录失败！");
        }
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 审核拒绝发放积分
     * @param uid 用户id 用户id
     * @param id  申请记录 申请记录
     * @param reason 拒绝原因 拒绝原因
     * @return
     */
    @Override
    @Transactional
    public ResponseVO refusePoint(Long uid, Long id, String reason) {
        //判断用户权限
        Boolean isPlatformUser = platformDao.isPlatformUser(uid);
        if (isPlatformUser == null || !isPlatformUser) {
            return new ResponseVO(UserResponseEnum.USER_NOT_ROOT);
        }

        PointInfoPO pointInfoPO = pointInfoDao.selectByPrimaryKey(id);
        if (pointInfoPO == null || PointInfoConstant.CHECK_PENDING != pointInfoPO.getStatus()) {
            return new ResponseVO(PointResponseEnum.EXAMINE_REPEAT);
        }

        //拒绝积分申请
        int info = platformDao.refusePoint(pointInfoPO.getId(), reason);

        if (info < 1) {
            log.info("审核失败");
            return new ResponseVO(PointResponseEnum.CHECK_ERROR);
        }
        return new ResponseVO(CommonResponseEnum.SUCCESS);
    }

    /**
     * 平台查看积分发行审核列表
     *
     * @param queryPointDTO
     * @return
     * @wangjie
     */
    @Override
    public ResponseVO<PageBean<PointInfoDTO>> pointIssueList(QueryPointDTO queryPointDTO) {
        Page page = PageHelper.startPage(queryPointDTO.getPage(), queryPointDTO.getPageSize());
        List<PointInfoDTO> pointInfoDTOList = pointInfoDao.pointIssueList(queryPointDTO);
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean<PointInfoDTO> pageBean = new PageBean<>(pointInfoDTOList, queryPointDTO.getPage(), queryPointDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    @Override
    public ResponseVO pointGrantList(QueryPointDTO queryPointDTO) {
        queryPointDTO.setType(GeneralPointRecordConstant.POINT_SUB);
        queryPointDTO.setCategory(GeneralPointRecordConstant.CATEGORY_PLATFORM_GRANT_OUT);
        Page page = PageHelper.startPage(queryPointDTO.getPage(), queryPointDTO.getPageSize());
        List<PointRecordDTO> pointRecordDTOList = generalPointRecordDao.grantPointRecordList(queryPointDTO);
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(pointRecordDTOList, queryPointDTO.getPage(), queryPointDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.SUCCESS, pageBean);
    }

    @Override
    public GeneralPointInfoPO getGeneralPointInfoByPlatformId(Long platformId) {

        return generalPointRecordDao.getGeneralPointInfoByPlatformId(platformId);
    }
}
