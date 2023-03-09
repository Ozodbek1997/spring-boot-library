package com.beck.springbootlibrary.repo;

import com.beck.springbootlibrary.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;


public interface MessageRepository extends JpaRepository<Message,Long> {

        Page<Message> findAllByUserEmail(@RequestParam("user_email") String userEmail, Pageable pageable);

}
