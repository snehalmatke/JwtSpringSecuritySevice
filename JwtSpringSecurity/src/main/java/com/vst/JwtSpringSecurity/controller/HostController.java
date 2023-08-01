package com.vst.JwtSpringSecurity.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.vst.JwtSpringSecurity.dto.LoginRequest;
import com.vst.JwtSpringSecurity.dto.OtpRequestDto;
import com.vst.JwtSpringSecurity.dto.RefreshTokenRequest;
import com.vst.JwtSpringSecurity.model.RefreshToken;
import com.vst.JwtSpringSecurity.dto.HostDto;
import com.vst.JwtSpringSecurity.dto.JwtResponse;
import com.vst.JwtSpringSecurity.serviceJwtService.HostService;
import com.vst.JwtSpringSecurity.serviceJwtService.JwtService;
import com.vst.JwtSpringSecurity.serviceJwtService.OtpService;
import com.vst.JwtSpringSecurity.serviceJwtService.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class HostController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private HostService hostService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private OtpService otpService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Check if a new host registration request can be initiated and return the
	 * appropriate response status.
	 *
	 * @param phoneNumber The phone number of the host for which the registration
	 *                    request is made.
	 * @return ResponseEntity with the appropriate status message as per the request
	 *         status.
	 */
	@GetMapping("registerHost/sendOtp")
	public ResponseEntity<String> registerNewHostRequestForOtpIfExist(@RequestParam("phoneNumber") String phoneNumber) {

		int hostFlag = hostService.checkedIfHostExist(phoneNumber);
		if (hostFlag == 1) {
			return ResponseEntity.ok("{\"status\":\"hostExist\"}");
		} else if (hostFlag == 2) {
			int userFlag = userService.checkIfUserExist(phoneNumber);
			if (userFlag == 1) {
				return ResponseEntity.ok("{\"status\":\"userExist\"}");
			} else if (userFlag == 2) {
				int otpFlag = otpService.RequestForOtp(phoneNumber);
				if (otpFlag == 3) {
					return ResponseEntity.ok("{\"status\":\"sent\"}");
				} else if (otpFlag == 1) {
					return ResponseEntity.ok("{\"status\":\"alreadySent\"}");
				} else if (otpFlag == 2) {
					return ResponseEntity.ok("{\"status\":\"wait\"}");

				}
			}
		}
		return ResponseEntity.ok("{\"status\":\"error\"}");

	}

	/**
	 * 
	 * Check if the user exists and send an OTP request if the user is found. Return
	 * the appropriate response status based on the user existence and OTP sending
	 * status.
	 *
	 * @param phoneNumber The phone number of the user for which the OTP request is
	 *                    made.
	 * @return ResponseEntity with the appropriate status message as per the request
	 *         status.
	 */
	@GetMapping("registerHost/ExistsUser/sendOtp")
	public ResponseEntity<String> sendOtpIfUserExists(@RequestParam("phoneNumber") String phoneNumber) {
		int userFlag = userService.checkIfUserExist(phoneNumber);

		if (userFlag == 1) {
			int otpFlag = otpService.RequestForOtp(phoneNumber);
			if (otpFlag == 3) {
				return ResponseEntity.ok("{\"status\":\"sent\"}");
			} else if (otpFlag == 1) {
				return ResponseEntity.ok("{\"status\":\"alreadySent\"}");
			} else if (otpFlag == 2) {
				return ResponseEntity.ok("{\"status\":\"wait\"}");
			}
		} else if (userFlag == 2) {
			return ResponseEntity.ok("{\"status\":\"userNotExists\"}");
		}

		return ResponseEntity.ok("{\"status\":\"error\"}");
	}

	/**
	 * Verify the OTP and create a new host if the conditions are met.
	 *
	 * @param phoneNumber The phone number for which the OTP verification and host
	 *                    creation is performed.
	 * @param otp         The OTP code entered by the user for verification.
	 * @return ResponseEntity with the appropriate status message as per the
	 *         verification and host creation status.
	 */
	@GetMapping("registerHost/verifyOtp")
	public ResponseEntity<String> verifyAndCreateNewHost(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("otp") int otp) {

		int hostFlag = hostService.checkedIfHostExist(phoneNumber);
		int userFlag = userService.checkIfUserExist(phoneNumber);

		if (hostFlag == 1) {
			return ResponseEntity.ok().body("{\"status\":\"hostExist\"}");
		} else if (userFlag == 1) {
			return ResponseEntity.ok().body("{\"status\":\"userExist\"}");
		} else {
			boolean isOtpVerified = otpService.verifyOtp(phoneNumber, otp);
			if (isOtpVerified) {
				boolean isHostCreated = hostService.createHostByPhoneNumber(phoneNumber);
				if (isHostCreated) {
					return ResponseEntity.ok("{\"status\":\"created\"}");
				} else {
					return ResponseEntity.ok().body("{\"status\":\"failed\"}");
				}
			} else {
				return ResponseEntity.ok().body("{\"status\":\"invalid\"}");
			}
		}
	}

	/**
	 * Verify the OTP and create a host with an existing user if the conditions are
	 * met. Return the appropriate response status based on the verification and
	 * host creation status.
	 *
	 * @param phoneNumber The phone number for which the OTP verification and host
	 *                    creation is performed.
	 * @param otp         The OTP code entered by the user for verification.
	 * @return ResponseEntity with the appropriate status message as per the
	 *         verification and host creation status.
	 */
	@GetMapping("registerHost/existUser/verifyOtp")
	public ResponseEntity<String> verifyAndCreateHostWithExistingUser(@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("otp") int otp) {

		int hostFlag = hostService.checkedIfHostExist(phoneNumber);
		int userFlag = userService.checkIfUserExist(phoneNumber);

		if (hostFlag == 1) {
			return ResponseEntity.ok().body("{\"status\":\"hostExist\"}");
		} else if (userFlag == 1) {
			boolean isOtpVerified = otpService.verifyOtp(phoneNumber, otp);
			if (isOtpVerified) {
				boolean isHostCreated = hostService.createHostByExistsUser(phoneNumber);
				if (isHostCreated) {
					return ResponseEntity.ok("{\"status\":\"created\"}");
				} else {
					return ResponseEntity.ok().body("{\"status\":\"failed\"}");
				}
			} else {
				return ResponseEntity.ok().body("{\"status\":\"invalid\"}");
			}
		} else {
			return ResponseEntity.ok().body("{\"status\":\"userNotExists\"}");
		}
	}

	/**
	 * Update additional details for a host using a PUT request
	 * 
	 * @param hostContactNo The contact number of the host whose details are to be
	 *                      updated.
	 * 
	 * @param hostDto       The HostDto containing the updated details to be saved.
	 * @return ResponseEntity with the appropriate status message as per the success
	 *         or failure of the host details update.
	 */
	@PutMapping("registerHost/AdditinalDetails")
	public ResponseEntity<String> registerAdditionalDetails(@RequestParam String hostContactNo,
			@RequestBody HostDto hostDto) {
		hostDto.setHostContactNo("");
		boolean host = hostService.updateHost(hostContactNo, hostDto);
		if (host) {
			return ResponseEntity.ok("{\"status\":\"success\"}");
		} else {
			return ResponseEntity.ok().body("{\"status\":\"failed\"}");
		}
	}
	
	
	
	@PutMapping("registerHost/updatePassword")
	public ResponseEntity<String> register(@RequestParam String hostContactNo,
			@RequestBody HostDto hostDto) {
		hostDto.setHostContactNo("");
		boolean host = hostService.updatePassword(hostContactNo, hostDto);
		if (host) {
			return ResponseEntity.ok("{\"status\":\"success\"}");
		} else {
			return ResponseEntity.ok().body("{\"status\":\"failed\"}");
		}
	}

	/**
	 * Login Host by ContactNo and Password
	 * 
	 * @param loginRequest The LoginRequest object containing the host's contact
	 *                     number and password.
	 * @return ResponseEntity with a JWT token if the login is valid, or a response
	 *         indicating invalid credentials.
	 */
	@PostMapping("loginHost/ByContactNoAndPassword")
	public ResponseEntity<String> loginByHostContactNoAndPassword(@RequestBody LoginRequest loginRequest) {
	    String hostContactNo = loginRequest.getUserName();
	    String password = loginRequest.getPassword();

	    // Check if the hostContactNo exists in the host database
	    int hostExistStatus = hostService.checkedIfHostExist(hostContactNo);
	    if (hostExistStatus == 2) {
	        return ResponseEntity.ok().body("{\"status\":\"hostNotExist\"}");
	    }

	    if (hostExistStatus == 1) {
	        // Check if the username and password match
	        boolean isValidLogin = hostService.loginByHostContactNoAndPassword(hostContactNo, password);
	        if (isValidLogin) {
	            // Generate the JWT token
	            String jwtToken = jwtService.generateToken(hostContactNo);
	            return ResponseEntity.ok("{\"token\":\"" + jwtToken + "\"}");
	        } else {
	            return ResponseEntity.ok().body("{\"status\":\"invalid\"}");
	        }
	    } else {
	        // Handle other error scenarios if necessary
	        return ResponseEntity.ok().body("{\"status\":\"error\"}");
	    }
	}



	/**
	 * Login Host by email and Password
	 * 
	 * @param loginRequest The LoginRequest object containing the host's email and
	 *                     password.
	 * @return ResponseEntity with a JWT token if the login is valid, or a response
	 *         indicating invalid credentials.
	 */
	@PostMapping("loginHost/ByEmailAndPassword")
	public ResponseEntity<String> loginByHostEmailAndPassword(@RequestBody LoginRequest loginRequest) {
	    String hostEmail = loginRequest.getUserName();
	    String password = loginRequest.getPassword();

	    // Check if the hostEmail exists in the host database
	    int hostExistStatus = hostService.checkedIfHostExistByEmail(hostEmail);
	    if (hostExistStatus == 2) {
	        return ResponseEntity.ok().body("{\"status\":\"hostNotExist\"}");
	    }

	    if (hostExistStatus == 1) {
	        // Check if the username and password match
	        boolean isValidLogin = hostService.loginByHostEmailAndPassword(hostEmail, password);
	        if (isValidLogin) {
	            // Generate the JWT token
	            String jwtToken = jwtService.generateToken(hostEmail);
	            return ResponseEntity.ok("{\"token\":\"" + jwtToken + "\"}");
	        } else {
	            return ResponseEntity.ok().body("{\"status\":\"invalid\"}");
	        }
	    } else {
	        // Handle other error scenarios if necessary
	        return ResponseEntity.ok().body("{\"status\":\"error\"}");
	    }
	}

	/**
	 * sent otp for login host
	 * 
	 * @param phoneNumber The phone number of the host for which to send the OTP.
	 * @return ResponseEntity with the OTP sending status as the response body.
	 */
	@GetMapping("loginHost/sentOtp")
	public ResponseEntity<String> sendOtpForLoginHost(@RequestParam("phoneNumber") String phoneNumber) {
		int flag = hostService.checkedIfHostExist(phoneNumber);

		if (flag == 1) {
			int otpFlag = otpService.RequestForOtp(phoneNumber);

			if (otpFlag == 3) {
				return ResponseEntity.ok("{\"status\":\"sent\"}");
			} else if (otpFlag == 1) {
				return ResponseEntity.ok("{\"status\":\"alreadySent\"}");
			} else if (otpFlag == 2) {
				return ResponseEntity.ok("{\"status\":\"wait\"}");
			}
		} else if (flag == 2) {
			return ResponseEntity.ok("{\"status\":\"notExists\"}");
		}

		return ResponseEntity.ok("{\"status\":\"error\"}");
	}

	/**
	 * login host by verify otp
	 * 
	 * @param OtpRequestDto An object containing the phone number and OTP to be
	 *                      verified.
	 * @return ResponseEntity with the JWT token as the response body if the OTP is
	 *         valid, or an error message if the OTP is invalid.
	 */
	@PostMapping("loginHost/verifyOtp")
	public ResponseEntity<String> loginHostByVerifyOtp(@RequestBody OtpRequestDto OtpRequestDto) {
		String phoneNumber = OtpRequestDto.getPhoneNumber();
		int otp = OtpRequestDto.getOtp();

		// Verify the OTP
		boolean isOtpValid = otpService.verifyOtp(phoneNumber, otp);

		if (isOtpValid) {
			// OTP is valid, generate JWT token
			String jwtToken = jwtService.generateToken(phoneNumber);
			return ResponseEntity.ok("{\"token\":\"" + jwtToken + "\"}");
		} else {
			// Invalid OTP, return error message
			return ResponseEntity.ok().body("{\"status\":\"invalid\"}");
		}
	}

	
	@GetMapping("/getUserId")
	public ResponseEntity<String> getUserId(@RequestHeader("Authorization") String authorizationHeader) {
	    String userId = jwtService.extractUserIdFromHeader(authorizationHeader);

	    if (userId != null) {
	        return ResponseEntity.ok("{\"status\":\"" + userId + "\"}");
	    } else {
	        return ResponseEntity.ok().body("{\"status\":\"invalid\"}");
	    }
	}	
	
	
	@GetMapping("forgetPassword/sentOtp")
	public ResponseEntity<String> sentOtpForForgetPassword(@RequestParam("phoneNumber") String phoneNumber) {
		int flag = hostService.checkedIfHostExist(phoneNumber);

		if (flag == 1) {
			int otpFlag = otpService.RequestForOtp(phoneNumber);

			if (otpFlag == 3) {
				return ResponseEntity.ok("{\"status\":\"sent\"}");
			} else if (otpFlag == 1) {
				return ResponseEntity.ok("{\"status\":\"alreadySent\"}");
			} else if (otpFlag == 2) {
				return ResponseEntity.ok("{\"status\":\"wait\"}");
			}
		} else if (flag == 2) {
			return ResponseEntity.ok("{\"status\":\"hostNotExists\"}");
		}

		return ResponseEntity.ok("{\"status\":\"error\"}");
	}
	
	
	@GetMapping("forgetPassword/verifyOtp")
	public ResponseEntity<String> verifyOtpForForgetPassword(@RequestParam("phoneNumber") String phoneNumber,
	                                                         @RequestParam("otp") int otp) {
	    int hostFlag = hostService.checkedIfHostExist(phoneNumber);
	    if (hostFlag == 2) {
	        return ResponseEntity.ok().body("{\"status\":\"hostNotExist\"}");
	    } else {
	        boolean isOtpVerified = otpService.verifyOtp(phoneNumber, otp);
	        if (isOtpVerified) {
	            return ResponseEntity.ok("{\"status\":\"verified\"}");
	        } else {
	            return ResponseEntity.ok().body("{\"status\":\"invalid\"}");
	        }
	    }
	}
	
	
	  @PutMapping("forgetPassword/updatePassword")
	    public ResponseEntity<String> forgetPassword(@RequestParam("phoneNumber") String phoneNumber,
	                                                 @RequestParam("otp") int otp,
	                                                 @RequestBody HostDto hostDto) {
	        boolean isOtpVerified = otpService.verifyOtp(phoneNumber, otp);
	        if (isOtpVerified) {
	        	boolean host = hostService.updatePassword(phoneNumber, hostDto);
	    		if (host) {
	    			return ResponseEntity.ok("{\"status\":\"success\"}");
	    		} else {
	    			return ResponseEntity.ok().body("{\"status\":\"failed\"}");
	    		}
	        } else {
	            return ResponseEntity.ok().body("{\"status\":\"invalid\"}");
	        }
	    }

	

//	@PostMapping("/refreshToken")
//	public JwtResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
//		return refreshTokenService.findByToken(refreshTokenRequest.getToken())
//				.map(refreshTokenService::verifyExpiration).map(RefreshToken::getUserInfo).map(userInfo -> {
//					String accessToken = jwtService.generateToken(userInfo.getUserFirstName());
//					return JwtResponse.builder().accessToken(accessToken).token(refreshTokenRequest.getToken()).build();
//				}).orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
//	}
	  
	  
	  
	  

	
}
