package com.kalix.fabric8.user.api.dao;


import com.kalix.fabric8.user.entities.UserEntity;
import com.kalix.framework.core.api.dao.IGenericDao;

import java.io.Serializable;

/**
 * Created by chenyanxu on 2017/2/27.
 */
public interface IUserEntityDao<T extends UserEntity,PK extends Serializable> extends IGenericDao<T,PK> {
    T getUser(String loginName);
    T getUser(Long id);
    void updateUserLoginInfo(long id, String loginIP);
}
