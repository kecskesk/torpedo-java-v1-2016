package com.torpedogame.v1.model.protocol;

/**
 * Created by Dombi Soma on 03/11/2016.
 */
public class CreateGameResponse extends Response {
    private int id;

    public CreateGameResponse(){
        super();
    }

    public CreateGameResponse(int id, String message, int code) {
        super(message, code);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return getCode() + " " + getId() + " " + getMessage();
    }
}
