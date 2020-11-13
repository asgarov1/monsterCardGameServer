package at.fhtw.bif3.http.request;

import at.fhtw.bif3.http.url.HttpUrl;
import at.fhtw.bif3.http.url.Url;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static at.fhtw.bif3.http.request.RequestHeader.*;
import static java.lang.System.lineSeparator;

@Getter
public class HttpRequest implements Request, Runnable {

    private final Socket socket;
    private Method method;
    private Url url;
    private final Map<String, String> headers = new HashMap<>();
    private String messageBody;

    @SneakyThrows
    public HttpRequest(Socket socket) {
        this.socket = socket;
    }

    @Override
    public boolean isValid() {
        return url != null;
    }

    @Override
    public String getMethod() {
        return method.name();
    }

    @Override
    public Url getUrl() {
        return url;
    }

    @Override
    public Map<String, String> getHeaders() { return headers; }

    @Override
    public int getHeaderCount() {
        return getHeaders().size();
    }

    @Override
    public String getUserAgent() {
        return getHeaders().get(USER_AGENT.getName());
    }

    @Override
    public int getContentLength() {
        return getHeaders().containsKey(CONTENT_LENGTH.getName()) ? Integer.parseInt(getHeaders().get(CONTENT_LENGTH.getName())) : 0;
    }

    @Override
    public String getContentType() {
        return getHeaders().getOrDefault(CONTENT_TYPE.getName(), "");
    }

    @Override
    @SneakyThrows
    public InputStream getContentStream() {
        if (getHeaders().containsKey(CONTENT_LENGTH.getName())) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                //skipping to content;
            }
            return socket.getInputStream();
        }
        return null;
    }

    @Override
    public String getContentString() {
        return messageBody;
    }

    @Override
    public byte[] getContentBytes() {
        return messageBody.getBytes();
    }

    @Override
    public void run() {
        processRequest();
    }

    @SneakyThrows
    private void processRequest() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            var requestBuilder = new StringBuilder();

            String headerLine = skipToHeaderLine(in);
            if (headerLine == null) {
                return;
            }

            readMethodAndUrl(headerLine);

            requestBuilder.append(headerLine).append(lineSeparator());
            requestBuilder.append(setHeaders(in));

            this.messageBody = readMessageBody(in);
            requestBuilder.append(messageBody).append(lineSeparator());
            out.write("HTTP/1.0 200 OK\r\n");
            System.out.println(requestBuilder.toString());
        } finally {
            socket.close();
        }
    }

    private void readMethodAndUrl(String headerLine) {
        setHttpMethod(headerLine);
        url = HttpUrl.valueOf(headerLine);
    }

    @SneakyThrows
    private String readMessageBody(BufferedReader in) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!method.equals(Method.GET) && getHeaders().containsKey(CONTENT_LENGTH.getName())) {
            for (int i = 0; i < getContentLength(); i++) {
                int read = in.read();
                stringBuilder.append((char) read);
            }
        }
        return stringBuilder.toString();
    }

    private StringBuilder setHeaders(BufferedReader in) throws IOException {
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append(lineSeparator());
            headers.put(line.split(": ")[0], line.split(": ")[1]);
        }
        return requestBuilder;
    }

    private void setHttpMethod(String headerLine) {
        if (headerLine.contains(Method.GET.name())) {
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
