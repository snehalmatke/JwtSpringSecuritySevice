package com.vst.JwtSpringSecurity.serviceJwtService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vst.JwtSpringSecurity.dto.HostDto;
import com.vst.JwtSpringSecurity.dto.OtpRequestDto;
import com.vst.JwtSpringSecurity.dto.UserDto;

import com.vst.JwtSpringSecurity.utility.Utility;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	
	@Autowired
	private Utility utility;

	@Autowired
	private PasswordEncoder passwordEncoder;	
	
	  @Autowired
	 private Environment environment;
	  
	  public String getGeneratedId() {
			String number = "";
			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			return ft.format(dNow) + number;

		}

	  
	public int checkIfUserExist(String phoneNumber) {
		int flag=0;
		//check if exist in host db
		  RestTemplate restTemplate = new RestTemplate();
		  String userUrl = environment.getProperty("manageUser.url", String.class);
		  String url=userUrl+"getUserDataByContactNo?userContactNo="+phoneNumber;
		  
		  try {
	        ResponseEntity<UserDto> response = restTemplate.exchange(
	                url,
	                HttpMethod.GET,
	                null,
	                UserDto.class
	        );

	        if (response.getStatusCode().is2xxSuccessful()) {
	        	UserDto userDto = response.getBody();
	            if(userDto!=null) {
	            	return flag=1;
	            }
	            else if(userDto==null) {
					return flag=2;
				}
	           
	        } else {
	            System.out.println("Request failed with response code: " + response.getStatusCodeValue());
	            return flag=2;
	        }
		  }catch (Exception e) {
			  return flag=2;
		}
			return flag;
		
	}


}