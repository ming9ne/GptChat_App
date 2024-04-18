package com.chatgpt.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Table(name="chatt_history_tbl")
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class ChattHistory {
    private final static SimpleDateFormat datefmt = new SimpleDateFormat("yyyMMddHHmmss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    private String userId;

    @Column(length = 14)
    private String createDate;

    private Integer token;

    @Column(columnDefinition = "TEXT")
    private String rqst;

    @Column(columnDefinition = "TEXT")
    private String resp;

    //
    public ChattHistory(String userid, int token, String rqst, String resp) {
        this.userId = userid;
        this.token = token;
        this.rqst = rqst;
        this.resp = resp;
        this.createDate = datefmt.format(new Date());
    }
}