package com.torpedogame.v1.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Filter;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

/**
 *
 * @author kkrisz
 */
public class SparkServer extends Thread {

    public SparkServer(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
    }    

    private GuiInfoMessage guiInfoMessage = new GuiInfoMessage();
    private GuiMoveRequest guiMoveRequest = null;
    
    private final int width;
    private final int heigth;

    /**
     * Very simple REST endpoint
     */
    @Override
    public void run() {
        enableCORS("*", "*", "*");
        get("/rest/getInfos",(req, resp) -> {
                ObjectMapper mapper = new ObjectMapper();
                String jsonToSend = "";
                try {
                    jsonToSend = mapper.writeValueAsString(guiInfoMessage);
                } catch (Exception e) {
                    System.out.println("Error sending out gui data");
                }

                return jsonToSend;
            }
        );

        post("/rest/move", (req, resp) -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                GuiMoveRequest tempRequest = mapper.readValue(req.body(), GuiMoveRequest.class);
                
                if (isOutOfMap(tempRequest)) {
                    return "\"not ok\"";
                } else {
                    guiMoveRequest = mapper.readValue(req.body(), GuiMoveRequest.class);
                    return  "\"ok\"";
                }
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "\"not ok\"";
            }
        });

        options("rest/move", (req, resp) -> "\"ok\"");
    }

    /**
     * we have to turn on this in order to avoid CORS errors
     *
     * @param origin
     * @param methods
     * @param headers
     */
    private static void enableCORS(final String origin, final String methods,
            final String headers) {
        before(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                response.header("Access-Control-Allow-Origin", origin);
                response.header("Access-Control-Request-Method", methods);
                response.header("Access-Control-Allow-Headers", "content-type");
                response.type("application/json");
            }
        });
    }

    public void updateMessage(GuiInfoMessage guiInfoMessage) {
        this.guiInfoMessage = guiInfoMessage;
    }

    public GuiMoveRequest getGuiMoveRequest() {
        return guiMoveRequest;
    }

    public void clearMoveRequest() {
        guiMoveRequest = null;
    }

    private boolean isOutOfMap(GuiMoveRequest tempRequest) {
        double tWidth = tempRequest.getX();
        double tHeight = tempRequest.getY();
        
        if ((tWidth < 0 || tWidth > width) || (tHeight < 0 || tHeight > heigth)) {
            return true;
        }
        else {
            return false;
        }
    }
}
