package com.fuzamei.bonuspoint.service.impl.user.platform;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.PlatformBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.AccountMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.PlatformInfoMapper;
import com.fuzamei.bonuspoint.entity.dto.PlatformInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.user.KeyDTO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.entity.vo.user.PlatformBaseInfoVO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.service.user.platform.PlatformService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-07-13 15:08
 **/
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    private  PlatformInfoMapper platformInfoMapper;
    @Autowired
    private  AccountDao accountDao;
    @Autowired
    private  PlatformBC platformBC;
    @Autowired
    private BlockChainUtil blockChainUtil;
    @Autowired
    private BlockInfoDao blockInfoDao;



    @Override
    public ResponseVO<PlatformBaseInfoVO> getSelfPlatformBaseInfo(Long uid) {
        Example example = new Example(PlatformInfoPO.class);
        example.createCriteria().andEqualTo("uid",uid);
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectOneByExample(example);
        PlatformBaseInfoVO platformBaseInfoVO = new PlatformBaseInfoVO();
        BeanUtils.copyProperties(platformInfoPO,platformBaseInfoVO);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,platformBaseInfoVO);
    }

    @Override
    public ResponseVO updatePlatformBaseInfo(PlatformInfoDTO platformInfoDTO) {

        PlatformInfoPO platformInfoPO = new PlatformInfoPO();
        BeanUtils.copyProperties(platformInfoDTO,platformInfoPO);
        Example example = new Example(PlatformInfoPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid",platformInfoDTO.getUid());
        int result = platformInfoMapper.updateByExampleSelective(platformInfoPO,example);
        if (result==1){
            return new ResponseVO(SafeResponseEnum.UPDATE_SUCCESS);
        }
        return new ResponseVO(SafeResponseEnum.UPDATE_FAIL);
    }

    @Override
    public ResponseVO updatePlatformPointRate(Long platformUid, Float pointRate) {

        PlatformInfoPO platformInfoPO = new PlatformInfoPO();
        platformInfoPO.setPointRate(pointRate);
        Example example = new Example(PlatformInfoPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid",platformUid);
        platformInfoPO = platformInfoMapper.selectOneByExample(example);
        platformInfoPO.setPointRate(pointRate);
        int result = platformInfoMapper.updateByPrimaryKeySelective(platformInfoPO);
        if (result ==0){
            return new ResponseVO(CommonResponseEnum.UPDATE_FALL);
        }
        KeyDTO platformKey = accountDao.getUserKeyById(platformUid);
        ResponseBean<String> responseBean = platformBC.setPlatformPointRate(platformInfoPO.getId(), platformInfoPO.getPointRate().toString(), platformKey.getPublicKey(), platformKey.getPrivateKey());

        ResponseBean<TransactionResultBean> queryTransaction = blockChainUtil.queryTransaction(responseBean.getResult());
        if (queryTransaction == null) {
            log.info("上链失败");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        if (StringUtil.isNotBlank(queryTransaction.getError())) {
            log.info("上链失败");
            throw new RuntimeException(BlockChainResponseEnum.ADD_BLOCK_FAILED.getMessage());
        }
        log.info("上链成功");
        Long height = queryTransaction.getResult().getHeight();
        String hash = responseBean.getResult();

        BlockInfoPO blockInfoPO = new BlockInfoPO(platformUid, height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.SetPointRate_VALUE);
        blockInfoDao.insertBlockInfo(blockInfoPO);
        return new ResponseVO(CommonResponseEnum.UPDATE_SUCCESS);
    }
}
