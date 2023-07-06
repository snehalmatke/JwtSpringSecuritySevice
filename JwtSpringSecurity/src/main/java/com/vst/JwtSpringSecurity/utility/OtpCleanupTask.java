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
	     LocalDateTime currentTime = LocalDateTime.now().minus(4, ChronoUnit.MINUTES);
	     List<OtpRequestDto> expiredOtpList = otpRepository.findByExpiryTimeBefore(currentTime);
	     for (OtpRequestDto otp : expiredOtpList) {
	         // Set the fields to be deleted to null or default values
	         otp.setOtp(0); // Set the otp field to 0 (or any default value)
	         otp.setExpiryTime(null); // Set the expiryTime field to null
	         otp.setAttempts(0); // Set the attempts field to 0 (or any default value)
	         otp.setCurrentTime(null); // Set the currentTime field to null

	         // Save the updated OTP object
	         otpRepository.save(otp);
	     }
	 }

	 
}
