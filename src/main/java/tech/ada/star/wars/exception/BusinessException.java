package tech.ada.star.wars.exception;


import org.springframework.http.HttpStatus;
import tech.ada.star.wars.controller.commons.MessageType;
import tech.ada.star.wars.controller.commons.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private List<ResponseMessage> errors;
    private final HttpStatus httpStatus;

    public BusinessException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BusinessException(List<ResponseMessage> errors) {
        this(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public BusinessException(List<ResponseMessage> errors, HttpStatus httpStatus) {
        this(httpStatus);
        this.errors = errors;
    }

    public BusinessException(ResponseMessage message) {
        this(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public BusinessException(ResponseMessage message, HttpStatus httpStatus) {
        this(new ArrayList<>(), httpStatus);
        this.addError(message);
    }

    public BusinessException(String text, String... params) {
        this(new ArrayList<>());
        errors.add(new ResponseMessage(text, MessageType.ERROR, params));
    }

    public List<ResponseMessage> getErrors() {
        return errors;
    }

    public void addError(ResponseMessage error) {
        this.errors.add(error);
    }

    public void addError(String text, String... params) {
        this.errors.add(new ResponseMessage(text, MessageType.ERROR, params));
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
