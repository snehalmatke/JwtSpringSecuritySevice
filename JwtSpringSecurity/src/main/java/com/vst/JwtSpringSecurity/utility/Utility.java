package com.vst.JwtSpringSecurity.utility;

import org.springframework.stereotype.Component;

@Component
public class Utility {

	public static  String toTitleCase(String input) {
	    StringBuilder titleCase = new StringBuilder(input.length());
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            nextTitleCase = false;
	        }

	        titleCase.append(c);
	    }

	    return titleCase.toString();
	}
	
	public static String sanitizeInput(String input) {
		return input.replaceAll("[^a-zA-Z0-9]", ""); // remove all characters except letters and digits
	}
	
	public static String  validator(String input) {
		return input.replace("^[a-zA-Z]+$","first name should only contain letters");
	}
}
