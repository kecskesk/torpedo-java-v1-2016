package com.torpedogame.v1.model.protocol;

import java.util.List;

/**
 *
 * @author kkrisz
 */
public class SubmarinesResponse extends Response {
    private List<Submarine> submarines;

    public SubmarinesResponse(List<Submarine> submarines, String message, int code) {
        super(message, code);
        this.submarines = submarines;
    }

    public List<Submarine> getSubmarines() {
        return submarines;
    }

    public void setSubmarines(List<Submarine> submarines) {
        this.submarines = submarines;
    }
}
