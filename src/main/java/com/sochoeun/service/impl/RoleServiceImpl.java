package com.sochoeun.service.impl;

import com.sochoeun.exception.NotFoundException;
import com.sochoeun.model.Role;
import com.sochoeun.repository.RoleRepository;
import com.sochoeun.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role request) {
        return roleRepository.save(request);
    }

    @Override
    public List<Role> getRoleList() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Integer roleId) {
        return roleRepository.findById(roleId).orElseThrow(()->new NotFoundException("Role",roleId));
    }

    @Override
    public Role updateRole(Integer roleId, Role request) {
        Role getRole = getRoleById(roleId);
        getRole.setName(request.getName());
        return roleRepository.save(getRole);
    }

    @Override
    public void deleteRole(Integer roleId) {
        getRoleById(roleId);
        roleRepository.deleteById(roleId);
    }
}
