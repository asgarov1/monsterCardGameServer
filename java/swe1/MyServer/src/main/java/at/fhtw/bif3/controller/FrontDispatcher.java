package at.fhtw.bif3.controller;

import at.fhtw.bif3.controller.handler.*;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;
import at.fhtw.bif3.http.response.Response;
import lombok.SneakyThrows;

import java.net.Socket;

public class FrontDispatcher implements Runnable {

    private final Socket connectionSocket;

    public FrontDispatcher(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    @SneakyThrows
    public void run() {
        Request request = HttpRequest.valueOf(connectionSocket.getInputStream());
        Response response = getResponse(request);
        response.send(connectionSocket.getOutputStream());
        connectionSocket.close();
    }

    private HttpResponse getResponse(Request request) {
        var response = new HttpResponse();
        try {
            HttpStatus status = handle(request);
//            response.setStatusCode(status.getCode());
        } catch (IllegalArgumentException e) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.getCode());
        }
        return response;
    }

    private HttpStatus handle(Request request) {
        String path = request.getUrl().getPath();
        Controller controller;
        switch (path) {
            case "/users" -> controller = new UserController();
            case "/sessions" -> controller = new SessionController();
            case "/packages" -> controller = new PackageController();
            case "/transactions" -> controller = new TransactionController();
            case "/cards" -> controller = new CardController();
            case "/deck" -> controller = new DeckController();
            case "/stats" -> controller = new StatsController();
            case "/score" -> controller = new ScoreController();
            case "/battles" -> controller = new BattlesController();
            case "/tradings" -> controller = new TradingController();
            default -> throw new IllegalArgumentException("Unexpected url path: " + path);
        }
        return controller.handleRequest(request);
    }
}
