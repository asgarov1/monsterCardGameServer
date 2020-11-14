package at.fhtw.bif3.controller;

import at.fhtw.bif3.http.request.HttpHeader;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.ContentType;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import lombok.SneakyThrows;

import java.net.Socket;

public class Controller implements Runnable {

    private final Socket connectionSocket;
    private Request request;

    public Controller(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    @SneakyThrows
    public void run() {
        request = HttpRequest.valueOf(connectionSocket.getInputStream());

        var response = new HttpResponse();
        response.setStatusCode(HttpStatus.OK.getCode());
        response.addHeader(HttpHeader.CONTENT_TYPE.getName(), ContentType.TEXT_HTML.getName());
        response.send(connectionSocket.getOutputStream());

        connectionSocket.close();
    }
}
