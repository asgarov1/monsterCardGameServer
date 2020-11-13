package at.fhtw.bif3.http;

import at.fhtw.bif3.http.request.HttpRequest;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer {
    public static void main(String[] args) {
        int port = 8000;

        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                new Thread(new HttpRequest(server.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
