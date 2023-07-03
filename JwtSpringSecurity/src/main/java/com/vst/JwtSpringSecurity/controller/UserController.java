package com.vst.JwtSpringSecurity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vst.JwtSpringSecurity.dto.AuthRequest;
import com.vst.JwtSpringSecurity.dto.JwtResponse;
import com.vst.JwtSpringSecurity.dto.OtpRequestDto;
import com.vst.JwtSpringSecurity.dto.RefreshTokenRequest;
import com.vst.JwtSpringSecurity.dto.UserDto;
import com.vst.JwtSpringSecurity.model.RefreshToken;
import com.vst.JwtSpringSecurity.model.UserInfo;
import com.vst.JwtSpringSecurity.serviceJwtService.JwtService;
import com.vst.JwtSpringSecurity.serviceJwtService.RefreshTokenService;
import com.vst.JwtSpringSecurity.serviceJwtService.TwilioService;
import com.vst.JwtSpringSecurity.serviceJwtService.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class UserController {

	 @Autowired
	    private UserService userService;
	    
	    @Autowired
	    private JwtService jwtService;

	    @Autowired
	    private RefreshTokenService refreshTokenService;

	    @Autowired
	    private AuthenticationManager authenticationManager;
	    
	    @Autowired
	    private TwilioService twilioService;

	 

	    @PostMapping("/send-otp")
	    public ResponseEntity<String> sendOtp(@RequestBody OtpRequestDto otpRequestDto) {
	        try {
	            twilioService.sendOtp(otpRequestDto.getPhoneNumber());
	            return ResponseEntity.ok("OTP sent successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP");
	        }
	    }

	   
	    
	    @PostMapping("/verify-otp")
	    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequestDto otpRequestDto) {
	        String phoneNumber = otpRequestDto.getPhoneNumber();
	        int userEnteredOtp = otpRequestDto.getOtp();

	        boolean isOtpValid = twilioService.verifyOtp(otpRequestDto);
	        if (isOtpValid) {
	            return ResponseEntity.ok("OTP verification successful");
	        } else {
	            return ResponseEntity.badRequest().body("Invalid OTP");
	        }
	    }

	    @PostMapping("/register")
	    public String addNewUser(@RequestBody UserInfo userInfo) {
	        return userService.addUser(userInfo);
	    }
	    
	    
//	    @PostMapping("/registerUser")
//		public ResponseEntity<String> addUser(@Valid @RequestBody UserDto userDto) {
//			if (userService.addUser(userDto)) {
//				return new ResponseEntity<>("User data added successfully", HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>("Data did not added", HttpStatus.BAD_REQUEST);
//			}
//		}
	    

	    @PostMapping("/login")
	    public JwtResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	        if (authentication.isAuthenticated()) {
	            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
	            return JwtResponse.builder()
	                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
	                    .token(refreshToken.getToken()).build();
	        } else {
	            throw new UsernameNotFoundException("invalid user request !");
	        }
	    }

	    @PostMapping("/refreshToken")
	    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
	        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
	                .map(refreshTokenService::verifyExpiration)
	                .map(RefreshToken::getUserInfo)
	                .map(userInfo -> {
	                    String accessToken = jwtService.generateToken(userInfo.getName());
	                    return JwtResponse.builder()
	                            .accessToken(accessToken)
	                            .token(refreshTokenRequest.getToken())
	                            .build();
	                }).orElseThrow(() -> new RuntimeException(
	                        "Refresh token is not in database!"));
	    }
	    
	    
	 

	
}
