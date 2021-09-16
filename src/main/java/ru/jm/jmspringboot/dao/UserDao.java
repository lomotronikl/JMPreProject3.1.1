package ru.jm.jmspringboot.dao;

import ru.jm.jmspringboot.model.Role;
import ru.jm.jmspringboot.model.User;
import java.util.List;
import java.util.Set;

public interface UserDao {
    void saveUser(User user);
    void removeUserById(long id);
    List<User> getAllUsers();
    User getUser(long id);
    void updateUser( User user);
    void setRoles(Set<Role> roles, User user);
    User findByUsername(String userName);
}
