package com.kalix.trying.event.biz;

import com.kalix.framework.core.impl.biz.GenericBizServiceImpl;
import com.kalix.trying.event.api.biz.IEventBeanService;
import com.kalix.trying.event.api.dao.IEventBeanDao;
import com.kalix.trying.event.entities.EventBean;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.ws.rs.Path;
/**
 * @类描述：
 * @创建人：
 * @创建时间：
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
@Component(service = IEventBeanService.class, property = {"osgi.jaxrs.resource=true"})
@Path("/event")
public class EventBeanServiceImpl extends GenericBizServiceImpl<IEventBeanDao, EventBean> implements IEventBeanService {
    @Reference
    private IEventBeanDao dao;

    @Activate
    public void activate(){
        this.setDao(dao);
    }

    @Override
    public void mqtt_publish(String msg) {

    }

    @Override
    public void mqtt_subscribe(String topic) {
    }
}


