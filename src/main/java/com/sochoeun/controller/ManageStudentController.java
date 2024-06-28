package com.sochoeun.controller;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.ManageStudent;
import com.sochoeun.service.ManageStudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
@Tag(name = "ABOUT STUDENT INFORMATION")
public class ManageStudentController {
    private final ManageStudentService manageStudentService;
    private BaseResponse baseResponse;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ManageStudent student){
        ManageStudent manageStudent = manageStudentService.create(student);
        baseResponse = new BaseResponse();
        baseResponse.success(manageStudent);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<ManageStudent> list = manageStudentService.list();
        baseResponse = new BaseResponse();
        baseResponse.success(list);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id){
        ManageStudent list = manageStudentService.get(id);
        baseResponse = new BaseResponse();
        baseResponse.success(list);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody ManageStudent student){
        ManageStudent list = manageStudentService.update(id,student);
        baseResponse = new BaseResponse();
        baseResponse.success(list);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        manageStudentService.delete(id);
        baseResponse = new BaseResponse();
        baseResponse.success("Deleted");
        return ResponseEntity.ok(baseResponse);
    }
}
