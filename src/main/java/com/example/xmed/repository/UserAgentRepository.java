package com.example.xmed.repository;

import com.example.xmed.entity.UserAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserAgentRepository extends JpaRepository<UserAgent, Long> {

    Optional<UserAgent> findByUser_IdAndDeletedStatusFalse(Long user_id);

//    @Query("select * from user_agent where user_id =: user_id and ")
//    Optional<UserAgent> findByUser_Id(Long user_id);
}
