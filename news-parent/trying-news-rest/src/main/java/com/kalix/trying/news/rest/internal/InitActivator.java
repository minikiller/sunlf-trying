package com.kalix.trying.news.rest.internal;

import com.kalix.framework.core.api.osgi.BaseBundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

/**
 * @author chenyanxu
 */
public class InitActivator extends BaseBundleActivator {
    private ServiceReference reference;
    private HttpService httpService;
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        super.start(bundleContext);
        reference = bundleContext.getServiceReference(HttpService.class.getName());
        httpService = (HttpService) bundleContext.getService(reference);
        httpService.registerResources("/admin", "/min", null);

    }
}

