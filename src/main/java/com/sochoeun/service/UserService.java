package com.sochoeun.service;

import com.sochoeun.model.User;
import com.sochoeun.model.request.ChangePasswordRequest;
import com.sochoeun.model.request.UserRequest;
import com.sochoeun.model.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface UserService {
    List<UserResponse> getUsers(String firstname);
    User getUser(Integer userId);
    void disableUser(Integer userId);
    void enableUser(Integer userId);

    void updateUser(Integer userId, UserRequest request);

    String uploadProfile(Integer userId, MultipartFile file);

    void changePassword(ChangePasswordRequest request, Principal principal);
}
