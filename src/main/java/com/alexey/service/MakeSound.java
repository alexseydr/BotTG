package com.alexey.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class MakeSound {
    public InputStream makeSound(String text) {
        try {
            String url = "http://localhost:8000/tts?text=" + java.net.URLEncoder.encode(text, "UTF-8");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            System.out.println("Аудио успешно получено");
            return response.body(); // вернём InputStream

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

