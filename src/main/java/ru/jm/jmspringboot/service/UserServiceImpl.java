package ru.jm.jmspringboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jm.jmspringboot.dao.RoleDao;
import ru.jm.jmspringboot.dao.UserDao;
import ru.jm.jmspringboot.model.Role;
import ru.jm.jmspringboot.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service()
@Transactional
public class UserServiceImpl implements UserService{

    UserDao dao;
    RoleDao roleDao;

    @Autowired
    public void setDao(UserDao dao) {
        this.dao = dao;
        if (dao == null) {
            System.out.println("DAO NULL!!");
        }

    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {

        this.roleDao = roleDao;
    }

    public Role getAdminRole() {
        return roleDao.getRoleAdmin();
    }

    public void createRoles() {
        //   Создаем первоначальные роли
        roleDao.createRoles();
    }

    @Override
    public void saveUser(User user){
        updateUserRoles(user, user.isRoleAdmin(), user.isRoleUser());
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        dao.saveUser(user);
    }

    @Override
    public void updateUserRoles(User user, boolean isAdmin, boolean isUser) {
        user.setRoleAdmin(isAdmin);
        user.setRoleUser(isUser);
        Set<Role> roles = new HashSet<>();
        if (isAdmin) {
            roles.add(roleDao.getRoleAdmin());
        }
        if (isUser){
            roles.add(roleDao.getRoleUser());
        }
        user.setRoles(roles);
    }

    @Override
    public void removeUserById(long id)  {
        dao.removeUserById(id);
    }

    private User userSetRolesFlag(User user) {
        for (Role role : user.getRoles()) {
            if (role.getRoleName().indexOf("ADMIN") >= 0) {
                user.setRoleAdmin(true);
            } else {
                if (role.getRoleName().indexOf("USER") >= 0) {
                    user.setRoleUser(true);
                }
            }
        }
        return user;
    }
    @Override
    public List<User> getAllUsers() {
        List<User> users = dao.getAllUsers();
        for ( User user: users ) {
             userSetRolesFlag(user);
        }
        return users;
    }

    @Override
    public User getUser(long id) {

        return  userSetRolesFlag(dao.getUser(id));
    }

    public User getUser(String userName) {
        return  dao.findByUsername(userName);
    }

    @Override
    @Transactional
    public void updateUser(User updateUser) {
       dao.updateUser(updateUser);
    }

    private User updateUserCommon(long id, User user) {
        User updateuser= getUser(id);
        updateuser.setUsername(user.getUsername());
        updateuser.setName(user.getName());
        updateuser.setLastName(user.getLastName());
        if (user.getPassword().length()>0) {
            updateuser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        return updateuser;
    }

    public void updateUserShort(long id, User user) {
        updateUser(updateUserCommon(id, user));
    }

    public void updateUser(long id, User user) {
        User updateuser= updateUserCommon(id,user);
        updateUserRoles(updateuser, user.isRoleAdmin(), user.isRoleUser());
        updateUser(updateuser);
    }

    public void setRoles(Set<Role> roles, User user) {
        user.setRoles(roles);
        updateUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("!!!!!");
        User user = dao.findByUsername(s);
        System.out.println("1 "+user.toString());
        if (user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println(user.getUsername());
        org.springframework.security.core.userdetails.User userD= new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(),user.getAuthorities());
        return userD;
    }

    @Override
    public String toString(){
        return "USImpl";
    }

}
