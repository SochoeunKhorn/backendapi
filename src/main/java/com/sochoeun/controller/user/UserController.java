package com.sochoeun.controller.user;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.request.ChangePasswordRequest;
import com.sochoeun.model.request.UserRequest;
import com.sochoeun.model.response.UserResponse;
import com.sochoeun.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "USER")
public class UserController {
    private final UserService userService;
    @Value("${application.upload.server.path}"+"/user/")
    String serverPath;


    @PostMapping("/disable/{userId}")
    public ResponseEntity<?> disableUser(@PathVariable Integer userId){
        userService.disableUser(userId);
        return ResponseEntity.ok("User ID: %s disabled".formatted(userId));
    }

    @PostMapping("/enable/{userId}")
    public ResponseEntity<?> enableUser(@PathVariable Integer userId){
        userService.enableUser(userId);
        return ResponseEntity.ok("User ID: %s enabled".formatted(userId));
    }

    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId,@RequestBody UserRequest request){
        userService.updateUser(userId,request);
        return ResponseEntity.ok("User ID: %s updated".formatted(userId));
    }


    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal){
        userService.changePassword(request,principal);
        return ResponseEntity.ok("Password Changed");
    }

    @PutMapping(value = "/update/profile",consumes = "multipart/form-data")
    public ResponseEntity<String> updateUserProfile(
            @RequestParam Integer userId,
            @RequestParam MultipartFile file){
        String profile = userService.uploadProfile(userId, file);
        return ResponseEntity.ok().body(profile);
    }
    @GetMapping()
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String firstname){
        List<UserResponse> users = userService.getUsers(firstname);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.success(users);
        return ResponseEntity.ok(baseResponse);
    }

    @Hidden
    @GetMapping(path = "/profile/{filename}",produces = {IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE})
    public byte[] getProfile(@PathVariable("filename") String filename) throws Exception{
        return Files.readAllBytes(Paths.get(serverPath + filename));
    }

}
