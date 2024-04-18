package com.chatgpt.service;

import com.chatgpt.model.StudentInfo;
import com.chatgpt.model.UserInfo;
import com.chatgpt.repository.UserRepository;
import com.chatgpt.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // 사용자 조회 메소드
    public StudentInfo getUserByIdPhoneUsername(String userId, String phone, String username) {
        return studentRepository.findByUserIdAndPhoneAndUsername(userId, phone, username);
    }

    // 사용자 등록 메소드
    public void createUser(UserInfo userInfo) {

        String encodedPassword = passwordEncoder.encode(userInfo.getPassword());
        userInfo.setPassword(encodedPassword);
        userRepository.save(userInfo);
    }

    // 사용자 중복 체크
}
