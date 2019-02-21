package com.kalix.fabric8.rest.test;

import com.kalix.fabric8.rest.APIRequest;
import com.kalix.fabric8.rest.APIResponse;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestKongClient
        extends APITest
{
    @Test
    public void testCreateAPIGateWayHS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/create_HS256")
                .header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testGetJwtTokenHS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/token_HS256")
                .header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }


    @Test
    public void testGetJwtResult() {

        APIResponse resp = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/token_RS256")
                .header("AccessToken", accessToken)
                .invoke().assertStatus(200);
        String returnString = resp.getBody(String.class);

        String authorization = "Bearer " + returnString.substring(1, returnString.length()-1);
        String uri = "http://192.168.1.28:8000/rest/ms_users";
        APIResponse response = APIRequest.GET(uri)
                .header("AccessToken", accessToken)
                .header("Authorization", authorization)
                .invoke().assertStatus(200);
        String bodyString = response.getBody(String.class);
        System.out.println(bodyString);
    }

    @Test
    public void testDeleteAPIGateWayHS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/clear_HS256")
                //.header("Cookie", "JSESSIONID=" + token)
                .header("Cookie", "access_token=" + accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testCreateAPIGateWayRS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/create_RS256")
                .header("AccessToken", accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testGetJwtTokenRS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/token_RS256")
                .header("AccessToken", accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }

    @Test
    public void testDeleteAPIGateWayRS256() {
        APIResponse response = APIRequest.GET("http://localhost:8181/camel/rest/kongJwts/clear_RS256")
                //.header("Cookie", "JSESSIONID=" + token)
                //.header("Cookie", "access_token=" + accessToken)
                .header("AccessToken", accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        System.out.println(returnString);
    }
}
