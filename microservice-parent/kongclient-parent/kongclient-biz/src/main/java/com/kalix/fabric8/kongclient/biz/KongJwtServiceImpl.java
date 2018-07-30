package com.kalix.fabric8.kongclient.biz;

import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.util.ConfigUtil;
import com.kalix.fabric8.jwt.api.biz.IJwtService;
import com.kalix.fabric8.kongclient.api.biz.IKongJwtService;
import com.kalix.fabric8.kongclient.biz.kong.impl.KongClient;
import com.kalix.fabric8.kongclient.biz.kong.model.admin.api.Api;
import com.kalix.fabric8.kongclient.biz.kong.model.admin.consumer.Consumer;
import com.kalix.fabric8.kongclient.biz.kong.model.admin.plugin.Plugin;
import com.kalix.fabric8.kongclient.biz.kong.model.admin.plugin.PluginList;
import com.kalix.fabric8.kongclient.biz.kong.model.plugin.authentication.jwt.JwtConfig;
import com.kalix.fabric8.kongclient.biz.kong.model.plugin.authentication.jwt.JwtCredential;
import com.kalix.fabric8.kongclient.biz.kong.model.plugin.authentication.jwt.JwtCredentialList;

import java.util.*;

public class KongJwtServiceImpl implements IKongJwtService {
    private KongClient kongClient;
    private String KONG_ADMIN_URL = null;
    private String KONG_PROXY_URL = null;
    private String KONG_AUTH2_API_URL = null;
    private String API_UPSTREAM_URL = null;
    private String API_URIS = null;
    private String API_NAME = null;
    private String API_METHODS = null;
    private String CONSUMER_NAME = null;
    private IJwtService msJwtService;

    private Boolean needOauth2 = false;
    private final String kongConfigName = "ConfigKong";
    private final String PLUGIN_NAME = "jwt";

    private Api api;
    private Consumer consumer;
    private Plugin plugin;
    private JwtCredential credential;

    public KongJwtServiceImpl() {
        initConfigs();
        kongClient = new KongClient(KONG_ADMIN_URL, KONG_AUTH2_API_URL, needOauth2);
    }

    private void initConfigs() {
        KONG_ADMIN_URL = (String) ConfigUtil.getConfigProp("KONG_ADMIN_URL", kongConfigName);
        KONG_PROXY_URL = (String) ConfigUtil.getConfigProp("KONG_PROXY_URL", kongConfigName);
        KONG_AUTH2_API_URL = (String) ConfigUtil.getConfigProp("KONG_AUTH2_API_URL", kongConfigName);
        API_UPSTREAM_URL = (String) ConfigUtil.getConfigProp("API_UPSTREAM_URL", kongConfigName);
        API_NAME = (String) ConfigUtil.getConfigProp("API_NAME", kongConfigName);
        API_URIS = (String) ConfigUtil.getConfigProp("API_URIS", kongConfigName);
        API_METHODS = (String) ConfigUtil.getConfigProp("API_METHODS", kongConfigName);
        CONSUMER_NAME = (String) ConfigUtil.getConfigProp("CONSUMER_NAME", kongConfigName);
        if (KONG_AUTH2_API_URL != null && !KONG_AUTH2_API_URL.isEmpty()) {
            needOauth2 = true;
        }
    }

