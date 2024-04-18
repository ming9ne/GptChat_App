package com.chatgpt.repository;

import com.chatgpt.model.ChattHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattHistoryRepository extends JpaRepository<ChattHistory, Integer> {

    Page<ChattHistory> findByUserId(String userId, Pageable pagerequest);
}
