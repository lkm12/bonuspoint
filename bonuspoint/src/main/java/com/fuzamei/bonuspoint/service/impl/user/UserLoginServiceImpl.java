package com.fuzamei.bonuspoint.service.impl.user;

import com.fuzamei.bonuspoint.constant.CacheHeader;
import com.fuzamei.bonuspoint.constant.Roles;
import com.fuzamei.bonuspoint.dao.common.mapper.AccountMapper;
import com.fuzamei.bonuspoint.dao.common.mapper.PlatformInfoMapper;
import com.fuzamei.bonuspoint.entity.Token;
import com.fuzamei.bonuspoint.entity.dto.user.UserDTO;
import com.fuzamei.bonuspoint.entity.po.account.AccountPO;
import com.fuzamei.bonuspoint.entity.po.user.PlatformInfoPO;
import com.fuzamei.bonuspoint.enums.CommonResponseEnum;
import com.fuzamei.bonuspoint.enums.SafeResponseEnum;
import com.fuzamei.bonuspoint.enums.UserResponseEnum;
import com.fuzamei.bonuspoint.service.user.UserLoginService;
import com.fuzamei.bonuspoint.util.RandomUtil;
import com.fuzamei.bonuspoint.util.RedisTemplateUtil;
import com.fuzamei.bonuspoint.util.TokenUtil;
import com.fuzamei.common.model.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RefreshScope
@Transactional(rollbackFor = Exception.class)
public class UserLoginServiceImpl implements UserLoginService{


    /** 加密密钥 */
    @Value("${token.encrypt.key}")
    private String encryptKey;

    @Value("${token.client.browser}")
    private String browserClient;

    private final AccountMapper accountMapper;

    private final PlatformInfoMapper platformInfoMapper;

    private final RedisTemplateUtil redisTemplateUtil;

    @Value("${token.outTime.browser}")
    private Long browserOutTime;

    @Value(("${token.client.app}"))
    private String appClient;


    @Value("${token.outTime.app}")
    private Long appOutTime;

    private final RedisTemplate redisTemplate;
    @Autowired
    public UserLoginServiceImpl(AccountMapper accountMapper,PlatformInfoMapper platformInfoMapper,RedisTemplateUtil redisTemplateUtil,
            RedisTemplate redisTemplate){
        this.accountMapper = accountMapper;
        this.platformInfoMapper = platformInfoMapper;
        this.redisTemplateUtil = redisTemplateUtil;
        this.redisTemplate = redisTemplate;
    }



