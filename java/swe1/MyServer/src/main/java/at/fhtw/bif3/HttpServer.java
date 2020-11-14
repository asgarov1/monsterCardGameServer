package at.fhtw.bif3;

import at.fhtw.bif3.controller.Controller;
import lombok.SneakyThrows;

import java.net.ServerSocket;

public class HttpServer {

    @SneakyThrows
    public static void main(String[] args) {
        int port = 8000;

        ServerSocket server = new ServerSocket(port);
        while (true) {
            new Thread(new Controller(server.accept())).start();
        }

    }
}
