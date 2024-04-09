package com.genest.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genest.app.entity.SentMail;

public interface SentMailRepository extends JpaRepository<SentMail, Long> {
}
