package springboot.reload.plugin.model;

import java.io.Serializable;

public class ReloadResponse<T> implements Serializable {

	private static final long serialVersionUID = -2692047411017703120L;
	/**
	 * 接口调用成功，不需要返回对象
	 */
	public static <T> ReloadResponse<T> newSuccess(){
		ReloadResponse<T> reloadResponse = new ReloadResponse<T>();
		return reloadResponse;
	}
	
	/**
	 * 接口调用成功，有返回对象
	 */
	public static <T> ReloadResponse<T> newSuccess(T object) {
		ReloadResponse<T> reloadResponse = new ReloadResponse<T>();
		reloadResponse.setObject(object);
		return reloadResponse;
	}
	
	/**
	 * 接口调用失败，有错误码和描述，没有返回对象
	 */
	public static <T> ReloadResponse<T> newFailure(int code, String message){
		ReloadResponse<T> reloadResponse = new ReloadResponse<T>();
		reloadResponse.setCode(code!=0 ? code : -1);
		reloadResponse.setMessage(message);
		return reloadResponse;
	}
	
	/**
	 * 接口调用失败，有错误字符串码和描述，没有返回对象
	 */
	public static <T> ReloadResponse<T> newFailure(String error, String message){
		ReloadResponse<T> reloadResponse = new ReloadResponse<T>();
		reloadResponse.setCode(-1);
		reloadResponse.setError(error);
		reloadResponse.setMessage(message);
		return reloadResponse;
	}
	
	/**
	 * 转换或复制错误结果
	 */
	public static <T> ReloadResponse<T> newFailure(ReloadResponse<?> failure){
		ReloadResponse<T> reloadResponse = new ReloadResponse<T>();
		reloadResponse.setCode(failure.getCode()!=0 ? failure.getCode() : -1);
		reloadResponse.setError(failure.getError());
		reloadResponse.setMessage(failure.getMessage());
		reloadResponse.setException(failure.getException());
		return reloadResponse;
	}
	
	/**
	 * 接口调用失败，返回异常信息
	 */
	public static <T> ReloadResponse<T> newException(Exception e){
		ReloadResponse<T> reloadResponse = new ReloadResponse<T>();
		reloadResponse.setCode(-1);
		reloadResponse.setException(e);
		reloadResponse.setMessage(e.getMessage());
		return reloadResponse;
	}
	
	private int code;
	private T object;
	private String error;
	private String message;
	private Exception exception;
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	
	
}