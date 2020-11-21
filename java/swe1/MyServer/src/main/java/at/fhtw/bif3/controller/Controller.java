package at.fhtw.bif3.controller;

import at.fhtw.bif3.http.request.Request;
import at.fhtw.bif3.http.response.HttpResponse;
import at.fhtw.bif3.http.response.HttpStatus;

import static at.fhtw.bif3.http.response.HttpStatus.*;

public interface Controller {
    HttpResponse handleRequest(Request request);

    default HttpResponse notFound() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(NOT_FOUND.getCode());
        return httpResponse;
    }

    default HttpResponse forbidden() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(FORBIDDEN.getCode());
        return httpResponse;
    }

    default HttpResponse noContent() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(NO_CONTENT.getCode());
        return httpResponse;
    }

    default HttpResponse internalServerError() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(INTERNAL_SERVER_ERROR.getCode());
        return httpResponse;
    }

    default HttpResponse badRequest() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(BAD_REQUEST.getCode());
        return httpResponse;
    }

    default HttpResponse created() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(CREATED.getCode());
        return httpResponse;
    }

    default HttpResponse unauthorized() {
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(UNAUTHORIZED.getCode());
        return httpResponse;
    }
}
