package com.torpedogame.v1.model.protocol;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class JoinGameResponse {
    private String message;
    private int code;

    public JoinGameResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

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
