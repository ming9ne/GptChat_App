package com.chatgpt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

@DynamicInsert
@DynamicUpdate
@Entity
@Getter
@Setter
public class UserInfo {
    @Id
    private String userId;
    private String username;
    private String phone;
    private String password;
    private String role="student";
    private Long maxToken=10000L;
    private Long usedToken=0L;

    private UserInfo(String userId, String password, String role, Long maxToken, Long usedToken) {
        this.userId = userId;
        this.password = password;
        this.role = role;
        this.maxToken =maxToken;
        this.usedToken = usedToken;
    }

    protected UserInfo() {}

    public UserInfo(Object o, String userId, String encode, String user) {
    }

    public static UserInfo createUser(String userId, String password, PasswordEncoder passwordEncoder) {
        return new UserInfo(null, userId, passwordEncoder.encode(password), "USER");
    }


}