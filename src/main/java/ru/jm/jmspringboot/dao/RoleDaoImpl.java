package ru.jm.jmspringboot.dao;

import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import ru.jm.jmspringboot.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RoleDaoImpl implements RoleDao{
    @PersistenceContext
    private EntityManager entityManager;



    public Role getRoleAdmin() {
    Query roleQuery =  entityManager.createQuery("Select r from Role r where r.roleName=:roleName", Role.class);
    roleQuery.setParameter("roleName", "ROLE_ADMIN");
    return (Role) roleQuery.getSingleResult();
    }
    public Role getRoleUser() {
        Query roleQuery =  entityManager.createQuery("Select r from Role r where r.roleName=:roleName", Role.class);
        roleQuery.setParameter("roleName", "ROLE_USER");
        return (Role) roleQuery.getSingleResult();
    }

    public void createRoles() {
        //Создаем первоначальные роли
        Role roleAdmin = new Role();
        roleAdmin.setRoleName("ROLE_ADMIN");
        Role roleUser = new Role();
        roleUser.setRoleName("ROLE_USER");
        entityManager.persist(roleAdmin);
        entityManager.persist(roleUser);
        entityManager.flush();
    }

}
