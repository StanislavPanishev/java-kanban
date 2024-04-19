package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotAcceptableException;
import exception.ManagerSaveException;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SubTasksHandler implements HttpHandler {

    TaskManager taskManager;
    Gson gson;

    public SubTasksHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        InputStream inputStream = httpExchange.getRequestBody();
        String bodyString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        int responseCode = 0;
        String response = "";

        switch (method) {
            case "GET":
                try {
                    if (path.contains("/subtasks/") && path.split("/").length == 3) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        if (taskManager.getSubtask(id) == null) {
                            responseCode = 404;
                            response = "Not FoundNot Found";
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getSubtask(id));
                        }
                    } else if (path.equals("/subtasks")) {
                        responseCode = 200;
                        response = gson.toJson(taskManager.getSubtasksList());
                    } else {
                        responseCode = 404;
                        response = "Not Found";
                    }
                    break;
                } catch (NumberFormatException exception) {
                    responseCode = 404;
                    response = "Not Found";
                }
            case "POST":
                try {
                    Subtask subTaskFromJson = gson.fromJson(bodyString, Subtask.class);
                    if (subTaskFromJson.getId() == 0) {
                        int epicId = subTaskFromJson.getEpicID();
                        responseCode = 201;
                        response = gson.toJson(taskManager.createSubtask(subTaskFromJson));
                    } else {
                        taskManager.updateSubtask(subTaskFromJson);
                        responseCode = 201;
                        response = gson.toJson(taskManager.getSubtask(subTaskFromJson.getId()));
                    }
                    break;
                } catch (NotAcceptableException exception) {
                    responseCode = 406;
                    response = "Not Acceptable";
                    break;
                } catch (ManagerSaveException exception) {
                    responseCode = 500;
                    response = "Internal Server Error";
                    break;
                }
            case "DELETE":
                int id = Integer.parseInt(query.substring(3));
                taskManager.deleteSubtask(id);
                responseCode = 200;
                response = "Задача удалена";
                break;
        }
        httpExchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}