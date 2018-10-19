package com.fuzamei.bonuspoint.service.impl.point;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.PointBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.dao.block.BlockInfoDao;
import com.fuzamei.bonuspoint.dao.common.mapper.PointInfoMapper;
import com.fuzamei.bonuspoint.dao.user.CompanyInfoDao;
import com.fuzamei.bonuspoint.entity.dto.user.KeyDTO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.point.PointInfoPO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.service.point.PointInfoService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @program: bonus-point-cloud
 * @description:
 * @author: WangJie
 * @create: 2018-05-10 10:09
 **/
@Slf4j
@Service
@RefreshScope
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class PointInfoServiceImpl implements PointInfoService {

    private final PointInfoMapper pointInfoMapper;
    private final AccountDao accountDao;
    private final CompanyInfoDao companyInfoDao;
    private final BlockInfoDao blockInfoDao;
    private final PointBC pointBC;
    private final BlockChainUtil blockChainUtil;
    @Value("${method.chain}")
    private boolean chainSwitch;

    @Autowired
    public PointInfoServiceImpl(PointInfoMapper pointInfoMapper, AccountDao accountDao,CompanyInfoDao companyInfoDao,BlockInfoDao blockInfoDao , PointBC pointBC,BlockChainUtil blockChainUtil) {
        this.pointInfoMapper = pointInfoMapper;
        this.accountDao = accountDao;
        this.companyInfoDao = companyInfoDao;
        this.blockInfoDao = blockInfoDao;
        this.pointBC = pointBC;
        this.blockChainUtil = blockChainUtil;
    }

    @Override
    public List<PointInfoPO> listExpiredPoint(long time) {
        return pointInfoMapper.listExpiredPoint(time);
    }

    @Override
    public int setPointExpired(Long pointId) {
        log.info("使id:{}的积分过期",pointId);
        int result = pointInfoMapper.handelExpiredPointById(pointId);
        PointInfoPO pointInfoPO = pointInfoMapper.selectByPrimaryKey(pointId);
        if (result == 1){
            if (chainSwitch) {
                KeyDTO platformKey = accountDao.getUserKeyById(pointInfoPO.getIssuePlatform());
                ResponseBean<String> responseBean = pointBC.expirePoints(pointInfoPO.getCompany(), pointInfoPO.getId(), platformKey.getPublicKey(), platformKey.getPrivateKey());

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

                BlockInfoPO blockInfoPO = new BlockInfoPO(pointInfoPO.getIssuePlatform(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.ExpirePoints_VALUE);
                blockInfoDao.insertBlockInfo(blockInfoPO);
            }
        }
        return result;
    }
}
