package com.kalix.fabric8.kongclient.biz.kong.api.plugin.authentication;

/**
 * Created by vaibhav on 15/06/17.
 */
public interface BasicAuthService {
    void addCredentials(String consumerIdOrUsername, String username, String password);
}
