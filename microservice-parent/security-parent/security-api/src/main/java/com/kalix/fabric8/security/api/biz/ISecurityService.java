package com.kalix.fabric8.security.api.biz;

import java.util.Map;

public interface ISecurityService {

    public Map<String, Object> doLogin(String loginName, String password);

    public boolean checkLoginToken(String token);

}