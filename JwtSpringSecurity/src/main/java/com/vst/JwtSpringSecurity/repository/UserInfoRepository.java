package com.vst.JwtSpringSecurity.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vst.JwtSpringSecurity.dto.UserInfo;

public interface UserInfoRepository extends MongoRepository<UserInfo, Integer> {

	Optional<UserInfo> findByUserFirstName(String userFirstName);

	
	UserInfo save(UserInfo userInfo);

	UserInfo findByUserEmailIgnoreCaseAndIsActiveTrue(String userEmail);

	UserInfo findByUserContactNoAndIsActiveTrue(String userContactNo);

	UserInfo findByUserId(String userId);
}
