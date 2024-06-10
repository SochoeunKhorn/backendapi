package com.sochoeun.service;

import com.sochoeun.model.ManageStudent;

import java.util.List;

public interface ManageStudentService {
    ManageStudent create(ManageStudent student);
    List<ManageStudent> list();
    ManageStudent get(Integer id);
    ManageStudent update(Integer id,ManageStudent student);
    void delete(Integer id);
}
