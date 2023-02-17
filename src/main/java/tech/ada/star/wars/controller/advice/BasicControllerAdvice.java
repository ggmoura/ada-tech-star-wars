package tech.ada.star.wars.controller.advice;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.ada.star.wars.controller.commons.MessageType;
import tech.ada.star.wars.controller.commons.ResponseMessage;
import tech.ada.star.wars.controller.commons.ResponseObject;
import tech.ada.star.wars.exception.BusinessException;

import java.util.List;


@ControllerAdvice
@ResponseBody
public class BasicControllerAdvice {

	private static final String DEFAULT_ERROR_MESSAGE = "Request error, check o payload e o header attributes: {0}!";
	private final Logger logger;

	public BasicControllerAdvice(final Logger logger) {
		super();
		this.logger = logger;
	}

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<ResponseObject<Void>> handleMissingPathVariableException(MissingPathVariableException ex) {
		return new ResponseEntity<>(ResponseObject.message(ResponseMessage.error(ex.getLocalizedMessage())), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseObject<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		final List<FieldError> fieldErrors = ex.getFieldErrors();
		ResponseObject<Void> response = new ResponseObject<>();
		fieldErrors.forEach(err -> response.addMessage(ResponseMessage.error("{0}, ".concat(err.getDefaultMessage()),
				err.getField())));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ResponseObject<Void>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex)
	{
		ResponseObject<Void> response = buildResponseObject(DEFAULT_ERROR_MESSAGE, "MethodArgumentNotValid");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseObject<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		ResponseObject<Void> response = buildResponseObject(DEFAULT_ERROR_MESSAGE, ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ResponseObject<Void>> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		ResponseObject<Void> response = buildResponseObject(DEFAULT_ERROR_MESSAGE, "HttpRequestMethodNotSupported");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageConversionException.class)
	public ResponseEntity<ResponseObject<Void>> handleHttpMessageConversionException(HttpMessageConversionException ex) {
		ResponseObject<Void> response = buildResponseObject(DEFAULT_ERROR_MESSAGE, ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseObject<Void>> handleExceptions(Exception ex) {
		logger.error("Error Interno", ex);
		ResponseObject<Void> response = new ResponseObject<>();
		response.addMessage(
				"Ocorreu um erro inesperado, os responsáveis já foram notificados" +
				"e logo será solucionado, tente mais tarde!", MessageType.ERROR, "internalError");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ResponseObject<Void>> businessExceptions(BusinessException ex) {
		ResponseObject<Void> response = new ResponseObject<>();
		response.setMessages(ex.getErrors());
		return new ResponseEntity<>(response, ex.getHttpStatus());
	}

	private ResponseObject<Void> buildResponseObject(final String message, final String param) {
		ResponseObject<Void> response = new ResponseObject<>();
		response.addMessage(message, MessageType.ERROR, param);
		return response;
	}

}