package http.handlers;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import manager.TaskManager;
import http.HttpTaskServer;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

class PrioritizedHandlerTest {

    private static final int PORT = 8080;
    private TaskManager taskManager = new InMemoryTaskManager();
    private HttpTaskServer server = new HttpTaskServer();

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
    void handle() throws IOException, InterruptedException {
        taskManager.createTask(new Task("task1", "Повторить матерал", Status.NEW
                , LocalDateTime.now(), Duration.ofMinutes(100)));
        URI uri = URI.create("http://localhost:" + PORT + "/prioritized");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(1, response.body().split("},").length);
    }

}