package tech.ada.star.wars.controller.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class ResponseObject<T> {

	private T entity;

	private Page<T> items;

	private List<ResponseMessage> messages;

	public ResponseObject() {
		super();
	}

	public ResponseObject(T entity) {
		this();
		this.entity = entity;
	}

	public ResponseObject(List<T> itens) {
		this();
		items = new PageImpl<>(itens, Pageable.unpaged(), itens.size());
	}

	public ResponseObject(ResponseMessage mensagem) {
		addMessage(mensagem);
	}

	public static <T> ResponseObject<T> of(T target) {
		return new ResponseObject<>(target);
	}

	public static <T> ResponseObject<T> of(T target, ResponseMessage message) {
		ResponseObject response =  new ResponseObject<>(target);
		response.setMessages(new ArrayList<>());
		response.getMessages().add(message);
		return response;
	}

	public static ResponseObject<Void> of() {
		return new ResponseObject<>();
	}

	public static ResponseObject<Void> of(ResponseMessage message) {
		final ResponseObject<Void> responseObject = new ResponseObject<>();
		responseObject.addMessage(message);
		return responseObject;
	}

	public static <T> ResponseObject<T> page(Page<T> items) {
		final ResponseObject<T> responseObject = new ResponseObject<>();
		responseObject.setItems(items);
		return responseObject;
	}

	public static <T> ResponseObject<T> page(List<T> items) {
		return new ResponseObject<>(items);
	}

	public static <T> ResponseObject<T> message(List<ResponseMessage> messages) {
		ResponseObject<T> response =  new ResponseObject<>();
		response.setMessages(messages);
		return response;
	}
	
	public static <T> ResponseObject<T> message(ResponseMessage message) {
		return message(Arrays.asList(message));
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public Page<T> getItems() {
		return items;
	}

	public void setItems(Page<T> items) {
		this.items = items;
	}

	public List<ResponseMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ResponseMessage> messages) {
		this.messages = messages;
	}

	public void addMessage(ResponseMessage message) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(message);
	}

	public void addMessage(String text, MessageType type, Long time, String... params) {
		addMessage(new ResponseMessage(text, type, time, params));
	}

	public void addMessage(String text, MessageType type, String... params) {
		addMessage(new ResponseMessage(text, type, params));
	}
	
	public void addErrorMessage(String text, String... params) {
		addMessage(text, MessageType.ERROR, params);
	}

}
