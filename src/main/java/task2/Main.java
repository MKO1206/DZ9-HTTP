package task2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class Main {
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        CloseableHttpClient httpClient = buildClient();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=QqhAgpdxRVkCHE5ap6Aiw5QQH6ROsSydjJceutrO");
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println(String.format("Данные получены. Статус ответа сервера: %s", response.getStatusLine()));
                Picture picture = mapper.readValue(response.getEntity().getContent(), new TypeReference<Picture>() {
                });
                HttpGet picRequest = new HttpGet(picture.getUrl());
                CloseableHttpResponse picResponse = httpClient.execute(picRequest);
                InputStream inputStream =  picResponse.getEntity().getContent();
                Files.copy(inputStream, Paths.get(picture.getFileName()),StandardCopyOption.REPLACE_EXISTING); //Сохраняет изображение в корень проекта
            } else {
                System.out.println(String.format("Данные не получены. Статус ответа сервера: %s", response.getStatusLine()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static CloseableHttpClient buildClient() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        return httpClient;
    }
}
