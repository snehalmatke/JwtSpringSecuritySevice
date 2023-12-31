package com.vst.JwtSpringSecurity.model;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import com.vst.JwtSpringSecurity.dto.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "token")
public class RefreshToken {

	
	 private String id;

	    private String token;

	    private Instant expiryDate;

//	    @OneToOne
//	    @JoinColumn(name = "user_id", referencedColumnName = "id")
	    private UserInfo userInfo;

}
