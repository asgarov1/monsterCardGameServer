package at.fhtw.bif3.http.response;

import at.fhtw.bif3.http.request.HttpHeader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static at.fhtw.bif3.http.response.ContentType.APPLICATION_JSON;
import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpResponseTest {

    @Test
    void send() {

        String messageContent = "{`name`: `john`}".replace('`', '"');
        String expectedOutput = "HTTP/1.1 200 OK" + lineSeparator()
                + "Content-Type: " + APPLICATION_JSON.getName() + lineSeparator() + lineSeparator()
                + messageContent;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        var httpResponse = new HttpResponse();
        httpResponse.setStatusCode(HttpStatus.OK.getCode());
        httpResponse.addHeader(HttpHeader.CONTENT_TYPE.getName(),APPLICATION_JSON.getName());
        httpResponse.setContent(messageContent);

        httpResponse.send(baos);
        assertEquals(expectedOutput, new String(baos.toByteArray()));
    }
}