package com.kalix.trying.news.biz;

import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.util.Assert;
import com.kalix.trying.news.api.biz.INewsBeanService;
import com.kalix.trying.news.api.dao.INewsBeanDao;
import com.kalix.trying.news.entities.NewsBean;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @类描述：
 * @创建人：
 * @创建时间：
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
public class NewsBeanServiceImpl extends ShiroGenericBizServiceImpl<INewsBeanDao, NewsBean> implements INewsBeanService {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void beforeSaveEntity(NewsBean entity, JsonStatus status) {
        String userName = shiroService.getCurrentUserRealName();
        Assert.notNull(userName, "用户名不能为空.");
        if (StringUtils.isNotEmpty(userName)) {
            entity.setPublishPeople(userName);
        }
        super.beforeSaveEntity(entity, status);
    }

    public void init() {
        mqtt_subscribe("sd");
//        List<NewsBean> listBean=new ArrayList<NewsBean>();
//        for(int i=0;i<100;i++){
//            NewsBean bean=new NewsBean();
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
        List<NewsBean> beans=dao.findByNativeSql(str,NewsBean.class);
        System.out.println(beans);

    }

    @Override
    public JsonData getAllEntityByQuery(Integer page, Integer limit, String jsonStr, String sort) {
        return super.getAllEntityByQuery(page, limit, jsonStr, sort);
    }
}

