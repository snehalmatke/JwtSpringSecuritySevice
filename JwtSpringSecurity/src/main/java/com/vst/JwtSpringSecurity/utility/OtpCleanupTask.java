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

	 @Scheduled(fixedDelay = 60_000)
	    public void cleanupExpiredOtp() {
	        LocalDateTime currentTime = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
	        List<OtpRequestDto> expiredOtpList = otpRepository.findByExpiryTimeBefore(currentTime);
	        otpRepository.deleteAll(expiredOtpList);
	    }
	 
	 
	 
}
