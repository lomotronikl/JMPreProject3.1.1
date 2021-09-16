package ru.jm.jmspringboot.dao;

import ru.jm.jmspringboot.model.Role;

public interface RoleDao {
  Role getRoleAdmin();
  Role getRoleUser();
  void createRoles();
}
