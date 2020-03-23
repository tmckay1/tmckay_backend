package com.thetylermckay.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "image not found")
public class ImageNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}
