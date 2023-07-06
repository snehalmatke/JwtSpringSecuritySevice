package com.vst.JwtSpringSecurity.dto;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Otp")
public class OtpRequestDto {
	
	private String id;
	
	private String phoneNumber;

	private int otp;
	
	 private Instant expiryTime;
	
	 private int attempts;

	 private Instant currentTime ;
	 
	public OtpRequestDto(String phoneNumber, int otp) {
		super();
		this.phoneNumber = phoneNumber;
		this.otp = otp;
	}
	
	
}
	