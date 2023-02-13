package tech.ada.star.wars.controller.commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean isKey;
	private String key;
	private String text;
	private Map<String, String> params;
	private MessageType type;
	private Long time;
	private static final Long DEFAULT_TIME_VIEW;

	static {
		DEFAULT_TIME_VIEW = Long.valueOf(5000);
	}

	public ResponseMessage() {
		super();
	}

	public ResponseMessage(String text, MessageType type, Long timeToLive, String... params) {
		super();
		this.text = text;
		this.type = type;
		this.time = timeToLive;
		this.isKey = Boolean.FALSE;
		this.params = new HashMap<>();
		this.addParams(params);
	}

	public ResponseMessage(String text, MessageType type, String... params) {
		this(text, type, DEFAULT_TIME_VIEW, params);
	}

	public static ResponseMessage success(String text, String... params) {
		return new ResponseMessage(text, MessageType.SUCCESS, params);
	}

	public static ResponseMessage error(String text, String... params) {
		return new ResponseMessage(text, MessageType.ERROR, params);
	}

	public static ResponseMessage info(String text, String... params) {
		return new ResponseMessage(text, MessageType.INFO, params);
	}

	private void addParams(String[] params) {
		for (int i = 0; i < params.length; i++) {
			this.addParams(String.valueOf(i), params[i]);
		}
	}

	public void addParams(String key, String value) {
		this.params.put(key, value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getIsKey() {
		return isKey;
	}

}
