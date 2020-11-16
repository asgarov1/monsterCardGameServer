package at.fhtw.bif3;

import at.fhtw.bif3.controller.FrontDispatcher;
import lombok.SneakyThrows;

import java.net.ServerSocket;

public class HttpServer {

    @SneakyThrows
    public static void main(String[] args) {
        int port = 8000;

        ServerSocket server = new ServerSocket(port);
        while (true) {
            new Thread(new FrontDispatcher(server.accept())).start();
        }

        //TODO: how does authorization work?
        //  what does unconfigured deck mean? (echo 10)
        //  show configured deck with winning attributes?
        //  stats? whose stats?
        //  start /b "kienboec battle"
    }
}
