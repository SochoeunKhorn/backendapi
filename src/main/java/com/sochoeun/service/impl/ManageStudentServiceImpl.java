package com.sochoeun.service.impl;

import com.sochoeun.exception.ResourceNotFoundException;
import com.sochoeun.model.ManageStudent;
import com.sochoeun.repository.MangeStudentRepository;
import com.sochoeun.service.ManageStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ManageStudentServiceImpl implements ManageStudentService {
    private final MangeStudentRepository mangeStudentRepository;
    @Override
    public ManageStudent create(ManageStudent student) {
        return mangeStudentRepository.save(student);
    }

    @Override
    public List<ManageStudent> list() {
        return mangeStudentRepository.findAll();
    }

    @Override
    public ManageStudent get(Integer id) {
        return mangeStudentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Content ID: %s not found".formatted(id)));
    }

    @Override
    public ManageStudent update(Integer id, ManageStudent student) {
        ManageStudent manageStudent = get(id);
        manageStudent.setName(student.getName());
        manageStudent.setDescription(student.getDescription());
        return mangeStudentRepository.save(manageStudent);
    }

    @Override
    public void delete(Integer id) {
        get(id);
        mangeStudentRepository.deleteById(id);
    }
}
