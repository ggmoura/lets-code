package br.com.letscode.advice;

import br.com.letscode.commons.MessageType;
import br.com.letscode.commons.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationAdvice {


	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseObject<Void>> handleAccessDeniedException(AccessDeniedException ex) {
		ResponseObject<Void> response = buildResponseObject("Full authentication is required to access this resource > {0}", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ResponseObject<Void>> handleAuthenticationException(AuthenticationException ex) {
		ResponseObject<Void> response = buildResponseObject(ex.getMessage().concat(" > {0}"), "AuthenticationException");
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ResponseObject<Void>> handleAuthenticationException(BadCredentialsException ex) {
		ResponseObject<Void> response = buildResponseObject(ex.getMessage().concat(" > {0}"), "BadCredentialsException");
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	private ResponseObject<Void> buildResponseObject(final String message, final String param) {
		ResponseObject<Void> response = new ResponseObject<>();
		response.addMessage(message, MessageType.ERROR, param);
		return response;
	}

}
