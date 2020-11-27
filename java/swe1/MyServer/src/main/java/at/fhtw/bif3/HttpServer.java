package at.fhtw.bif3;

import at.fhtw.bif3.controller.dispatcher.FrontDispatcher;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    @SneakyThrows
    public static void main(String[] args) {
        int port = 8000;

        ServerSocket server = new ServerSocket(port);
        while (true) {
            new Thread(new FrontDispatcher(server.accept())).start();
        }
        //  TODO
        //    what does unconfigured deck mean? (echo 10)
        //    show configured deck with winning attributes?

    }
}



//TODO
    //