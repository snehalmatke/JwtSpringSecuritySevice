package com.vst.JwtSpringSecurity.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vst.JwtSpringSecurity.dto.UserInfo;

public class UserInfoUserDetails implements UserDetails {

	 private String userContactNo;
	 private String password;
	    private List<GrantedAuthority> authorities;

	    public UserInfoUserDetails(UserInfo userInfo) {
	    	userContactNo=userInfo.getUserContactNo();
	        password=userInfo.getPassword();
	        authorities= Arrays.stream(userInfo.getRoles().split(","))
	                .map(SimpleGrantedAuthority::new)
	                .collect(Collectors.toList());
	    }
	    


	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return authorities;
	    }

	    @Override
	    public String getPassword() {
	        return password;
	    }

	    @Override
	    public String getUsername() {
	        return userContactNo;
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }
}
