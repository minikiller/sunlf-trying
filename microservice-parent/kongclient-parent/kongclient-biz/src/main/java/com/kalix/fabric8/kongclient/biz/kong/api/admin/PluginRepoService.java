package com.kalix.fabric8.kongclient.biz.kong.api.admin;

import com.kalix.fabric8.kongclient.biz.kong.model.admin.plugin.EnabledPlugins;

public interface PluginRepoService {

	EnabledPlugins retrieveEnabledPlugins();

	Object retrievePluginSchema(String pluginName);
}
