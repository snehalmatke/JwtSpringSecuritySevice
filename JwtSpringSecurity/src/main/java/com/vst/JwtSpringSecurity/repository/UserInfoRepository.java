//package com.vst.JwtSpringSecurity.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//import com.vst.JwtSpringSecurity.dto.UserInfo;
//
//public interface UserInfoRepository extends MongoRepository<UserInfo, String> {
//
//	Optional<UserInfo> findByUserFirstName(String userFirstName);
//
//	UserInfo findByUserEmailIgnoreCaseAndIsActiveTrue(String userEmail);
//
//	UserInfo findByUserContactNoAndIsActiveTrue(String userContactNo);
//	
//	Optional<UserInfo> findByUserContactNo(String userContactNo);
//	
//	Optional<UserInfo> findByUserEmail(String userEmail);
//
//	UserInfo findByUserId(String userId);
//
//	UserInfo findByUserIdAndIsActiveTrue(String userId);
//}
