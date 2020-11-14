package at.fhtw.bif3.http.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static at.fhtw.bif3.http.request.HttpMethod.POST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpRequestTest {

    private static final int NUMBER_OF_HEADERS_IN_TEST_STREAM = 8;
    private static final String MESSAGE_CONTENT = "{`name`: `john`}".replace('`', '"');
    private static final String URL_PATH = "/packages";
    private static final String USER_AGENT = "PostmanRuntime/7.26.5";
    public static final String APPLICATION_JSON = "application/json";

    private final String request =
            "POST " + URL_PATH + " HTTP/1.1\r\n" +
                    "Content-Type: " + APPLICATION_JSON + "\r\n" +
                    "User-Agent: " + USER_AGENT + "\r\n" +
                    "Accept: */*\r\n" +
                    "Postman-Token: 938209d3-464e-47de-b141-25379d64404d\r\n" +
                    "Host: localhost:8000\r\n" +
                    "Accept-Encoding: gzip, deflate, br\r\n" +
                    "Connection: keep-alive\r\n" +
                    "Content-Length: " + MESSAGE_CONTENT.length() + "\r\n\r\n" +
                    MESSAGE_CONTENT + "\n\r";

    HttpRequest httpRequest;

    @BeforeEach
    public void init() {
        try {
            var inputStream = new ByteArrayInputStream(request.getBytes(StandardCharsets.UTF_8));
            httpRequest = HttpRequest.valueOf(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isValid() {
        assertTrue(httpRequest.isValid());
    }

    @Test
    void getMethod() {
        assertEquals(POST.name(), httpRequest.getMethod());
    }

    @Test
    void getUrl() {
        assertEquals("/packages", httpRequest.getUrl().getRawUrl());
    }

    @Test
    void getHeaders() {
        var headers = httpRequest.getHeaders();
        assertEquals(NUMBER_OF_HEADERS_IN_TEST_STREAM, headers.size());
        for (HttpHeader value : HttpHeader.values()) {
            assertTrue(headers.containsKey(value.getName()));
        }
    }

    @Test
    void getHeaderCount() {
        assertEquals(NUMBER_OF_HEADERS_IN_TEST_STREAM, httpRequest.getHeaderCount());
    }

    @Test
    void getUserAgent() {
        assertEquals(USER_AGENT, httpRequest.getUserAgent());
    }

    @Test
    void getContentLength() {
        int contentLength = MESSAGE_CONTENT.length();
        assertEquals(contentLength, httpRequest.getContentLength());
    }

    @Test
    void getContentType() {
        assertEquals(APPLICATION_JSON, httpRequest.getContentType());
    }

    @Test
    void getContentStream() {
        InputStream inputStream = httpRequest.getContentStream();
        var stringBuilder = new StringBuilder();
        for (int i = 0; i < httpRequest.getContentLength(); i++) {
            int read = 0;
            try {
                read = inputStream.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append((char) read);
        }

        assertEquals(MESSAGE_CONTENT, stringBuilder.toString());
    }

    @Test
    void getContentString() {
        assertEquals(MESSAGE_CONTENT, httpRequest.getContentString());
    }

    @Test
    void getContentBytes() {
        assertTrue(Arrays.equals(MESSAGE_CONTENT.getBytes(), httpRequest.getContentBytes()));
    }
}