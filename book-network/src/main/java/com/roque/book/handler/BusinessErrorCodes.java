package com.roque.book.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum BusinessErrorCodes {

	NO_CODE(0, NOT_IMPLEMENTED, "No code"),
	INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password incorrect"),
	NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "new password does not match"),
	ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
	ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
	BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or password is incorrect");

	private final int code;

	private final String descritpion;

	private final HttpStatus httpStatus;

	BusinessErrorCodes(int code, HttpStatus httpStatus, String descritpion) {
		this.code = code;
		this.httpStatus = httpStatus;
		this.descritpion = descritpion;
	}

}
