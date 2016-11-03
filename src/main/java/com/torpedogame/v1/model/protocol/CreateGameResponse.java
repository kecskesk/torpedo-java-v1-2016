package com.torpedogame.v1.model.protocol;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class CreateGameResponse {
    private String message;
    private int code;
    private int id;

    public CreateGameResponse(){}

    public CreateGameResponse(int code, int id, String message) {
        this.code = code;
        this.id = id;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString(){
        return getCode() + " " + getId() + " " + getMessage();
    }
}
