package br.com.oobj.zuul.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitExceededException;
import com.netflix.zuul.exception.ZuulException;

@ControllerAdvice
public class ErrorControllerAdvice {

	@ExceptionHandler(ZuulException.class)
	@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS)
	@ResponseBody
	public ErroDTO zuulException(HttpServletRequest req, ZuulException ex) {
		return new ErroDTO(HttpStatus.TOO_MANY_REQUESTS.value(), ex.getMessage());
	}

	@ExceptionHandler(RateLimitExceededException.class)
	@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS)
	@ResponseBody
	public ErroDTO rateLimitExceededException(HttpServletRequest req, RateLimitExceededException ex) {
		return new ErroDTO(HttpStatus.TOO_MANY_REQUESTS.value(), ex.getMessage());
	}
}
