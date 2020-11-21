package at.fhtw.bif3.controller.dispatcher;

import at.fhtw.bif3.controller.*;
import at.fhtw.bif3.http.request.HttpRequest;
import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.Response;
import lombok.SneakyThrows;

import java.net.Socket;

import static at.fhtw.bif3.http.response.HttpStatus.NOT_FOUND;

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
            response = handle(request);
        } catch (IllegalArgumentException e) {
            response.setStatusCode(NOT_FOUND.getCode());
        }
        return response;
    }

    private HttpResponse handle(Request request) {
        String path = request.getUrl().getSegments()[0];
        Controller controller = switch (path) {
            case "users" -> new UsersController();
            case "sessions" -> new SessionsController();
            case "packages" -> new PackagesController();
            case "transactions" -> new TransactionsController();
            case "cards" -> new CardsController();
            case "deck" -> new DeckController();
            case "stats" -> new StatsController();
            case "score" -> new ScoreController();
            case "battles" -> new BattlesController();
            case "tradings" -> new TradingsController();
            default -> throw new IllegalArgumentException("Unexpected url path: " + path);
        };
        return controller.handleRequest(request);
    }
}

//TODO question: my controllers return Response - I think that is correct? not sure what the echos stand for
