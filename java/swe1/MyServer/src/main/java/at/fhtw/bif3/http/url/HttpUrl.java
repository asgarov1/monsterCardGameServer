package at.fhtw.bif3.http.url;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpUrl implements Url {

    private final String headerLine;

    private HttpUrl(String headerLine) {
        this.headerLine = headerLine;
    }

    public static Url valueOf(String headerLine) {
        return new HttpUrl(headerLine);
    }

    @Override
    public String getRawUrl() {
        return headerLine.substring(headerLine.indexOf("/"), headerLine.lastIndexOf(" "));
    }

    @Override
    public String getPath() {
        String rawUrl = getRawUrl();
        if(rawUrl.contains("?")){
            return rawUrl.substring(0, rawUrl.indexOf("?"));
        }
        return rawUrl;
    }

    @Override
    public Map<String, String> getParameter() {
        Map<String, String> parameterMap = new HashMap<>();
        String rawUrl = getRawUrl();
        if(getRawUrl().contains("?")) {
            String[] parameters = rawUrl.substring(rawUrl.indexOf("?")+1).split("&");
            for (String parameterPair : parameters) {
                String key = parameterPair.split("=")[0];
                String value = parameterPair.split("=")[1];
                parameterMap.put(key, value);
            }
        }
        return parameterMap;
    }

    @Override
    public int getParameterCount() {
        return getParameter().keySet().size();
    }

    @Override
    public String[] getSegments() {
        return getPath().substring(1).split("/");
    }

    @Override
    public String getFileName() {
        return getSegments()[getSegments().length - 1].contains(".") ? getSegments()[getSegments().length - 1] : "";
    }

    @Override
    public String getExtension() {
        String lastSegment = getSegments()[getSegments().length - 1];
        return lastSegment.contains(".") ? lastSegment.substring(lastSegment.lastIndexOf(".")) : "";
    }

    @Override
    public String getFragment() {
        return getRawUrl().contains("#") ? getRawUrl().substring(getRawUrl().lastIndexOf("#")+1) : "";
    }
}
