package com.kalix.trying.event.dao;

import com.kalix.framework.core.impl.dao.GenericDao;
import com.kalix.trying.event.api.dao.IEventBeanDao;
import com.kalix.trying.event.entities.EventBean;
import org.osgi.service.component.annotations.Component;

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
@Component
public class EventBeanDaoImpl extends GenericDao<EventBean, Long> implements IEventBeanDao {
    @Override
    @PersistenceContext(unitName = "event-cm")

    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }
}
