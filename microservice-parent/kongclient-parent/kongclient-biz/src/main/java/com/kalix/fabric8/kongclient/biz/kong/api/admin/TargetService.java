package com.kalix.fabric8.kongclient.biz.kong.api.admin;

import com.kalix.fabric8.kongclient.biz.kong.model.admin.aim.Target;
import com.kalix.fabric8.kongclient.biz.kong.model.admin.aim.TargetList;

/**
 * Created by vaibhav on 13/06/17.
 */
@Deprecated
public interface TargetService {
    Target createTarget(String upstreamNameOrId, Target request);
    Target deleteTarget(String upstreamNameOrId, String target);
    TargetList listTargets(String upstreamNameOrId, String id, Integer weight, String target, Long size, String offset);
    TargetList listActiveTargets(String upstreamNameOrId);
}
