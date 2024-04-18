package com.chatgpt.repository;


import com.chatgpt.model.UserInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
    UserInfo findByUserIdAndPassword(String UserId, String password);

    UserInfo findByUserId(String UserId);
    //

    @Transactional
    //@Modifying(clearAutomatically = true)	// 1차 캐시 clear
    //@Query(value = "update user_tbl u set u.used_token = u.used_token + ?2 where u.user_id = ?1",nativeQuery = true)
    @Modifying()
    @Query(value = "update UserInfo u set u.usedToken = u.usedToken + ?2 where u.userId = ?1")
    int UpdateUsedToken(String userid, Long ntoken);

    @Transactional
    @Modifying()
    @Query(value = "update UserInfo u set u.password = ?2 where u.userId = ?1")
    int UpdatePassword(String userid, String password);

}