    private Api getKongAPIByName() {
        try {
            return kongClient.getApiService().getApi(API_NAME);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    private Consumer getKongConsumerByName() {
        try {
            return kongClient.getConsumerService().getConsumer(CONSUMER_NAME);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }


    private Plugin getJwtPlugin() {
        try {
            List<Plugin> plugins = new ArrayList<>();
            PluginList pluginList = kongClient.getPluginService().listPlugins(null, null, null, null, 1L, null);
            plugins.addAll(pluginList.getData());
            while (pluginList.getOffset() != null) {
                pluginList = kongClient.getPluginService().listPlugins(null, null, null, null, 1L, pluginList.getOffset());
                plugins.addAll(pluginList.getData());
            }
            for (Plugin plugin : plugins) {
                if (PLUGIN_NAME.equals(plugin.getName())) {
                    return plugin;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    @Override
    public JsonStatus createAPIGateWayHS256() {
        return createAPIGateWay("HS256");
    }

    @Override
    public JsonStatus createAPIGateWayRS256() {
        return createAPIGateWay("RS256");
    }

    private JsonStatus createAPIGateWay(String algorithm) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            api = getKongAPIByName();
            if (api == null) {
                api = createKongAPI();
            }
            consumer = getKongConsumerByName();
            if (consumer == null) {
                consumer = createKongConsumer();
            }
            plugin = getJwtPlugin();
            if (plugin == null) {
                plugin = createJwtPlugin(algorithm);
            }
            credential = getJwtCredential(algorithm);
            if (credential == null) {
                if ("RS256".equals(algorithm)) {
                    credential = createJwtCredentialRS256();
                } else {
                    credential = createJwtCredentialHS256();
                }
            }
            jsonStatus.setSuccess(true);
            jsonStatus.setMsg("create success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("create failed");
        }
        return jsonStatus;
    }

    @Override
    public JsonStatus clearAPIGateWayHS256() {
        return clearAPIGateWay("HS256");
    }

    @Override
    public JsonStatus clearAPIGateWayRS256() {
        return clearAPIGateWay("RS256");
    }

    private JsonStatus clearAPIGateWay(String algorithm) {
        JsonStatus jsonStatus = new JsonStatus();
        try {
            consumer = getKongConsumerByName();
            credential = getJwtCredential(algorithm);
            if (consumer != null && credential != null) {
                deleteJwtCredential();
            }
            plugin = getJwtPlugin();
            if (plugin != null) {
                deleteJwtPlugin();
            }
            consumer = getKongConsumerByName();
            if (consumer != null) {
                deleteKongConsumer();
            }
            api = getKongAPIByName();
            if (api != null) {
                deleteKongAPI();
            }
            jsonStatus.setSuccess(true);
            jsonStatus.setMsg("delete success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonStatus.setSuccess(false);
            jsonStatus.setMsg("delete failed");
        }
        return jsonStatus;
    }

    private void deleteJwtCredential() {
        kongClient.getJwtService().deleteCredentials(consumer.getId(), credential.getId());
    }

    private void deleteJwtPlugin() {
        kongClient.getPluginService().deletePlugin(plugin.getId());
    }

    private void deleteKongConsumer() {
        kongClient.getConsumerService().deleteConsumer(consumer.getId());
    }

    private void deleteKongAPI() {
        kongClient.getApiService().deleteApi(api.getId());
    }

    private JwtCredential createJwtCredentialHS256() {
        return kongClient.getJwtService().addCredentials(consumer.getId(), new JwtCredential());
    }

    private JwtCredential createJwtCredentialRS256() {
//        String pubkey1 = PUBLIC_KEY_START + PUBLIC_KEY + PUBLIC_KEY_END;
//        return kongClient.getJwtService().addCredentials(consumer.getId(), new JwtCredential(pubkey, "RS256"));
        String pubKey = getPublicKey();
//        System.out.println("pubkey equal:");
//        System.out.println(pubkey1 == pubKey);
        return kongClient.getJwtService().addCredentials(consumer.getId(), new JwtCredential(pubKey, "RS256"));
    }

    private JwtCredential getJwtCredential(String algorithm) {
        try {
            JwtCredentialList list = kongClient.getJwtService().listCredentials(consumer.getId(), null, null);
            if (list == null) {
                return null;
            }
            List<JwtCredential> credentials = list.getData();
            if (credentials != null && credentials.size() > 0) {
                for (JwtCredential jwtCredential : credentials) {
                    if (algorithm.equals(jwtCredential.getAlgorithm())) {
                        return jwtCredential;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Plugin createJwtPlugin(String algorithm) {
        Plugin plugin = new Plugin();
        plugin.setApiId(api.getId());
        plugin.setName(PLUGIN_NAME);
        plugin.setEnabled(true);
        plugin.setCreatedAt(System.currentTimeMillis());
        JwtConfig config = new JwtConfig();
        if ("RS256".equals(algorithm)) {
            config.setSecretIsBase64(false);
        } else {
            config.setSecretIsBase64(true);
        }
        plugin.setConfig(config);
        return kongClient.getPluginService().addPlugin(plugin);
    }

    private Consumer createKongConsumer() {
        Consumer consumer = new Consumer();
        consumer.setCustomId(UUID.randomUUID().toString());
        consumer.setUsername(CONSUMER_NAME);
        return kongClient.getConsumerService().createConsumer(consumer);
    }

    private Api createKongAPI() {
        Api request = new Api();
        request.setUris(Arrays.asList(API_URIS.split(",")));
        request.setName(API_NAME);
        request.setStripUri(false);
        request.setUpstreamUrl(API_UPSTREAM_URL);
        request.setMethods(Arrays.asList(API_METHODS.split(",")));
        return kongClient.getApiService().createApi(request);
    }

    private Api updateKongAPI(Api api) {
        Api request = new Api();
        request.setUris(api.getUris());
        request.setName(api.getName());
        request.setUpstreamUrl(api.getUpstreamUrl());
        request.setMethods(api.getMethods());
        return kongClient.getApiService().updateApi(api.getName(), request);
    }

    @Override
    public String getJwtTokenHS256() {
        try {
            Consumer consumer = getKongConsumerByName();
            JwtCredentialList list = kongClient.getJwtService().listCredentials(consumer.getId(), null, null);
            List<JwtCredential> jwtCredentials = list.getData();
            if (jwtCredentials != null && !jwtCredentials.isEmpty()) {
                for (JwtCredential jwtCredential : jwtCredentials) {
                    if ("HS256".equals(jwtCredential.getAlgorithm())) {
                        return generateJwtStringHS256(jwtCredential);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getJwtTokenRS256() {
        try {
            Consumer consumer = getKongConsumerByName();
            JwtCredentialList list = kongClient.getJwtService().listCredentials(consumer.getId(), null, null);
            List<JwtCredential> jwtCredentials = list.getData();
            if (jwtCredentials != null && !jwtCredentials.isEmpty()) {
                for (JwtCredential jwtCredential : jwtCredentials) {
                    if ("RS256".equals(jwtCredential.getAlgorithm())) {
//                        String privateKey = getPrivateKey();
                        return generateJwtStringRS256(jwtCredential);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPrivateKey() {
        return this.msJwtService.getPrivateKeyString();
    }

    private String getPublicKey() {
        return this.msJwtService.getPublicKeyString();
    }

    private String generateJwtStringHS256(JwtCredential jwtCredential) {
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("iss", jwtCredential.getKey());
        credentialMap.put("name", CONSUMER_NAME);
        credentialMap.put("iat", jwtCredential.getCreatedAt());
        return this.msJwtService.createJwt_HS256(jwtCredential.getSecret(), true, credentialMap);
    }

    private String generateJwtStringRS256(JwtCredential jwtCredential) {
        Map<String, Object> credentialMap = new HashMap<>();
        credentialMap.put("iss", jwtCredential.getKey());
        credentialMap.put("name", CONSUMER_NAME);
        credentialMap.put("iat", jwtCredential.getCreatedAt());
        return this.msJwtService.createJwt_RS256(credentialMap);
    }

    public Api getApi() {
        return api;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setMsJwtService(IJwtService msJwtService) {
        this.msJwtService = msJwtService;
    }
}
