package com.chatgpt.controller;

import com.chatgpt.model.StudentInfo;
import com.chatgpt.model.UserInfo;
import com.chatgpt.repository.UserRepository;
import com.chatgpt.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.chatgpt.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@Controller
public class UserController {



    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder 주입


    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
        // 사용자 이름과 비밀번호로 사용자 조회
        UserInfo matchedUserInfo = userRepository.findByUserId(userInfo.getUserId());

        if (matchedUserInfo != null && passwordEncoder.matches(userInfo.getPassword(), matchedUserInfo.getPassword())) {
            // 패스워드 일치시 로그인 성공 처리
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", matchedUserInfo);

            // 성공 시 메인 페이지로 리디렉션
            response.setHeader("Location", "/main");
            return ResponseEntity.status(HttpStatus.FOUND).body("로그인 성공");
        } else {
            // 로그인 실패
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

    @GetMapping("/api/userInfo")
    @ResponseBody
    public UserInfo getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserInfo loggedInUser = (UserInfo) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            System.out.println("세션에 저장된 사용자 정보: " + loggedInUser.getUserId() + ", " + loggedInUser.getUsername());
        } else {
            System.out.println("세션에 저장된 사용자 정보 없음");
        }

        return loggedInUser;
    }



    //회원등록

    @PostMapping("/api/checkUser")
    public ResponseEntity<String> checkUser(@RequestBody StudentInfo user) {
        // 입력된 id, phone, username으로 사용자 조회
        StudentInfo existingUser = userService.getUserByIdPhoneUsername(user.getUserId(), user.getPhone(), user.getUsername());
        if (existingUser != null) {
            // 사용자가 존재하면 200 OK 반환
            return ResponseEntity.ok("User exists");
        } else {
            // 사용자가 존재하지 않으면 404 Not Found 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    @PostMapping("/api/signup")
    public ResponseEntity<String> signUp(@RequestBody UserInfo userInfo) {
        // 사용자 등록
        userService.createUser(userInfo);
        // 등록 성공 시 200 OK 반환
        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping("/api/userList")
    public List<UserInfo> getUserList() {
        // 모든 사용자 정보를 데이터베이스에서 가져옴
        List<UserInfo> userList = userRepository.findAll();
        return userList;
    }

    // 계정 삭제
    @PostMapping("/api/deleteUsers")
    public ResponseEntity<String> deleteUsers(@RequestBody List<String> userIds) {
        // 선택된 사용자를 삭제
        try {
            for (String userId : userIds) {
                userRepository.deleteById(userId);
            }
            return ResponseEntity.ok("사용자 삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 삭제 실패: " + e.getMessage());
        }
    }

    @PostMapping("/api/saveTokenLimits")
    public ResponseEntity<String> saveTokenLimits(@RequestBody Map<String, String> tokenLimits) {
        try {
            for (String userId : tokenLimits.keySet()) {
                String limit = tokenLimits.get(userId);
                System.out.println("사용자 ID: " + userId + ", 토큰 제한 설정 값: " + limit);

                UserInfo userInfo = userRepository.findById(userId).orElse(null);
                if (userInfo != null) {
                    userInfo.setMaxToken(Long.valueOf(limit));
                    System.out.println("사용자 정보 업데이트: " + userInfo);

                    userRepository.save(userInfo);
                    System.out.println("사용자 정보 저장 완료: " + userInfo);
                } else {
                    System.out.println("사용자 정보를 찾을 수 없음: " + userId);
                }
            }
            return ResponseEntity.ok("토큰 제한 설정이 저장되었습니다.");
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 제한 설정 저장 실패: " + e.getMessage());
        }
    }


}