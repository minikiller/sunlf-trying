package com.kalix.fabric8.security.biz;

import com.kalix.fabric8.jwt.api.biz.IJwtService;
import com.kalix.fabric8.security.api.biz.ISecurityService;
import com.kalix.fabric8.user.api.biz.IUserService;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SecurityServiceImpl implements ISecurityService {
//    private ICacheManager cacheManager;
    private IJwtService msJwtService;
    private IUserService msUserService;

    public void setMsJwtService(IJwtService msJwtService) {
        this.msJwtService = msJwtService;
    }

    /**
    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
**/


    public void setMsUserService(IUserService msUserService) {
        this.msUserService = msUserService;
    }

    @Override
    public Map<String, Object> doLogin(String loginName, String password) {
        // 清空历史token
//        String tokenHistory = cacheManager.get("jwtToken");
//        if (tokenHistory != null) {
//            cacheManager.del("jwtToken");
//        }
        // 验证登录账号
        Map<String, Object> responseInfo = msUserService.checkLoginUser(loginName, password);

        long loginTime = System.currentTimeMillis();
        if (responseInfo != null) {
            // 创建Jwt token
            Map<String, Object> credentialMap = new HashMap<>();
            credentialMap.put("iss", loginName + UUID.randomUUID().toString().replace("-", ""));
            credentialMap.put("iat", loginTime);
            credentialMap.put("currentUserRealName", responseInfo.get("name"));
            credentialMap.put("currentUserLoginName", responseInfo.get("user_name"));
            credentialMap.put("currentUserId", responseInfo.get("user_id"));
            credentialMap.put("currentUserIcon", responseInfo.get("user_icon"));
            String jwtToken = msJwtService.createJwt_RS256(credentialMap);
            // 保存token到redis
//            cacheManager.save(jwtToken, loginName);
            responseInfo.put("jwtToken", jwtToken);
            /**
            return JsonStatus.successResult("{\"success\":true," +
                    "\"message\":\"login success\"," +
                    "\"user\":{\"name\":\"" + responseInfo.get("name") +
                    "\",\"id\":\"" + responseInfo.get("user_id") + "\"},\"access_token\":\"" + jwtToken + "\"}");
            **/
            return responseInfo;
        }
        return null;
        /**
        return JsonStatus.failureResult("{\"success\":false," +
                "\"message\":\"login failed\"," +
                "\"user\":{\"name\":\"" + responseInfo.get("name") +
                "\",\"id\":\"" + responseInfo.get("user_id") + "\"}}");
         **/
    }

    @Override
    public boolean checkLoginToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        Claims claims = msJwtService.parseJwt_RS256(token);
        if (claims != null) {
            // 添加超时验证逻辑

            return true;
        }
        /**
        String loginName = (String)claims.get(PermissionConstant.SYS_CURRENT_USER_LOGIN_NAME);
        String catchLoginName = cacheManager.get(token);
        if (loginName != null && !loginName.isEmpty() && loginName.equals(catchLoginName)) {
            return true;
        }
         **/
        return false;
    }
}
