package ru.croc.test.service;

/**
 *
 */
public class Log {

    private static Log instance;

    public static Log getInstance() {
        if(instance == null){
            instance = new Log();
        }
        return instance;
    }

    public void log(Object msg){
        System.out.println(msg);
    }
}
