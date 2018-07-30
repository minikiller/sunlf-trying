package com.kalix.fabric8.user.biz;

import com.kalix.fabric8.user.api.dao.IUserBeanDao;
import com.kalix.fabric8.user.entities.UserBean;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.util.MD5Util;
import com.kalix.fabric8.user.api.biz.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements IUserService {
    private IUserBeanDao dao;

    public void setDao(IUserBeanDao dao) {
        this.dao = dao;
    }

    @Override
    public Map<String, Object> checkLoginUser(String loginName, String password) {
        UserBean loginUser = getUserBeanByLoginName(loginName);
        if (loginUser == null) {
            return null;
        }

        Map<String, Object> responseInfo = null;
        if (loginUser.getPassword() != null && password != null &&
                loginUser.getPassword().equals(MD5Util.encode(password))) {
            responseInfo = new HashMap<>();
            responseInfo.put("user_id", loginUser.getId());
            responseInfo.put("name", loginUser.getName());
            responseInfo.put("user_name", loginUser.getLoginName());
            responseInfo.put("password", loginUser.getPassword());
            responseInfo.put("user_icon", loginUser.getIcon());
        }
        return responseInfo;
    }

    @Override
    public JsonData getAll() {
        List<UserBean> userBeanList = dao.getAll();
        JsonData jsonData = new JsonData();
        if (userBeanList != null && userBeanList.size() > 0) {
            jsonData.setTotalCount(Long.valueOf(userBeanList.size()));
            jsonData.setData(userBeanList);
        } else {
            jsonData.setTotalCount(0L);
        }
        return jsonData;
    }

    public UserBean getUserBeanByLoginName(String loginName) {
        if (loginName == null) {
            return null;
        }
        return dao.findUnique("select a from UserBean a where a.loginName = ?1", loginName);
    }

}
