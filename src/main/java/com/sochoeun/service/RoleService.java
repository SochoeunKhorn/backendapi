package com.sochoeun.service;

import com.sochoeun.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role request);
    List<Role> getRoleList();
    Role getRoleById(Integer roleId);
    Role updateRole(Integer roleId,Role request);
    void deleteRole(Integer roleId);
}
