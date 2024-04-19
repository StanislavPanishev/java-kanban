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
import task.Task;

class TasksHandlerTest {

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
    void shouldReturnCode200WhenGETRequest() throws InterruptedException, IOException {
        URI uri = URI.create("http://localhost:" + PORT + "/tasks");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void shouldReturnCode404WhenGETRequest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(404, response.statusCode(), "Not Found");
    }

    @Test
    void shouldReturnCode201WhenPOSTRequest() throws IOException, InterruptedException {
        taskManager.createTask(new Task("task1", "Повторить матерал", Status.NEW
                , LocalDateTime.now(), Duration.ofMinutes(100)));
        Task task2 = new Task("task2", "Повторить матерал", Status.NEW
                , LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        URI uri = URI.create("http://localhost:" + PORT + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    void shouldReturnCode200WhenDELETERequest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/tasks?id=1");
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