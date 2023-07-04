package com.vst.JwtSpringSecurity.error;

import java.time.LocalDateTime;

import org.apache.http.HttpStatus;

import lombok.Data;

@Data
public class ApiResponse {

	LocalDateTime timeStamp;
	String code;
	String message;
	String description;
	String reason;
}
