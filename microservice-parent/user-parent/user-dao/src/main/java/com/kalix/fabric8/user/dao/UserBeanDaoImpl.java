package com.kalix.fabric8.user.dao;

import com.kalix.fabric8.user.api.dao.IUserBeanDao;
import com.kalix.fabric8.user.entities.UserBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 14-1-16.
 */

public class UserBeanDaoImpl extends UserEntityDao<UserBean,Long> implements IUserBeanDao { //extends BaseAdminDao<UserBean, Long> implements IUserBeanDao {
    //private final String className = UserBean.class.getName();

    @Override
    @PersistenceContext(unitName = "admin-core-unit")
    public void setEntityManager(EntityManager em) {
        super.setEntityManager(em);
    }

//    @Override
//    public JsonData getUserList(int page,int limit) {
//        return super.getAll(page, limit);
//    }

//    @Override
//    public UserBean saveUser(UserBean user) {
//        return super.save(user);
//    }

//    @Override
//    public void removeUser(Long userId) {
//        super.remove(userId);
//    }
//
//    @Override
//    public UserBean getUser(Long userId) {
//        return super.get(userId);
//    }

//    @Transactional(Transactional.TxType.SUPPORTS)
//    public UserBean getUser(String username) {
//        UserBean user = this.findUnique("select u from UserBean u where u.loginName=?1", username);
//        return user;
//    }

//    @Override
//    public void updateUserLoginInfo(long id, String loginIp) {
//        this.update("update UserBean u set u.loginIp=?1, u.loginDate=?2 where u.id = ?3", loginIp, new Date(), id);
//    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UserBean> findByUserId(List<Long> userId, boolean contain) {
        if (userId != null && !userId.isEmpty()) {
            String s = userId.toString();
            String sql = s.substring(1, s.length() - 1);

            if (contain) {
                return this.find("select t from UserBean t where t.id in (" + sql + ") order by t.name");
            } else {
                return this.find("select t from UserBean t where t.id not in (" + sql + ") order by t.name");
            }
        } else {
            if (contain) {
                return new ArrayList<>();
            } else {
                return this.getAll();
            }
        }
    }

    /*@Override
    public CriteriaQuery buildCriteriaQuery(QueryDTO queryDTO) {
        UserDTO userDTO = (UserDTO) queryDTO;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserBean> criteriaQuery = criteriaBuilder.createQuery(UserBean.class);
        Root<UserBean> from = criteriaQuery.from(UserBean.class);
        EntityType<UserBean> userBean_ = from.getModel(); //实体元数据
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        if (userDTO.getName() != null && !userDTO.getName().trim().isEmpty()) {
            SingularAttribute<UserBean, String> name = (SingularAttribute<UserBean, String>) userBean_.getSingularAttribute("name");
            predicatesList.add(criteriaBuilder.like(from.get(name), "%" + userDTO.getName() + "%"));
        }
        if (userDTO.getLoginName() != null && !userDTO.getLoginName().trim().isEmpty()) {
            SingularAttribute<UserBean, String> loginName = (SingularAttribute<UserBean, String>) userBean_.getSingularAttribute("loginName");
            predicatesList.add(criteriaBuilder.like(from.get(loginName), "%" + userDTO.getLoginName() + "%"));
        }
        if (userDTO.getAvailable() != -1) {
            SingularAttribute<UserBean, Integer> available = (SingularAttribute<UserBean, Integer>) userBean_.getSingularAttribute("available");
            predicatesList.add(criteriaBuilder.equal(from.get(available), userDTO.getAvailable()));
        }
        criteriaQuery.where(predicatesList.toArray(new Predicate[predicatesList.size()]));
        CriteriaQuery select = criteriaQuery.select(from);
        return select;
    }*/


}
