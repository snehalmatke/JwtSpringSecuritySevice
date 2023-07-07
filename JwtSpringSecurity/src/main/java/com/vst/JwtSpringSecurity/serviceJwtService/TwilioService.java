package com.vst.JwtSpringSecurity.serviceJwtService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Date;

import java.util.concurrent.ThreadLocalRandom;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vst.JwtSpringSecurity.dto.OtpRequestDto;
import com.vst.JwtSpringSecurity.dto.UserInfo;
import com.vst.JwtSpringSecurity.repository.OtpRepository;
import com.vst.JwtSpringSecurity.repository.UserInfoRepository;
import com.vst.JwtSpringSecurity.utility.OtpCleanupTask;

import jakarta.annotation.PostConstruct;

@Service
public class TwilioService {

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private OtpCleanupTask otpCleanupTask;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	UserInfoRepository userRepository;

	@PostConstruct
	public void initialize() {
		otpCleanupTask.cleanupExpiredOtp();
	}

	public String getGeneratedId() {
		String number = "";
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return ft.format(dNow) + number;

	}

	private final String accountSid = "AC1940620aaf67cf569091060591a74704";
	private final String authToken = "bf502836a3f830f150db8822b36672d2";
	private final String twilioPhoneNumber = "+18145266534";

	@PostConstruct
	public void initTwilioClient() {
		Twilio.init(accountSid, authToken);
	}
	

    public boolean sendOtp(String phoneNumber) {
        // Check if OTP already exists for the phone number
        OtpRequestDto existingOtp = otpRepository.findByPhoneNumber(phoneNumber);
        if (existingOtp != null) {
            int attempts = existingOtp.getAttempts();
            if (attempts >= 2) {
                System.out.println("OTP already sent. Please try again later.");
                return false; // Indicate that OTP sending is not allowed
            }
            Instant currentTime = Instant.now();
            Instant otpTimestamp = existingOtp.getCurrentTime();
            if (otpTimestamp != null && otpTimestamp.plusSeconds(120).isAfter(currentTime)) {
                System.out.println("OTP cannot be sent yet. Please wait for 2 minutes.");
                return false; // Indicate that the OTP cannot be sent yet
            }
        }

        int otp;
        if (existingOtp != null && existingOtp.getAttempts() == 1) {
            otp = existingOtp.getOtp();
        } else {
            otp = generateOtp();
        }
        String messageBody = "Your OTP is: " + otp;

        OtpRequestDto otpRequestDto;
        if (existingOtp != null) {
            otpRequestDto = existingOtp;
        } else {
            otpRequestDto = new OtpRequestDto(phoneNumber, otp);
            otpRequestDto.setId(getGeneratedId());
            otpRequestDto.setExpiryTime(Instant.now().plusSeconds(300)); // Set expiry time to 5 minutes from now
        }
        otpRequestDto.setAttempts(otpRequestDto.getAttempts() + 1); // Increment the attempts count
        otpRequestDto.setCurrentTime(Instant.now().plusSeconds(120)); // Set the current timestamp for the second attempt
        otpRepository.save(otpRequestDto);

        try {
            Message message = Message
                    .creator(new PhoneNumber(phoneNumber), new PhoneNumber(twilioPhoneNumber), messageBody)
                    .create();

            System.out.println("OTP sent successfully. Message SID: " + message.getSid());

            // Set TTL behavior for the document
            Date expiryDate = Date.from(otpRequestDto.getExpiryTime());
            mongoTemplate.getCollection("Otp").createIndex(new Document("expiryTime", 1));

            return true; // Indicate that the OTP was sent successfully
        } catch (Exception e) {
            System.out.println("Failed to send OTP: " + e.getMessage());
            return false; // Indicate that the OTP failed to send
        }
	}





    public boolean verifyOtp(OtpRequestDto otpRequestDto) {
        OtpRequestDto existingOtp = otpRepository.findByPhoneNumber(otpRequestDto.getPhoneNumber());
        if (existingOtp != null) {
            if (existingOtp.getOtp() == otpRequestDto.getOtp()) {
                Instant currentTime = Instant.now();
                Instant otpExpiryTime = existingOtp.getExpiryTime();
                if (otpExpiryTime != null && otpExpiryTime.isAfter(currentTime)) {
                    otpRepository.delete(existingOtp); // Delete the OTP record

                    UserInfo user = new UserInfo();
                    user.setUserContactNo(existingOtp.getPhoneNumber());
                    user.setUserId("USR"+ getGeneratedId());
                    userRepository.save(user);

                    return true; // OTP verification successful
                } else {
                    return false; // OTP has expired
                }
            } else {
                return false; // Invalid OTP
            }
        } else {
            return false; // OTP does not exist for the phone number
        }
    }

	private int generateOtp() {
		return ThreadLocalRandom.current().nextInt(1000, 9999);
	}

}