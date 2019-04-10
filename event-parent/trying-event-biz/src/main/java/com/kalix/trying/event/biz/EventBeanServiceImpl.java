package com.kalix.trying.event.biz;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.Assert;
import com.kalix.trying.event.api.biz.IEventBeanService;
import com.kalix.trying.event.api.dao.IEventBeanDao;
import com.kalix.trying.event.entities.EventBean;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.osgi.annotation.bundle.Requirement;
import org.osgi.service.component.annotations.Component;

import java.util.List;

/**
 * @类描述：
 * @创建人：
 * @创建时间：
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
@Component(service = IEventBeanService.class, property = {"osgi.jaxrs.resource=true"})

@Requirement(
        filter = "(osgi.jaxrs.name=aries.shiro.authc)",
        namespace = "osgi.service"
)
@Requirement(
        filter = "(osgi.jaxrs.name=aries.shiro.authz)",
        namespace = "osgi.service"
)
public class EventBeanServiceImpl extends ShiroGenericBizServiceImpl<IEventBeanDao, EventBean> implements IEventBeanService {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void beforeSaveEntity(EventBean entity, JsonStatus status) {
        String userName = shiroService.getCurrentUserRealName();
        Assert.notNull(userName, "用户名不能为空.");
        if (StringUtils.isNotEmpty(userName)) {
            entity.setPublishPeople(userName);
        }
        super.beforeSaveEntity(entity, status);
    }

    public void init() {
        mqtt_subscribe("sd");
//        List<EventBean> listBean=new ArrayList<EventBean>();
//        for(int i=0;i<100;i++){
//            EventBean bean=new EventBean();
//            bean.setContent("is number"+String.valueOf(i));
//            listBean.add(bean);
//        }
//        dao.addBatch(listBean);
    }


    @Override
    public void mqtt_publish(String msg) {

    }

    @Override
    public void mqtt_subscribe(String topic) {
        String str="SELECT * FROM trying_news WHERE trying_news ==> 'my_content:中国'";
        List<EventBean> beans=dao.findByNativeSql(str, EventBean.class);
        System.out.println(beans);

    }

    @Override
    @RequiresRoles("testd")
//    @RequiresAuthentication
    public JsonData getAllEntityByQuery(Integer page, Integer limit, String jsonStr, String sort) {
        return super.getAllEntityByQuery(page, limit, jsonStr, sort);
    }

    @Override
    @RequiresRoles("testd")
    public void doSave(EventBean entity, JsonStatus jsonStatus) {
        super.doSave(entity, jsonStatus);
    }
}

