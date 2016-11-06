package com.torpedogame.v1;


import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 *
 * @author kkrisz
 */
public class SparkServer extends Thread {

    @Override
    public void run() {
        Spark.get(new Route("/rest/getInfos") {
            @Override
            public Object handle(final Request request,
                                 final Response response) {
                return "Hello World From Spark\n";
            }
        });
    }
    
}
