package com.vst.JwtSpringSecurity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vst.JwtSpringSecurity.dto.OtpRequestDto;

@Repository
public interface OtpRepository extends MongoRepository<OtpRequestDto, String>{

	OtpRequestDto findByPhoneNumber(String phoneNumber);

    List<OtpRequestDto> findByExpiryTimeBefore(LocalDateTime expiryTime);


	List<OtpRequestDto> findByPhoneNumberAndOtp(String phoneNumber, int otp);
	
}
