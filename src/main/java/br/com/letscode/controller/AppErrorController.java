package br.com.letscode.controller;

import javax.servlet.http.HttpServletRequest;

import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.commons.ResponseObject;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AppErrorController implements ErrorController {

	private final static String ERROR_PATH = "/error";

	@RequestMapping(value = ERROR_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject<Void>> error(HttpServletRequest request) {
		HttpStatus status = getStatus(request);
		ResponseObject<Void> r = new ResponseObject<>();
		r.addMessage(ResponseMessage.error(status.getReasonPhrase().concat(": {0}"), status.name()));
		return new ResponseEntity<ResponseObject<Void>>(r, status);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		try {
			return HttpStatus.valueOf((Integer) request.getAttribute("javax.servlet.error.status_code"));
		} catch (Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

}