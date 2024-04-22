package com.genest.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.genest.app.entity.UserJoin;

public interface UserRepository extends JpaRepository<UserJoin, Integer> {

	// DBにemailが存在するかどうかを確認
	Boolean existsByEmail(String email);

	// emailを受け取ってDBテーブルで会員を照会するメソッド
	UserJoin findByEmail(String email);
}