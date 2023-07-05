package com.vst.JwtSpringSecurity.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vst.JwtSpringSecurity.dto.OtpRequestDto;
import com.vst.JwtSpringSecurity.dto.UserInfo;

public class UserInfoUserDetails2 implements UserDetails{

    private String phoneNumber;
    private int otp;
    private List<GrantedAuthority> authorities;

    public UserInfoUserDetails2(OtpRequestDto otpRequestDto) {
        phoneNumber = otpRequestDto.getPhoneNumber();
        otp = otpRequestDto.getOtp();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
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

    @Override
    public String getPassword() {
        return String.valueOf(otp);
    }
}
