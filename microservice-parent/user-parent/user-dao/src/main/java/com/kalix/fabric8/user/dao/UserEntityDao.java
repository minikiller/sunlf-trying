package com.kalix.fabric8.user.dao;

import com.kalix.fabric8.user.api.dao.IUserEntityDao;
import com.kalix.fabric8.user.entities.UserEntity;
import com.kalix.framework.core.impl.dao.GenericDao;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenyanxu on 2017/2/27.
 */
public abstract class UserEntityDao<T extends UserEntity,PK extends Serializable> extends GenericDao<T,PK> implements IUserEntityDao<T,PK> {
    @Override
    public T getUser(String loginName) {
        T user= this.findUnique("select u from "+this.classSimpleName+" u where u.loginName=?1", loginName);
        return user;
    }

    @Override
    public T getUser(Long id){
        return this.get((PK) id);
    }

    @Override
    public void updateUserLoginInfo(long id, String loginIP) {
        this.update("update "+this.classSimpleName+" u set u.loginIp=?1, u.loginDate=?2 where u.id = ?3", loginIP, new Date(), id);
    }
}
