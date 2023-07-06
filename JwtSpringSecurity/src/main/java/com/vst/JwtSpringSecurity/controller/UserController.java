package com.vst.JwtSpringSecurity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.twilio.Twilio;
import com.twilio.exception.AuthenticationException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.vst.JwtSpringSecurity.config.UserInfoUserDetails1;
import com.vst.JwtSpringSecurity.config.UserInfoUserDetails2;
import com.vst.JwtSpringSecurity.config.UserInfoUserDetailsService;
import com.vst.JwtSpringSecurity.dto.AuthRequest;
import com.vst.JwtSpringSecurity.dto.JwtResponse;
import com.vst.JwtSpringSecurity.dto.OtpRequestDto;
import com.vst.JwtSpringSecurity.dto.RefreshTokenRequest;
import com.vst.JwtSpringSecurity.dto.UserInfo;
import com.vst.JwtSpringSecurity.exception.ValidatorException;
import com.vst.JwtSpringSecurity.model.RefreshToken;
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
	            boolean otpSent = twilioService.sendOtp(otpRequestDto.getPhoneNumber());
	            if (otpSent) {
	                return ResponseEntity.ok("OTP sent successfully");
	            } else {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP, try after sometime");
	            }
	        } catch (Exception e) {
	            System.out.println("Failed to send OTP: " + e.getMessage());
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
	    public ResponseEntity<String> addNewUser(@Valid @RequestBody UserInfo userInfo) {
	        try {
	            userService.addUser(userInfo);
	            return ResponseEntity.ok("User added to the system");
	        } catch (ValidatorException e) {
	            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
	        }
	    }

	     

	    @PostMapping("/loginByContactNO")
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
	    
	    @PostMapping("/loginByEmail")
	    public JwtResponse authenticateAndGetToken1(@RequestBody AuthRequest authRequest) {
	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	        if (authentication.isAuthenticated()) {
	            RefreshToken refreshToken = refreshTokenService.createRefreshTokenByEmail(authRequest.getUsername());
	            return JwtResponse.builder()
	                    .accessToken(jwtService.generateToken(authRequest.getUsername()))
	                    .token(refreshToken.getToken()).build();
	        } else {
	            throw new UsernameNotFoundException("invalid user request !");
	        }
	    }
	    
	   
	    
	    @PostMapping("/loginByOtp")
	    public ResponseEntity<String> loginByOtp(@RequestBody OtpRequestDto otpRequestDto) {
	        String phoneNumber = otpRequestDto.getPhoneNumber();
	        int userEnteredOtp = otpRequestDto.getOtp();

	        boolean isOtpValid = twilioService.verifyOtp(otpRequestDto);
	        if (isOtpValid) {
	            // Generate JWT token
	            UserDetails userDetails = new UserInfoUserDetails2(otpRequestDto);
	            String jwtToken = jwtService.generateToken(phoneNumber);

	            return ResponseEntity.ok("OTP verification successful. JWT token: " + jwtToken);
	        } else {
	            return ResponseEntity.badRequest().body("Invalid OTP");
	        }
	    }

	    
	    
	    

	    @PostMapping("/refreshToken")
	    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
	        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
	                .map(refreshTokenService::verifyExpiration)
	                .map(RefreshToken::getUserInfo)
	                .map(userInfo -> {
	                    String accessToken = jwtService.generateToken(userInfo.getUserFirstName());
	                    return JwtResponse.builder()
	                            .accessToken(accessToken)
	                            .token(refreshTokenRequest.getToken())
	                            .build();
	                }).orElseThrow(() -> new RuntimeException(
	                        "Refresh token is not in database!"));
	    }
	    
	    
	 

	
}
