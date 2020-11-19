package at.fhtw.bif3.controller;

import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpStatus;

public interface Controller {
    HttpStatus handleRequest(Request request);
}
