package com.kalix.trying.event.api.biz;

import com.kalix.framework.core.api.biz.IBizService;
import com.kalix.trying.event.entities.EventBean;

/**
 * @类描述：应用服务接口.
 * @创建人：
 * @创建时间：
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public interface IEventBeanService extends IBizService<EventBean> {
    //在此添加新的业务方法
    void mqtt_publish(String msg);
    void mqtt_subscribe(String topic);
}
