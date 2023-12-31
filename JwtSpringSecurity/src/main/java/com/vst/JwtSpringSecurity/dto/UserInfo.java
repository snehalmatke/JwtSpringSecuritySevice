package com.vst.JwtSpringSecurity.dto;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class UserInfo {

	@Id
	private String userId;

	@NotBlank(message = "User first name must not be Blank")
	@Size(min = 3, max = 20, message = "First name should be min=3 character and max=50 character")
	@NotNull(message = "User last name must not be Null  ")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "First name should only contain letters")
	private String userFirstName;

	@NotBlank(message = "User last name must not be Blank")
	@Size(min = 3, max = 20, message = "Last name should be min=3 character and max=50 character")
	@NotNull(message = "User last name must not be Null ")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "Last name should only contain letters")
	private String userLastName;

	private String userGender;

	private String userDateOfBirth;

	@NotBlank(message = "User Email must not be Blank")
	@Email(message = "Please Enter Correct Email")
	@NotNull(message = "User Email must not be Null")
	private String userEmail;

	@NotBlank(message = "User ContactNo must not be Blank")
	@NotNull(message = "User ContactNo must not be Null")
	@Pattern(regexp = ("(0|91)?[6-9][0-9]{9}"), message = "please Enter Valid ContactNo")
	private String userContactNo;

	@NotBlank(message = "User Address must not be Blank")
	private String userAddress;

	@NotNull(message = "User City must not be Null")
	@NotBlank(message = "User City must not be Blank")
	@Pattern(regexp = "^[a-zA-Z]+$", message = " City should only contain letters")
	private String userCity;

	private String userState;

	private String userPincode;

	private String userProfilePhoto;
	
	private Date createdDate;
	
	private Date modifiedDate;

	private boolean isActive = true;
	
	@NotNull(message = "Password must not be Null")
	@NotBlank(message = "Password must not be Blank")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8}$", message = "Password should contain a combination of at least one lowercase letter, one first letter uppercase , one digit, and one special character. It should be exactly 8 characters long.")
	private String password;



	
	private String roles;



	
}
