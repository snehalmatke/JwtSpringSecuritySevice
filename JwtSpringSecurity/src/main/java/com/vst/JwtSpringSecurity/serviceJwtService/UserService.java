package com.vst.JwtSpringSecurity.serviceJwtService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vst.JwtSpringSecurity.dto.UserInfo;
import com.vst.JwtSpringSecurity.exception.NotAcceptableException;
import com.vst.JwtSpringSecurity.exception.ValidatorException;
import com.vst.JwtSpringSecurity.repository.UserInfoRepository;
import com.vst.JwtSpringSecurity.utility.Utility;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	public String getGeneratedId() {
		String number = "";
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return ft.format(dNow) + number;

	}

	@Autowired
	Utility utility;

	@Autowired
	UserInfoRepository userRepo;

	@Autowired
	PasswordEncoder passwordEncoder;	
	
	
	public void addUser(UserInfo userInfo) {
	    userInfo.setUserId("USR" + getGeneratedId());
	    userInfo.setUserFirstName(utility.toTitleCase(userInfo.getUserFirstName()));
	    userInfo.setUserLastName(utility.toTitleCase(userInfo.getUserLastName()));
	    userInfo.setUserState(utility.toTitleCase(userInfo.getUserState()));
	    userInfo.setUserCity(utility.toTitleCase(userInfo.getUserCity()));
	    userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
	    userInfo.setRoles("USER");
	    
	    UserInfo existingUserEmail = userRepo.findByUserEmailIgnoreCaseAndIsActiveTrue(userInfo.getUserEmail());
	    if (existingUserEmail != null) {
	        throw new ValidatorException("Email ID already exists. Please use a different email.");
	    }

	    UserInfo existingUserContactNo = userRepo.findByUserContactNoAndIsActiveTrue(userInfo.getUserContactNo());
	    if (existingUserContactNo != null) {
	        throw new ValidatorException("ContactNo already exists. Please use a different ContactNo.");
	    }

	    UserInfo existsUser = userRepo.findByUserId(userInfo.getUserId());
	    if (existsUser != null) {
	        userInfo.setUserId("USR" + getGeneratedId());
	    }
	    userRepo.save(userInfo);
	}


}