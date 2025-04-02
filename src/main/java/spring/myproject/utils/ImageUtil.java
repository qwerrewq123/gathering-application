package spring.myproject.utils;

import org.springframework.http.HttpHeaders;

public class ImageUtil {

    public static void addContentTypeDefault(HttpHeaders headers){
        headers.add("Content-Type", "application/octet-stream");
    }

}
