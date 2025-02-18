package com.roque.book.handler;

import static com.roque.book.handler.BusinessErrorCodes.ACCOUNT_DISABLED;
import static com.roque.book.handler.BusinessErrorCodes.ACCOUNT_LOCKED;
import static com.roque.book.handler.BusinessErrorCodes.BAD_CREDENTIALS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.roque.book.exception.OperationNotPermittedException;

import jakarta.mail.MessagingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<ExceptionResponse> handleException(LockedException lockedException) {
		return ResponseEntity.status(UNAUTHORIZED)
				.body(ExceptionResponse.builder().businessErrorCode(ACCOUNT_LOCKED.getCode())
						.businessErrorDescription(ACCOUNT_LOCKED.getDescritpion()).error(lockedException.getMessage())
						.build());
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<ExceptionResponse> handleException(DisabledException disabledException) {
		return ResponseEntity.status(UNAUTHORIZED)
				.body(ExceptionResponse.builder().businessErrorCode(ACCOUNT_DISABLED.getCode())
						.businessErrorDescription(ACCOUNT_DISABLED.getDescritpion())
						.error(disabledException.getMessage()).build());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException badCredentialsException) {
		return ResponseEntity.status(UNAUTHORIZED)
				.body(ExceptionResponse.builder().businessErrorCode(BAD_CREDENTIALS.getCode())
						.businessErrorDescription(BAD_CREDENTIALS.getDescritpion())
						.error("Login and / or Password is incorrect").build());
	}

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ExceptionResponse> handleException(MessagingException messagingException) {
		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.body(ExceptionResponse.builder().error(messagingException.getMessage()).build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {
		Set<String> errors = new HashSet<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			var errorMessage = error.getDefaultMessage();
			errors.add(errorMessage);
		});
		return ResponseEntity.status(INTERNAL_SERVER_ERROR)
				.body(ExceptionResponse.builder().validationErrors(errors).build());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
		// log the exception
		exception.printStackTrace();
		return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ExceptionResponse.builder()
				.businessErrorDescription("Internal error, contact the admin").error(exception.getMessage()).build());
	}

	@ExceptionHandler(OperationNotPermittedException.class)
	public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exception) {
		return ResponseEntity.status(BAD_REQUEST)
				.body(ExceptionResponse.builder().error(exception.getMessage()).build());
	}
}
