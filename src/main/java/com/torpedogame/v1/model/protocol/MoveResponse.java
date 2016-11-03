package com.torpedogame.v1.model.protocol;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class MoveResponse extends Response {

    public MoveResponse(){
        super();
    }

    public MoveResponse(String message, int code) {
        super(message, code);
    }

    @Override
    public String toString(){
        return getCode() + " " + getMessage();
    }
}
