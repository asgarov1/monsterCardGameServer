package at.fhtw.bif3;

import at.fhtw.bif3.controller.dispatcher.FrontDispatcher;
import lombok.SneakyThrows;

import java.net.ServerSocket;

import static at.fhtw.bif3.util.PropertiesReader.getProperties;
import static java.lang.Integer.parseInt;

public class HttpServer {

    @SneakyThrows
    public static void main(String[] args) {
        int port = parseInt(getProperties().getProperty("port"));
        ServerSocket server = new ServerSocket(port);
        while (true) {
            new Thread(new FrontDispatcher(server.accept())).start();
        }
    }
}