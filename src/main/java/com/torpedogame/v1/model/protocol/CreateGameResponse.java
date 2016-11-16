package com.torpedogame.v1.model.protocol;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class CreateGameResponse extends Response {
    public CreateGameResponse(){
        super();
    }

    public CreateGameResponse(String message, int code) {
        super(message, code);
    }

    @Override
    public String toString(){
        return "Created game " + getCode() + ": " + getMessage();
    }
}
