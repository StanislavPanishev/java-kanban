package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotAcceptableException;
import exception.ManagerSaveException;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandler implements HttpHandler {

    TaskManager taskManager;
    Gson gson;

    public EpicsHandler(TaskManager taskManager, Gson gson) {
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
                    if (path.contains("/epics/") && path.split("/").length == 3) {
                        if (checkIdFromPathInManager(path)) {
                            responseCode = 404;
                            response = "Not Found1";
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getEpic(getIdFromPath(path)));
                        }
                    } else if (path.contains("/epics/") && path.split("/")[3].equals("subtasks")) {
                        if (checkIdFromPathInManager(path)) {
                            responseCode = 404;
                            response = "Not Found2";
                        } else {
                            int id = Integer.parseInt(path.split("/")[2]);
                            responseCode = 200;
                            response = gson.toJson(taskManager.subtaskList(id));
                        }
                    } else if (path.equals("/epics")) {
                        responseCode = 200;
                        response = gson.toJson(taskManager.getEpicsList());
                    } else {
                        responseCode = 404;
                        response = "Not Found3";
                    }
                    break;
                } catch (NumberFormatException exception) {
                    responseCode = 404;
                    response = "Not Found";
                }
            case "POST":
                try {
                    Epic epicFromJson = gson.fromJson(bodyString, Epic.class);
                    if (epicFromJson.getId() == 0) {
                        responseCode = 201;
                        response = gson.toJson(taskManager.createEpic(epicFromJson));
                    } else {
                        taskManager.updateEpic(epicFromJson);
                        responseCode = 201;
                        response = gson.toJson(taskManager.getEpic(epicFromJson.getId()));
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
                taskManager.deleteEpic(id);
                responseCode = 200;
                response = "Задача удалена";
                break;
        }
        httpExchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("Код ответа: " + responseCode + "; тело ответа: " + response);
    }

    boolean checkIdFromPathInManager(String path) {
        int epicId = getIdFromPath(path);
        return (taskManager.getEpic(epicId) == null);
    }

    int getIdFromPath(String path) {
        return Integer.parseInt(path.split("/")[2]);
    }

}