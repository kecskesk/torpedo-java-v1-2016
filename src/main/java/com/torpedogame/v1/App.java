package com.torpedogame.v1;

import com.torpedogame.v1.service.GameAPI;
import com.torpedogame.v1.service.GameApiImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        GameAPI gameAPI = new GameApiImpl();
        System.out.print(gameAPI.createGame().toString());
    }
}
