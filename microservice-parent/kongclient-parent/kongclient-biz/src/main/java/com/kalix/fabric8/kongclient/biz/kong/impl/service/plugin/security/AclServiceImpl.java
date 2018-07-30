package com.kalix.fabric8.kongclient.biz.kong.impl.service.plugin.security;

import com.kalix.fabric8.kongclient.biz.kong.api.plugin.security.AclService;
import com.kalix.fabric8.kongclient.biz.kong.exception.KongClientException;
import com.kalix.fabric8.kongclient.biz.kong.internal.plugin.security.RetrofitAclService;
import com.kalix.fabric8.kongclient.biz.kong.model.plugin.security.acl.Acl;
import com.kalix.fabric8.kongclient.biz.kong.model.plugin.security.acl.AclList;

import java.io.IOException;

/**
 * Created by vaibhav on 18/06/17.
 *
 * Updated by fanhua on 2017-08-07.
 *
 * Upated by dvilela on 22/10/17.
 */
public class AclServiceImpl implements AclService {

    private RetrofitAclService retrofitAclService;

    public AclServiceImpl(RetrofitAclService retrofitAclService) {
        this.retrofitAclService = retrofitAclService;
    }
    @Override
    public void associateConsumer(String usernameOrId, String group) {
        try {
            retrofitAclService.associateConsumer(usernameOrId, new Acl(group)).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }

    @Override
    public AclList listAcls(String usernameOrId, Long size, String offset) {
        try {
            return retrofitAclService.listAcls(usernameOrId, size, offset).execute().body();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
