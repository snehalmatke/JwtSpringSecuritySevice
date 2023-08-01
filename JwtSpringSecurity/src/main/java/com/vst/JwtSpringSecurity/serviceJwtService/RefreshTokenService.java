//package com.vst.JwtSpringSecurity.serviceJwtService;
//
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.util.Date;
//import java.util.Optional;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.vst.JwtSpringSecurity.model.RefreshToken;
//
//
//@Service
//public class RefreshTokenService {
//
////	  @Autowired
////	   private RefreshTokenRepository refreshTokenRepository;
////	  
////	  
////	  @Autowired
////	  private OtpRepository otpRepository;
////	  
////	    @Autowired
////	    private UserInfoRepository userInfoRepository;
////	    
////	    @Autowired
////	    HostRepository hostRepository;
//
//	    
//	    public String getGeneratedId() {
//			String number = "";
//			Date dNow = new Date();
//			SimpleDateFormat ft = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//			return ft.format(dNow) + number;
//
//		}
//	    
//	    
//
//	    // This method creates a refresh token for the given username.
//	    // It generates a random token, sets the user information using the username,
//	    // and saves the refresh token in the repository.
//	    public RefreshToken createRefreshToken(String username) {
//	        RefreshToken refreshToken = RefreshToken.builder()
//	                .userInfo(userInfoRepository.findByUserContactNo(username).get())
//	                .token(UUID.randomUUID().toString())
//	                .expiryDate(Instant.now().plusMillis(5184000000L)) // 2 months from now
//	                .build();
//    		refreshToken.setId(getGeneratedId());
//
//	        return refreshTokenRepository.save(refreshToken);
//	    }
//	    
//	    
//	    public RefreshToken createRefreshTokenHost(String username) {
//	        RefreshToken refreshToken = RefreshToken.builder()
//	                .hostDto(hostRepository.findByHostContactNo(username).get())
//	                .token(UUID.randomUUID().toString())
//	                .expiryDate(Instant.now().plusMillis(600000)) // 10 minutes from now
//	                .build();
//    		refreshToken.setId(getGeneratedId());
//
//	        return refreshTokenRepository.save(refreshToken);
//	    }
//	    
//	  
//	    
//	    public RefreshToken createRefreshTokenByEmail(String username) {
//	        RefreshToken refreshToken = RefreshToken.builder()
//	                .userInfo(userInfoRepository.findByUserEmail(username).get())
//	                .token(UUID.randomUUID().toString())
//	                .expiryDate(Instant.now().plusMillis(600000)) // 10 minutes from now
//	                .build();
//	        refreshToken.setId(getGeneratedId());
//	        return refreshTokenRepository.save(refreshToken);
//	    }
//	    
//	    public RefreshToken createRefreshTokenHostEmail(String username) {
//	        RefreshToken refreshToken = RefreshToken.builder()
//	                .hostDto(hostRepository.findByHostEmail(username).get())
//	                .token(UUID.randomUUID().toString())
//	                .expiryDate(Instant.now().plusMillis(600000)) // 10 minutes from now
//	                .build();
//    		refreshToken.setId(getGeneratedId());
//
//	        return refreshTokenRepository.save(refreshToken);
//	    }
//	    
//	    
//	    
//	    public RefreshToken createRefreshTokenByOtp(String username) {
//	        RefreshToken refreshToken = RefreshToken.builder()
//	                .userInfo(userInfoRepository.findByUserContactNo(username).get())
//	                .token(UUID.randomUUID().toString())
//	                .expiryDate(Instant.now().plusMillis(600000)) // 10 minutes from now
//	                .build();
//	        refreshToken.setId(getGeneratedId());
//	        return refreshTokenRepository.save(refreshToken);
//	    }
//	    
//	    
//
//
//	    // This method finds a refresh token by its token value.
//	    public Optional<RefreshToken> findByToken(String token) {
//	        return refreshTokenRepository.findByToken(token);
//	    }
//
//	    // This method verifies if a refresh token has expired.
//	    // If the expiry date of the token is before the current time,
//	    // the token is deleted from the repository, and a RuntimeException is thrown.
//	    public RefreshToken verifyExpiration(RefreshToken token) {
//	        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//	            refreshTokenRepository.delete(token);
//	            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
//	        }
//	        return token;
//	    }
//}
