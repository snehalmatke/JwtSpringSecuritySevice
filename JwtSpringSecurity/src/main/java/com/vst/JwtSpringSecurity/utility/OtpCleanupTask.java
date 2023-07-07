package com.vst.JwtSpringSecurity.utility;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vst.JwtSpringSecurity.dto.OtpRequestDto;
import com.vst.JwtSpringSecurity.repository.OtpRepository;

@Component
public class OtpCleanupTask {

	 @Autowired
	    private OtpRepository otpRepository;

//	 @Scheduled(fixedDelay = 60_000)
//	    public void cleanupExpiredOtp() {
//	        LocalDateTime currentTime = LocalDateTime.now().minus(4, ChronoUnit.MINUTES);
//	        List<OtpRequestDto> expiredOtpList = otpRepository.findByExpiryTimeBefore(currentTime);
//	        otpRepository.deleteAll(expiredOtpList);
//	    } 
//	 
	 
	 @Scheduled(fixedDelay = 60_000) // Run every 4 minutes (240,000 milliseconds)
	 public void cleanupExpiredOtp() {
	     LocalDateTime currentTime = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
	     List<OtpRequestDto> expiredOtpList = otpRepository.findByExpiryTimeBefore(currentTime);
	     for (OtpRequestDto otp : expiredOtpList) {
	        
	         otp.setOtp(0);
	         otp.setExpiryTime(null); 
	         otp.setAttempts(0); 
	         otp.setCurrentTime(null); 

	         
	         otpRepository.save(otp);
	     }
	 }

	 
}
