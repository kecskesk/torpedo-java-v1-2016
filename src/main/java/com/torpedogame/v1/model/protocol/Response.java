package com.torpedogame.v1.model.protocol;

/**
 *
 * @author kkrisz
 */
public class Response {
    protected String message;
    protected int code;

    public Response(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public Response() {}    
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
