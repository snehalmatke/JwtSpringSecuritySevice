package com.vst.JwtSpringSecurity.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vst.JwtSpringSecurity.model.RefreshToken;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken,Integer>{

    Optional<RefreshToken> findByToken(String token);

}
