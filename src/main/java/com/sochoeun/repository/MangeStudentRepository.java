package com.sochoeun.repository;

import com.sochoeun.model.ManageStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MangeStudentRepository extends JpaRepository<ManageStudent,Integer> {
}
