package http.handlers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import http.HttpTaskServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import task.Status;
import task.Epic;


class EpicsHandlerTest {

    private static final int PORT = 8080;
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer();
    Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    void setUp() {
        taskManager.deleteAll();
        try {
            HttpTaskServer.startHttpTaskServer(taskManager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void shouldReturnCode200AndEpicListWhenGETRequest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        taskManager.saveEpic(new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45)));
        taskManager.saveEpic(new Epic("Важный эпик 2", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(4), Duration.ofMinutes(45)));
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(taskManager.getEpicsList()), response.body());
    }

    @Test
    void shouldReturnCode201WhenPOSTRequest() throws IOException, InterruptedException {
        Epic epic = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        URI uri = URI.create("http://localhost:" + PORT + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        epic.setId(1);
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    void shouldReturnCode200WhenDELETERequest() throws IOException, InterruptedException {
        Epic epic = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        taskManager.saveEpic(epic);
        URI uri = URI.create("http://localhost:" + PORT + "/epics?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
    }

}