    @Override
    public ResponseVO browserLogin(UserDTO userDTO) {

        Example examplePlatform = new Example(PlatformInfoPO.class);
        Example.Criteria criteriaPlatform = examplePlatform.createCriteria();
        criteriaPlatform.andEqualTo("mark",userDTO.getMark());
        //通过平台mark查出平台信息
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectOneByExample(examplePlatform);

        //平台不存在
        if(platformInfoPO == null){
            return new ResponseVO(CommonResponseEnum.PLATFORM_NOT_EXIST);
        }

        //用户不能进后台
        if (userDTO.getTokenType().equals(browserClient) && userDTO.getRole().equals(Roles.MEMBER)) {
            return new ResponseVO(SafeResponseEnum.AUTHORITY_WRONG);
        }

        Example example = new Example(AccountPO.class);
        Example.Criteria criteria = example.createCriteria();
        AccountPO accountPO;
        switch (userDTO.getRole()){
            //为商户时
            case 2:{
                criteria.andEqualTo("username", userDTO.getUsername());
                criteria.andEqualTo("passwordHash", userDTO.getPasswordHash());
                criteria.andEqualTo("pId",platformInfoPO.getUid());
                criteria.andEqualTo("role",userDTO.getRole());
                 accountPO = accountMapper.selectOneByExample(example);
                if(accountPO == null){
                    return new ResponseVO(UserResponseEnum.USERNAME_PASSWORD_FAIL);
                }
            }
            break;
            //为平台与总平台时
            default:{
                criteria.andEqualTo("username", userDTO.getUsername());
                criteria.andEqualTo("passwordHash", userDTO.getPasswordHash());
                criteria.andEqualTo("role", userDTO.getRole());
                accountPO = accountMapper.selectOneByExample(example);
                if(accountPO == null){
                    return new ResponseVO(UserResponseEnum.USERNAME_PASSWORD_FAIL);
                }
                if(!platformInfoPO.getUid().equals(accountPO.getId())){
                    return new ResponseVO(CommonResponseEnum.PLATFORM_LOGIN_ERROR);
                }
            }
            break;
        }

        userDTO.setId(accountPO.getId());
        /**
         * 进行token的更新
         */
        Token token = new Token();
        token.setUid(accountPO.getId());
        token.setRole(accountPO.getRole());
        token.setClient(userDTO.getTokenType());
        token.setTokenStr(RandomUtil.getRandomString(32));
        token.setPId(platformInfoPO.getUid());
        String tokenString = TokenUtil.getTokenString(token, encryptKey);
        Token accessToken = new Token(tokenString);

        if (token.getClient().equals(browserClient)) {
            redisTemplateUtil.setStr(CacheHeader.TOKEN + token.getUid(), token.getTokenStr(), browserOutTime, TimeUnit.SECONDS);
        }

        Long currentTime = System.currentTimeMillis();


        //添加登录时间
        AccountPO accountPOLogin = new AccountPO();
        accountPOLogin.setLoginAt(currentTime);
        accountPOLogin.setUpdatedAt(currentTime);
        accountPOLogin.setId(accountPO.getId());
        //更新登录时间
        int i = accountMapper.updateByPrimaryKeySelective(accountPOLogin);
        if (i < 0) {
            log.info("登录时间更新失败");
            return new ResponseVO(CommonResponseEnum.UPDATE_FALL);

        }

        return new ResponseVO<>(UserResponseEnum.MEMBER_LOGIN_SUCCESS, accessToken);

    }


    @Override
    public ResponseVO memberLogin(UserDTO userDTO) {

        Example examplePlatform = new Example(PlatformInfoPO.class);
        Example.Criteria criteriaPlatform = examplePlatform.createCriteria();
        criteriaPlatform.andEqualTo("mark",userDTO.getMark());
        //通过平台mark查出平台信息
        PlatformInfoPO platformInfoPO = platformInfoMapper.selectOneByExample(examplePlatform);

        //平台不存在
        if(platformInfoPO == null){
            return new ResponseVO(CommonResponseEnum.PLATFORM_NOT_EXIST);
        }


        Example example = new Example(AccountPO.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("username", userDTO.getUsername());
        criteria.andEqualTo("passwordHash", userDTO.getPasswordHash());
        criteria.andEqualTo("pId",platformInfoPO.getUid());
        criteria.andEqualTo("role",Roles.MEMBER);
        AccountPO accountPO = accountMapper.selectOneByExample(example);
        if(accountPO == null){
         return new ResponseVO(UserResponseEnum.USERNAME_PASSWORD_FAIL);
        }

        userDTO.setId(accountPO.getId());
        /**
         * 进行token的更新
         */
        Token token = new Token();
        token.setUid(accountPO.getId());
        token.setRole(accountPO.getRole());
        token.setClient(userDTO.getTokenType());
        token.setTokenStr(RandomUtil.getRandomString(32));
        token.setPId(platformInfoPO.getUid());
        String tokenString = TokenUtil.getTokenString(token, encryptKey);
        Token accessToken = new Token(tokenString);

        if (token.getClient().equals(appClient)) {
            redisTemplateUtil.setStr(CacheHeader.TOKEN + token.getUid(), token.getTokenStr(), appOutTime, TimeUnit.SECONDS);
        }

        Long currentTime = System.currentTimeMillis();


        AccountPO accountPOLogin = new AccountPO();
        accountPOLogin.setLoginAt(currentTime);
        accountPOLogin.setUpdatedAt(currentTime);
        accountPOLogin.setId(accountPO.getId());
        //更新登录时间
        int i = accountMapper.updateByPrimaryKeySelective(accountPOLogin);
        if (i < 0) {
            log.info("登录时间更新失败");
            return new ResponseVO(CommonResponseEnum.UPDATE_FALL);

        }
        return new ResponseVO<>(UserResponseEnum.MEMBER_LOGIN_SUCCESS, accessToken);

    }

}
