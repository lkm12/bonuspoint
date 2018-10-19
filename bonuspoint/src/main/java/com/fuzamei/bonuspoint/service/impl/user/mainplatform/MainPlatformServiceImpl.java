package com.fuzamei.bonuspoint.service.impl.user.mainplatform;

import brokerpoints.Brokerpoints;
import com.fuzamei.bonuspoint.blockchain.bc.MainPlatformBC;
import com.fuzamei.bonuspoint.blockchain.util.BlockChainUtil;
import com.fuzamei.bonuspoint.constant.UserStatus;
import com.fuzamei.bonuspoint.dao.account.AccountDao;
import com.fuzamei.bonuspoint.dao.common.mapper.AccountMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.BlockInfoMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.PlatformInfoMapper;
import com.fuzamei.bonuspoint.dao.point.MemberPointDao;
import com.fuzamei.bonuspoint.dao.user.UserDao;
import com.fuzamei.bonuspoint.entity.dto.PlatformInfoDTO;
import com.fuzamei.bonuspoint.entity.dto.account.AccountDTO;
import com.fuzamei.bonuspoint.entity.dto.user.KeyDTO;
import com.fuzamei.bonuspoint.entity.dto.user.PagePointDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.block.BlockInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.entity.po.user.UserPO;
import com.fuzamei.bonuspoint.enums.BlockChainResponseEnum;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.user.UserService;
import com.fuzamei.bonuspoint.service.user.mainplatform.MainPlatformService;
import com.fuzamei.bonuspoint.util.StringUtil;
import com.fuzamei.common.bean.PageBean;
import com.fuzamei.common.model.dto.PageDTO;
import com.fuzamei.common.model.vo.ResponseVO;
import com.fzm.blockchain.entity.ResponseBean;
import com.fzm.blockchain.entity.TransactionResultBean;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MainPlatformServiceImpl implements MainPlatformService {

    @Value("${page.pageSize}")
    private Integer pageSize;
    @Value("${method.chain}")
    private boolean chainSwitch;
    private final UserService userService;
    private final UserDao userDao;

    private final AccountMapper accountMapper;

    private final PlatformInfoMapper platformInfoMapper;

    private final AccountDao accountDao;
    private final MainPlatformBC mainPlatformBC;
    private final BlockChainUtil blockChainUtil;
    private final BlockInfoMapper blockInfoMapper;
    private final MemberPointDao memberPointDao;

    @Autowired
    public MainPlatformServiceImpl(BlockInfoMapper blockInfoMapper, UserService userService, UserDao userDao, AccountMapper accountMapper,
                                   PlatformInfoMapper platformInfoMapper, AccountDao accountDao, MainPlatformBC mainPlatformBC,
                                   BlockChainUtil blockChainUtil,MemberPointDao memberPointDao) {
        this.blockInfoMapper = blockInfoMapper;
        this.userService = userService;
        this.userDao = userDao;
        this.accountMapper = accountMapper;
        this.platformInfoMapper = platformInfoMapper;
        this.accountDao = accountDao;
        this.mainPlatformBC = mainPlatformBC;
        this.blockChainUtil = blockChainUtil;
        this.memberPointDao = memberPointDao;
    }



    @Override
    public ResponseVO addPlatform(AccountDTO accountDTO, PlatformInfoDTO platformInfoDTO) {
        AccountPO accountPO = new AccountPO();
        BeanUtils.copyProperties(accountDTO, accountPO);
        accountPO.setStatus(UserStatus.AVAILABLE);
        accountPO.setIsInitialize(UserStatus.UNINIT);
        accountPO.setCreatedAt(System.currentTimeMillis());
        int result = accountMapper.insertSelective(accountPO);
        if (result != 1) {
            throw new RuntimeException("添加平台管理员失败");
        }


        PlatformInfoPO platformInfoPO = new PlatformInfoPO();
        BeanUtils.copyProperties(platformInfoDTO,platformInfoPO);
        platformInfoPO.setUid(accountPO.getId());
        platformInfoPO.setMark(UUID.randomUUID().toString());
        result = platformInfoMapper.insertSelective(platformInfoPO);
        if (result != 1) {
            throw new RuntimeException("添加平台信息失败");
        }

        if (chainSwitch) {
            KeyDTO mainPlatformKey = accountDao.getUserKeyById(accountDTO.getPId());
            System.out.println(mainPlatformKey);
            System.out.println(platformInfoPO);
            ResponseBean<String> responseBean = mainPlatformBC.createPlatform(platformInfoPO.getId().toString(), platformInfoPO.getCashRate().toString(), platformInfoPO.getPointRate().toString(), mainPlatformKey.getPublicKey(), mainPlatformKey.getPrivateKey());
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

            BlockInfoPO blockInfoPO = new BlockInfoPO(accountDTO.getPId(), height, hash, System.currentTimeMillis(), Brokerpoints.ActionType.CreatePlatform_VALUE);
            result = blockInfoMapper.insertSelective(blockInfoPO);
            if (result != 1) {
                log.error("添加上链结果到数据库失败，添加信息为：{}",blockInfoPO);
            }
        }
        return new ResponseVO<>(CommonResponseEnum.ADD_SUCCESS, accountPO);
    }

    @Override
    public ResponseVO listPlatform(PageDTO pageDTO) {
        Example example = new Example(PlatformInfoPO.class);
        Example.Criteria criteria = example.createCriteria();
        if (pageDTO.getFuzzyMatch()!=null) {
            criteria.andLike("platform_name", pageDTO.getFuzzyMatch());
        }
        Page page = PageHelper.startPage(pageDTO.getPage(), pageDTO.getPageSize());
        List<PlatformInfoPO> appVersionPOList =platformInfoMapper.selectByExample(example);
        int total = Integer.valueOf(String.valueOf(page.getTotal()));
        PageBean pageBean = new PageBean<>(appVersionPOList, pageDTO.getPage(), pageDTO.getPageSize(), total);
        return new ResponseVO<>(CommonResponseEnum.QUERY_SUCCESS,pageBean);
    }




}
