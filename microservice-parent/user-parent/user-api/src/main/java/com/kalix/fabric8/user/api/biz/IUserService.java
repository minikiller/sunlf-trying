package com.kalix.fabric8.user.api.biz;

import com.kalix.framework.core.api.persistence.JsonData;

import java.util.Map;

public interface IUserService {
    public Map<String, Object> checkLoginUser(String loginName, String password);

    public JsonData getAll();
}
