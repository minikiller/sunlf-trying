package com.kalix.fabric8.kongclient.biz.kong.internal.plugin.authentication;

import com.kalix.fabric8.kongclient.biz.kong.model.plugin.authentication.oauth2.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

/**
 * Created by fanhua on 2017-08-07.
 *
 * Attention:
 * According to the @Path regulation, the @Path parameter name must match \{([a-zA-Z][a-zA-Z0-9_-]*)\
 * Which means, you cannot input slash "/" inside the content of "api_uri"
 */
public interface RetrofitOAuth2ProcessService {


    // OAuth2 Process ---------------------------------------------------------------------------------------------------

    @POST("/{api_uri}/oauth2/authorize")
    Call<Map<String, Object>> authorize(@Path(value = "api_uri", encoded = true) String apiUri, @Body AuthorizationRequest request);


    @POST("/{api_uri}/oauth2/token")
    Call<Token> grantToken(@Path(value = "api_uri", encoded = true) String apiUri, @Body GrantTokenRequest request);

}
