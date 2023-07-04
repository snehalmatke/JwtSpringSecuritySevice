package com.vst.JwtSpringSecurity.serviceJwtService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vst.JwtSpringSecurity.dto.OtpRequestDto;
import com.vst.JwtSpringSecurity.repository.OtpRepository;
import com.vst.JwtSpringSecurity.utility.OtpCleanupTask;

import jakarta.annotation.PostConstruct;

@Service
public class TwilioService {
	
	 @Autowired
	    private OtpRepository otpRepository;
	 
	 @Autowired
	    private OtpCleanupTask otpCleanupTask;
	 
	 @PostConstruct
	    public void initialize() {
	        otpCleanupTask.cleanupExpiredOtp();
	    }
	
	public String getGeneratedId() {
		String number = "";
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return ft.format(dNow)+ number;

		
		
	}

	private final String accountSid = "AC2f44184946ce8bf8d8fd75aa5b777bf0";
    private final String authToken = "cd82d09da18be80ffbb0210f84c799a5";
    private final String twilioPhoneNumber = "+15417033598";
 
    @PostConstruct
    public void initTwilioClient() {
        Twilio.init(accountSid, authToken);
    }

   

    public boolean sendOtp(String phoneNumber) {
        // Check if OTP already exists for the phone number
        OtpRequestDto existingOtp = otpRepository.findByPhoneNumber(phoneNumber);
        if (existingOtp != null) {
            // OTP already exists, do not generate a new one
            System.out.println("OTP already generated for phone number: " + phoneNumber);
            return true; // Indicate that the OTP was already generated
        }

        int otp = generateOtp();
        String messageBody = "Your OTP is: " + otp;

        OtpRequestDto otpRequestDto = new OtpRequestDto(phoneNumber, otp);
        otpRequestDto.setId(getGeneratedId());
        otpRequestDto.setExpiryTime(Instant.now().plusSeconds(90)); // Set expiry time to 90 seconds from now
        otpRepository.save(otpRequestDto);

        try {
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    messageBody
            ).create();

            System.out.println("OTP sent successfully. Message SID: " + message.getSid());

            // Invoke the cleanup task to delete expired OTP data
            otpCleanupTask.cleanupExpiredOtp();;

            return true; // Indicate that the OTP was sent successfully
        } catch (Exception e) {
            // Handle the exception here (e.g., log the error, notify the administrator, etc.)
            System.out.println("Failed to send OTP: " + e.getMessage());
            return false; // Indicate that the OTP failed to send
        }
    }


  

    public boolean verifyOtp(OtpRequestDto otpRequestDto) {
        OtpRequestDto otpRequestDto2 = otpRepository.findByPhoneNumber(otpRequestDto.getPhoneNumber());
        if (otpRequestDto2 != null) {
            int otp = otpRequestDto2.getOtp();
            if (otp == otpRequestDto.getOtp()) {
                Instant currentTime = Instant.now();
                Instant otpExpiryTime = otpRequestDto2.getExpiryTime();
                if (otpExpiryTime != null && otpExpiryTime.isAfter(currentTime)) {
                    otpRepository.delete(otpRequestDto2); // Delete the OTP record
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


  
    private int generateOtp() {
        return ThreadLocalRandom.current().nextInt(1000, 9999);
    }

}
