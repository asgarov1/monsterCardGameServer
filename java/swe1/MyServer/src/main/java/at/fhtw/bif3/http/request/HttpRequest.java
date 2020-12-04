package at.fhtw.bif3.http.request;

import at.fhtw.bif3.http.url.HttpUrl;
import at.fhtw.bif3.http.url.Url;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static at.fhtw.bif3.http.request.HttpHeader.*;
import static java.lang.Integer.parseInt;
import static java.lang.System.in;
import static java.lang.System.lineSeparator;

@Getter
public class HttpRequest implements Request {

    private final String receivedRequest;

    private HttpRequest(InputStream inputStream) throws IOException {
        receivedRequest = readRequest(inputStream);
    }

    public static HttpRequest valueOf(InputStream inputStream) throws IOException {
        return new HttpRequest(inputStream);
    }

    private String readRequest(InputStream inputStream) throws IOException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            var requestBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append(lineSeparator());
            }
            if (requestBuilder.toString().contains(CONTENT_LENGTH.getName())) {
                for (int i = 0; i < extractContentLength(requestBuilder); i++) {
                    requestBuilder.append((char) in.read());
                }
            }
            return requestBuilder.toString();
        } finally {
            in.close();
        }
    }

    private int extractContentLength(StringBuilder request) {
        String contentLengthLine = request.substring(request.indexOf(CONTENT_LENGTH.getName()));
        int start = contentLengthLine.indexOf(": ") + ": ".length();
        int end = contentLengthLine.indexOf("\r");
        return parseInt(contentLengthLine.substring(start, end));
    }

    private ByteArrayInputStream getInputStream() {
        return new ByteArrayInputStream(receivedRequest.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean isValid() {
        try {
            HttpUrl.valueOf(getHeaderLine());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getMethod() {
        String headerLine = getHeaderLine();
        return headerLine.substring(0, headerLine.indexOf(" /"));
    }

    @Override
    public Url getUrl() {
        return HttpUrl.valueOf(getHeaderLine());
    }

    @Override
    public Map<String, String> getHeaders() {
        return readHeaders();
    }

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
        return getHeaders().containsKey(CONTENT_LENGTH.getName()) ? parseInt(getHeaders().get(CONTENT_LENGTH.getName())) : 0;
    }

    @Override
    public String getContentType() {
        return getHeaders().getOrDefault(CONTENT_TYPE.getName(), "");
    }

    @Override
    @SneakyThrows
    public InputStream getContentStream() {
        return new ByteArrayInputStream(getContentString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String getContentString() {
        return Arrays.stream(receivedRequest
                .split("\n"))
                .filter(line -> line.contains("[") || line.contains("{"))
                .map(line -> line.replace("\r", ""))
                .findFirst()
                .orElseGet(this::getLastLine);
    }

    private String getLastLine() {
        return receivedRequest.split("\n")[receivedRequest.split("\n").length - 1];
    }

    @Override
    public byte[] getContentBytes() {
        return getContentString().getBytes();
    }

    private Map<String, String> readHeaders() {
        Map<String, String> headers = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.contains(":") && !line.contains("{")) {
                    headers.put(line.split(": ")[0], line.split(": ")[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headers;
    }

    private String getHeaderLine() {
        String line = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream()))) {
            while ((line = in.readLine()) != null && line.isEmpty()) { /* skipping to the headerLine */ }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}