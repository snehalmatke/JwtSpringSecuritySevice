package com.vst.JwtSpringSecurity.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vst.JwtSpringSecurity.dto.UserDto;
import com.vst.JwtSpringSecurity.model.UserInfo;


public interface UserInfoRepository extends MongoRepository<UserInfo, Integer> {

    Optional<UserInfo> findByName(String username);

	UserDto save(UserDto userDto);

}
