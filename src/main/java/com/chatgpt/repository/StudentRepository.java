package com.chatgpt.repository;

import com.chatgpt.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends JpaRepository<StudentInfo, Long> {

    StudentInfo findByUserIdAndPhoneAndUsername(String userId, String phone, String username);


}
