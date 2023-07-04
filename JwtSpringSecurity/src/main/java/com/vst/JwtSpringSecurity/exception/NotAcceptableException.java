package com.vst.JwtSpringSecurity.exception;

public class NotAcceptableException extends RuntimeException{

	private static final long serialVersionUID = 215232344516490651L;

	public NotAcceptableException(String message) {
	super(message);
}
}
