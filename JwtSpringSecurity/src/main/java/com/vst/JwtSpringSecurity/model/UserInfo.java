package com.vst.JwtSpringSecurity.model;


import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class UserInfo {

	  private String id;
	    private String name;
	    private String email;
	    private String password;
	    private String roles;
	    
	   
	 
	
}
