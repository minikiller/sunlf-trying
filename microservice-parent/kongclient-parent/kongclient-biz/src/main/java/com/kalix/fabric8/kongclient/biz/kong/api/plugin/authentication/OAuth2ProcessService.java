package com.kalix.fabric8.kongclient.biz.kong.api.plugin.authentication;

import com.kalix.fabric8.kongclient.biz.kong.model.plugin.authentication.oauth2.*;

import java.util.Map;

public interface OAuth2ProcessService {


    // OAuth2 Process ---------------------------------------------------------------------------------------------------


    Map<String, Object> authorize(String apiUri, AuthorizationRequest request);

    Token grantToken(String apiUri, GrantTokenRequest request);
}
