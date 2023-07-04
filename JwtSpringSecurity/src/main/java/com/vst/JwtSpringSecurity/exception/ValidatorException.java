package com.vst.JwtSpringSecurity.exception;

public class ValidatorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidatorException(String message) {
		super(message);
	}
}
