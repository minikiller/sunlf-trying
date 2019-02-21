package com.kalix.fabric8.security.biz;

import com.kalix.fabric8.cache.api.biz.ICacheManager;
import com.kalix.fabric8.jwt.api.biz.IJwtService;
import com.kalix.fabric8.security.api.biz.ISecurityService;
import com.kalix.fabric8.user.api.biz.IUserService;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SecurityServiceImpl implements ISecurityService {
//    private ICacheManager msCacheManager;
    private String expTimeout;
    private IJwtService msJwtService;
    private IUserService msUserService;

    public void setMsJwtService(IJwtService msJwtService) {
        this.msJwtService = msJwtService;
    }

    public void setExpTimeout(String expTimeout) {
        this.expTimeout = expTimeout;
    }

//    public void setMsCacheManager(ICacheManager msCacheManager) {
//        this.msCacheManager = msCacheManager;
//    }

    public void setMsUserService(IUserService msUserService) {
        this.msUserService = msUserService;
    }

    @Override
    public Map<String, Object> doLogin(String loginName, String password) {
        // 重新登录，清空历史登录人信息
//        Object loginUserHistory = msCacheManager.get(loginName);
//        if (loginUserHistory != null) {
//            msCacheManager.del(loginName);
//        }
        // 验证登录账号
        Map<String, Object> responseInfo = msUserService.checkLoginUser(loginName, password);

        long loginTime = System.currentTimeMillis();
        long expTime = Long.valueOf(expTimeout);
        if (responseInfo != null) {
            // 创建Jwt token
            Map<String, Object> credentialMap = new HashMap<>();
            credentialMap.put("iss", loginName + UUID.randomUUID().toString().replace("-", ""));
            credentialMap.put("iat", loginTime);
            if (expTime > 0) {
                credentialMap.put("exp", loginTime + expTime);
            }
            credentialMap.put("currentUserRealName", responseInfo.get("name"));
            credentialMap.put("currentUserLoginName", responseInfo.get("user_name"));
            credentialMap.put("currentUserId", responseInfo.get("user_id"));
            credentialMap.put("currentUserIcon", responseInfo.get("user_icon"));
            String jwtToken = msJwtService.createJwt_RS256(credentialMap);

            // 保存登录用户信息到redis
//            Map<String, Object> userMap = new HashMap<>();
//            userMap.put("loginTime", credentialMap.get("iat"));
//            userMap.put("currentUserLoginName", credentialMap.get("currentUserLoginName"));
//            userMap.put("currentUserId", credentialMap.get("currentUserId"));
//            userMap.put("access_token", jwtToken);
//            msCacheManager.save(loginName, userMap);

            responseInfo.put("jwtToken", jwtToken);
            return responseInfo;
        }
        return null;
    }

    @Override
    public boolean checkLoginToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        Claims claims = msJwtService.parseJwt_RS256(token);
        if (claims != null) {
            // 添加超时验证逻辑
            Date expDate = claims.getExpiration();
            Long expTime = (expDate.getTime())/1000;
            Long currentTime = System.currentTimeMillis();
            if (currentTime > expTime) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public Map<String, Object> getCurrentLoginUser(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        Claims claims = msJwtService.parseJwt_RS256(token);
        if (claims == null) {
            return null;
        }
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("loginTime", (Long)claims.get("iat"));
        userMap.put("currentUserLoginName", (String)claims.get("currentUserLoginName"));
        userMap.put("currentUserId", (String)claims.get("currentUserId"));
        return userMap;
    }
}
