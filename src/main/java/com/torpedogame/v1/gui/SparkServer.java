package com.torpedogame.v1.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import static spark.Spark.before;

/**
 *
 * @author kkrisz
 */
public class SparkServer extends Thread {

    private GuiInfoMessage guiInfoMessage = new GuiInfoMessage();
    private GuiMoveRequest guiMoveRequest = null;

    /**
     * Very simple REST endpoint
     */
    @Override
    public void run() {
        enableCORS("*", "*", "*");
        Spark.get(new Route("/rest/getInfos") {
            @Override
            public Object handle(final Request request,
                    final Response response) {

                ObjectMapper mapper = new ObjectMapper();
                String jsonToSend = "";
                try {
                    jsonToSend = mapper.writeValueAsString(guiInfoMessage);
                } catch (Exception e) {
                    System.out.println("Error sending out gui data");
                }

                return jsonToSend;
            }
        });

        Spark.post(new Route("/rest/move") {
            @Override
            public Object handle(final Request request, final Response response) {
                int submarineId = Integer.valueOf(request.queryParams("id"));
                double x = Double.valueOf(request.queryParams("x"));
                double y = Double.valueOf(request.queryParams("y"));
                guiMoveRequest = new GuiMoveRequest(submarineId, x, y);
                return "ok";
            }
        });
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
                response.header("Access-Control-Allow-Headers", headers);
            }
        });
    }

    public void updateMessage(GuiInfoMessage guiInfoMessage) {
        this.guiInfoMessage = guiInfoMessage;
    }

    public GuiMoveRequest getGuiMoveRequest() {
        return guiMoveRequest;
    }
}
