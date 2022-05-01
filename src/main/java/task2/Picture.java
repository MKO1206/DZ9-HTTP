package task2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.nio.file.Paths;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Picture {
    private final String url;

    public Picture(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return url.substring( url.lastIndexOf('/')+1);
    }
}
