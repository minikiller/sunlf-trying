package com.kalix.fabric8.kongclient.biz.kong.api.admin;

import com.kalix.fabric8.kongclient.biz.kong.model.admin.sni.Sni;
import com.kalix.fabric8.kongclient.biz.kong.model.admin.sni.SniList;

/**
 * Created by vaibhav on 13/06/17.
 */
@Deprecated
public interface SniService {
    Sni createSni(Sni request);
    Sni getSni(String name);
    Sni updateSni(String name, Sni request);
    Sni createOrUpdateSni(Sni request);
    Sni deleteSni(String name);
    SniList listSnis();
}
