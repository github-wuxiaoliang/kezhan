package com.br.cobra.web.model;

public class ResponseDto<T> {

    private int code;
    private String message;
    private T result;
    
    public ResponseDto(){
        this.code = 0;
    }
    
    public ResponseDto(int code,String message){
        this.code = code;
        this.message = message;
    }
    
    public ResponseDto(T result){
        this.code = 0;
        this.result = result;
    }
    /**
     * @return the {@link #code}
     */
    public int getCode() {
        return code;
    }
    /**
     * @param code
     * the {@link #code} to set
     */
    public void setCode(int code) {
        this.code = code;
    }
    /**
     * @return the {@link #message}
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     * the {@link #message} to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the {@link #result}
     */
    public T getResult() {
        return result;
    }
    /**
     * @param result
     * the {@link #result} to set
     */
    public void setResult(T result) {
        this.result = result;
    }
    
}
