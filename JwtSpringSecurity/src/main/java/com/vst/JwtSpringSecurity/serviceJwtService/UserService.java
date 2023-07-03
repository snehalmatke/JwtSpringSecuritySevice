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

import com.vst.JwtSpringSecurity.dto.UserDto;
import com.vst.JwtSpringSecurity.model.UserInfo;
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

	public String addUser(UserInfo userInfo) {
		userInfo.setId(getGeneratedId());
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		userRepo.save(userInfo);
		return "user added to system ";
	}

//    @Transactional
//	public boolean addUser(UserDto userDto) {
//
//		try {
//			// mongoConfig.mongoClient();
//			Date dNow = new Date();
//			userDto.setUserFirstName(utility.toTitleCase(userDto.getUserFirstName()));
//			userDto.setUserLastName(utility.toTitleCase(userDto.getUserLastName()));
//			userDto.setUserState(utility.toTitleCase(userDto.getUserState()));
//			userDto.setUserCity(utility.toTitleCase(userDto.getUserCity()));
//			userDto.setActive(true);
//			userDto.setUserId("USR" + getGeneratedId());
//	        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
//			if(userRepo.save(userDto)!=null) {
//			return true;
//			}else
//				return false;
//		} catch (Exception exception) {
//			exception.printStackTrace();
//		}
//		return false;
//
//	}

}
