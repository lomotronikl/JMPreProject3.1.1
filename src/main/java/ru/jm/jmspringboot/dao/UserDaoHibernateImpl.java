package ru.jm.jmspringboot.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import ru.jm.jmspringboot.model.Role;
import ru.jm.jmspringboot.model.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoHibernateImpl  implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveUser(User user){
        entityManager.persist(user);
    }

    public void delete(User user) {
        entityManager.remove(user);
    }

    public User findOne(final long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void removeUserById(long id) {
        delete(findOne(id));
    }

    @Override
    public List<User> getAllUsers() {
                  return entityManager.createQuery("from " + User.class.getName()).getResultList();
    }

    @Override
    public User getUser(long id) {
        return   findOne(id);
    }

    @Override
    public  void updateUser(User user){
       entityManager.merge(user);
    }

    @Override
    public void setRoles(Set<Role> roles, User user) {
        user.setRoles(roles);
        entityManager.merge(user);
    }

    @Override
    public User findByUsername(String userName) {
        try {
            Query query = entityManager.createQuery("select u from User u where u.username = :userName", User.class);
            query.setParameter("userName", userName);
            User user = (User) query.getSingleResult();

            return user;
        }catch (Exception ex) {
            System.out.println("Error:");
            System.out.println("user " + userName);
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
