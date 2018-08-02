package com.kalix.fabric8.rest.test;

import com.kalix.fabric8.rest.APIRequest;
import com.kalix.fabric8.rest.APIResponse;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.core.MediaType;

/**
 * Demo about how to use this framework
 * <p/>
 * Need store some some APIs configuration in a env.properties files
 *
 * @author Carl Ji
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestHTTPRequests extends APITest {
    //login cookie token
    private String token;
    //insert entity id return by tag
    private static long id;
    //insert delete update status
    private boolean succeed;

    /**
     * test for getAll method
     */
    @Test
    public void testGETHttpRequest() {
        String uri = getValue("getall.uri");
        APIResponse response = APIRequest.GET(uri)
                // .header("AccessToken", "JSESSIONID=" + token)
                // .header("Authorization", "Bearer " + accessToken)
                .header("AccessToken", accessToken)
                .invoke().assertStatus(200);
        String returnString = response.getBody(String.class);
        JSONObject jsonObject = new JSONObject(returnString);
        Assert.assertNotNull(jsonObject);
    }

}
