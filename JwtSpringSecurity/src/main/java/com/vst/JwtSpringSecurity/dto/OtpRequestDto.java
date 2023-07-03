package com.vst.JwtSpringSecurity.dto;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Otp")
public class OtpRequestDto {
	
	private String id;
	
	private String phoneNumber;

	private int otp;
	
	 private Instant expiryTime;

	public OtpRequestDto(String phoneNumber, int otp) {
		super();
		this.phoneNumber = phoneNumber;
		this.otp = otp;
	}
	
	
}
	