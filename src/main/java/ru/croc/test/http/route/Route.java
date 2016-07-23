package ru.croc.test.http.route;

import com.sun.net.httpserver.HttpExchange;

/**
 *
 */
public interface Route {

    void process(HttpExchange httpExchange);

}
