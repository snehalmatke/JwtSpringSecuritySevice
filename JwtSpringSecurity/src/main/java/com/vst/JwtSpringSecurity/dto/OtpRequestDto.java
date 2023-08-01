package com.vst.JwtSpringSecurity.dto;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Document(collection = "Otp")
public class OtpRequestDto {
	
	@Id
	private String id;
	
	@Pattern(regexp = ("(0|91)?[6-9][0-9]{9}"), message = "please Enter Valid ContactNo")
	private String phoneNumber;

	private int otp;
	
	 private Instant expiryTime;
	
	 private int attempts;

	 private Instant currentTime ;
	 
	 private boolean verifiedStatus;
	 	 
//	public OtpRequestDto(String phoneNumber, int otp) {
//		super();
//		this.phoneNumber = phoneNumber;
//		this.otp = otp;
//	}
	
	
}
	