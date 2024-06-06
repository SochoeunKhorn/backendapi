package com.sochoeun.controller.user;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.Role;
import com.sochoeun.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private BaseResponse baseResponse;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Role request){
        Role role = roleService.createRole(request);
        baseResponse = new BaseResponse();
        baseResponse.success(role);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getRoles(){
        List<Role> roles = roleService.getRoleList();
        baseResponse = new BaseResponse();
        baseResponse.success(roles);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRole(@PathVariable Integer roleId){
        Role role = roleService.getRoleById(roleId);
        baseResponse = new BaseResponse();
        baseResponse.success(role);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<?> update(@PathVariable Integer roleId,@RequestBody Role request){
        Role role = roleService.updateRole(roleId,request);
        baseResponse = new BaseResponse();
        baseResponse.success(role);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> delete(@PathVariable Integer roleId){
        roleService.deleteRole(roleId);
        baseResponse = new BaseResponse();
        baseResponse.success("Role Id: %s deleted".formatted(roleId));
        return ResponseEntity.ok(baseResponse);
    }
}
