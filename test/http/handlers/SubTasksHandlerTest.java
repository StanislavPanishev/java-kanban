package http.handlers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Assertions;

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
import task.Subtask;
import task.Epic;

class SubTasksHandlerTest {

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
    void shouldReturnCode200AndSubTasksListWhenGETRequest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        Epic epic = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        taskManager.saveEpic(epic);
        taskManager.saveSubtask(new Subtask("Подготовка к переезду", "Полить кота", Status.NEW
                , LocalDateTime.now().plusHours(3), Duration.ofMinutes(60), 1));
        taskManager.saveSubtask(new Subtask("Подготовка к переезду", "Полить кота", Status.NEW
                , LocalDateTime.now().plusHours(5), Duration.ofMinutes(60), 1));
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(gson.toJson(taskManager.getSubtasksList()), response.body());
    }

    @Test
    void shouldReturnCode404WhenGETRequestAndSubTaskNotCreate() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/subtask/1");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(404, response.statusCode());
    }

    @Test
    void shouldReturnCode201WhenPOSTRequest() throws IOException, InterruptedException {
        Epic epic = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        taskManager.saveEpic(epic);
        taskManager.saveSubtask(new Subtask("Подготовка к переезду", "Полить кота", Status.NEW
                , LocalDateTime.now().plusHours(3), Duration.ofMinutes(60), 1));
        Subtask subTask2 = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW
                , LocalDateTime.now().plusHours(5), Duration.ofMinutes(60), 1);
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask2)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    void shouldReturnCode200WhenDELETERequest() throws IOException, InterruptedException {
        Epic epic = new Epic("Важный эпик 1", "Подготовка к переезду", Status.NEW,
                LocalDateTime.now().plusHours(2), Duration.ofMinutes(45));
        taskManager.saveEpic(epic);
        Subtask subtask = new Subtask("Подготовка к переезду", "Полить кота", Status.NEW
                , LocalDateTime.now().plusHours(5), Duration.ofMinutes(60), 1);
        taskManager.saveSubtask(subtask);
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        Assertions.assertEquals(200, response.statusCode(), "Значение кода не соответствует успеху");
    }

}