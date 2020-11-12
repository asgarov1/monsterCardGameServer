package at.fhtw.bif3.http;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest implements Request, Runnable {

    private final Socket socket;
    private Method method;
    private final Map<String, String> headers = new HashMap<>();

    public HttpRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public Url getUrl() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public int getHeaderCount() {
        return 0;
    }

    @Override
    public String getUserAgent() {
        return null;
    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public InputStream getContentStream() {
        return null;
    }

    @Override
    public String getContentString() {
        return null;
    }

    @Override
    public byte[] getContentBytes() {
        return new byte[0];
    }

    @Override
    public void run() {
        processRequest();
    }

    private void processRequest() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            StringBuilder requestBuilder = new StringBuilder();

            String headerLine = skipToHeaderLine(in);
            if(headerLine == null){ return; }
            setHttpMethod(headerLine);

            requestBuilder.append(headerLine).append(System.lineSeparator());
            requestBuilder.append(readHeaders(in));

            requestBuilder.append(readAdditionalContent(in));
//            out.write("HTTP/1.0 200 OK\r\n");
            System.out.println(requestBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private StringBuilder readAdditionalContent(BufferedReader in) {
        StringBuilder stringBuilder = new StringBuilder();
        if(!method.equals(Method.GET) && getHeaders().containsKey("Content-Length")){
            int contentLength = Integer.parseInt(getHeaders().get("Content-Length"));
            for (int i = 0; i < contentLength; i++) {
                try {
                    stringBuilder.append((char) in.read());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder;
    }

    private StringBuilder readHeaders(BufferedReader in) throws IOException {
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append(System.lineSeparator());
            headers.put(line.split(": ")[0], line.split(": ")[1]);
        }
        return requestBuilder;
    }

    private void setHttpMethod(String headerLine) {
        if(headerLine.contains(Method.GET.name())){
            method = Method.GET;
        } else if (headerLine.contains(Method.POST.name())) {
            method = Method.POST;
        } else if (headerLine.contains(Method.PUT.name())) {
            method = Method.PUT;
        } else if (headerLine.contains(Method.DELETE.name())) {
            method = Method.DELETE;
        }
    }

    private String skipToHeaderLine(BufferedReader in) throws IOException {
        String line = in.readLine();
        while (line != null && line.isEmpty()) {
            line = in.readLine();
        }
        return line;
    }
}
