package com.kalix.trying.news.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.trying.news.api.dao.INewsBeanDao;
import com.kalix.trying.news.entities.NewsBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @类描述：
 * @创建人：
 * @创建时间：
 * @修改人：
 * @修改时间：
 * @修改备注：
 */
//@Transactional
public class NewsBeanDaoImpl extends GenericDao<NewsBean, Long> implements INewsBeanDao {
    @Override
    @PersistenceContext(unitName = "news-cm")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}
