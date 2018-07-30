package com.kalix.fabric8.jwt.api.biz;

import io.jsonwebtoken.Claims;

import java.util.Map;

/**
 * @author fwb
 */
public interface IJwtService {

    /**
     * 获取私钥串
     * @return
     */
    public String getPrivateKeyString();

    /**
     * 获取公钥串
     * @return
     */
    public String getPublicKeyString();

    /**
     * 创建基于RSA256的Jwt token
     * @return
     */
    public String createJwt_RS256(Map<String, Object> credentialMap);

    /**
     * 创建基于HS256的Jwt token
     * @param secret
     * @param isSecretBase64
     * @return
     */
    public String createJwt_HS256(String secret, Boolean isSecretBase64, Map<String, Object> credentialMap);

    /**
     * 解析基于RSA256的Jwt
     * @param jsonWebToken
     * @return
     */
    public Claims parseJwt_RS256(String jsonWebToken);
}
