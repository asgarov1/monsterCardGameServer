package at.fhtw.bif3.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpResponse implements Response {

    private HttpStatus status = HttpStatus.OK;
    private final Map<String, String> headers = new HashMap<>();
    private String content = "";
    private ContentType contentType;
    private String serverHeader;

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public int getContentLength() {
        return content.length();
    }

    @Override
    public String getContentType() {
        return contentType.getName();
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = Arrays.stream(ContentType.values()).filter(type -> type.getName().equals(contentType)).findFirst().orElseThrow();
    }

    @Override
    public int getStatusCode() {
        return status.getCode();
    }

    @Override
    public void setStatusCode(int status) {
        this.status = Arrays.stream(HttpStatus.values()).filter(httpStatus -> httpStatus.getCode() == status).findFirst().orElseThrow();
    }

    @Override
    public String getStatus() {
        return this.status.getMessage();
    }

    @Override
    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    @Override
    public String getServerHeader() {
        return this.serverHeader;
    }

    @Override
    public void setServerHeader(String server) {
        this.serverHeader = server;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setContent(byte[] content) {
        setContent(new ByteArrayInputStream(content));
    }

    @Override
    public void setContent(InputStream stream) {
        this.content = new BufferedReader(new InputStreamReader(stream))
                .lines().collect(Collectors.joining("\n"));
    }

    @SneakyThrows
    @Override
    public void send(OutputStream network) {
        var messageBuilder = new StringBuilder();

        messageBuilder.append("HTTP/1.1 ")
                .append(status.getCode()).append(" ")
                .append(status.getMessage()).append(lineSeparator());

        headers.keySet().forEach(key -> {
            messageBuilder.append(key).append(": ").append(headers.get(key)).append(lineSeparator());
        });

        messageBuilder.append(lineSeparator());
        messageBuilder.append(content);

        network.write(messageBuilder.toString().getBytes());
        network.flush();
    }
}

