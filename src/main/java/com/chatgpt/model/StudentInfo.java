package com.chatgpt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class StudentInfo {
    @Id
    private String userId;
    private String username;
    private String phone;
    private String role;


}