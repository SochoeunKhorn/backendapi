package com.sochoeun.service.impl;

import com.sochoeun.exception.RoleNotFoundException;
import com.sochoeun.model.Role;
import com.sochoeun.model.User;
import com.sochoeun.model.request.UserRequest;
import com.sochoeun.model.response.UserResponse;
import com.sochoeun.repository.RoleRepository;
import com.sochoeun.repository.UserRepository;
import com.sochoeun.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.upload.server.path}"+"/users/")
    String clientPath;
    @Override
    public List<UserResponse> getUsers(String firstname) {
        List<User> all;
        if(firstname == null){
           all = userRepository.findAll();
        }else {
          all =  userRepository.findAllByFirstnameContainingIgnoreCase(firstname);
        }


        List<UserResponse> userResponses = new ArrayList<>();
        all.forEach(user -> {
            String status = "";
          //  log.info("Status {}", user.getStatus());
            if (user.getStatus()) {
                status = "ACTIVE";
            } else {
                status = "DISABLE";
            }
            List<String> strRole = new ArrayList<>();
            List<Role> roles = user.getRoles();
            roles.forEach(role -> {
                strRole.add(role.getName());
            });
            UserResponse response =UserResponse.builder()
                    .id(user.getId())
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .profile(user.getProfile())
                    .status(status)
                    .roles(strRole)
                    .build();
            userResponses.add(response);
        });
       // log.info("Response:{}",userResponses);
        return userResponses;
    }

    @Override
    public User getUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User ID: %s not found".formatted(userId)));

        return user;
    }

    @Override
    public void disableUser(Integer userId) {
        User user = getUser(userId);
        user.setStatus(false);
        userRepository.save(user);
    }

    @Override
    public void enableUser(Integer userId) {
        User user = getUser(userId);
        user.setStatus(true);
        userRepository.save(user);
    }

    @Override
    public void updateUser(Integer userId, UserRequest request) {
        User user = getUser(userId);

        List<String> strRole =request.getRoles();
        List<Role> roles = new ArrayList<>();
        for (String role:strRole){
            Role getRole = roleRepository.findByName(role).orElseThrow(
                    () -> new RoleNotFoundException(role));
            roles.add(getRole);
        }

        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProfile(request.getProfile());
        user.setRoles(roles);

        userRepository.save(user);
    }

    @Override
    public String uploadProfile(Integer userId, MultipartFile file) {
        User user = getUser(userId);
        String getProfileName =(user.getFirstname()+ "-" +user.getLastname()).toLowerCase();
        String photoUrl = photoFunction.apply(getProfileName,file);
        user.setProfile(photoUrl);
        userRepository.save(user);
        return photoUrl;
    }

    private final Function<String,String> fileExtension =
            filename -> Optional.of(filename)
                    .filter(name -> name.contains("."))
                    .map(name
                            -> "." + name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String,MultipartFile,String> photoFunction = (id,image) ->{
        try{
            Path fileStorageLocation = Paths.get(clientPath).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(
                    image.getInputStream(),
                    fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), // filename
                    REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath() // localhost:8080
                    .path("/api/users/profile/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        }catch (Exception e){
            throw new RuntimeException("Unable to save image");
        }
    };

}
