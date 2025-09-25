package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import ultility.LocalDateTimeAdapter;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public void startServer() throws IOException {
        server.createContext("/", new TaskHandler());
        server.start();
    }

    public void stop(){
        server.stop(0);
    }




    private class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            Integer taskId = null;
            String req = exchange.getRequestURI().getQuery();
            if (req != null && req.contains("id")) {
                taskId = Integer.parseInt(req.split("id=")[1]);
            }
            if (taskId != null) {
                path = path + "/?id=";
            }
            switch (method) {
                case "GET":
                    handleGetRequest(exchange, path, taskId);
                    break;
                case "POST":
                    handlePOSTRequest(exchange, path, taskId);
                    break;
                case "DELETE":
                    handleDELETERequest(exchange, path, taskId);
                    break;
            }


        }
    }

    private void handleGetRequest(HttpExchange exchange, String method, Integer taskId) throws IOException {
        switch (method) {
            case "/tasks/task/?id=":
                Task task = taskManager.getTaskById(taskId);
                if (task != null) {
                    sendResponseTask(exchange, 200, "OK", task);
                } else {
                    sendResponse(exchange, 404, "Not Found");
                }
                break;

            case "/tasks/task/":
                sendResponseTask(exchange, 200, "OK", taskManager.getTasks());
                break;

            case "/tasks/subtasks/epic/?id=":
                sendResponseSubTask(exchange, 200, "OK", taskManager.getEpicById(taskId).getSubTaskId());
                break;
            case "/tasks/history":
                sendResponseTask(exchange, 200, "OK", taskManager.getHistory());
                break;
            case "/tasks/":
                sendResponseTask(exchange, 200, "OK", taskManager.getPrioritizedTasks());
                break;
            case "/tasks/subtask/":
                sendResponseSubTask(exchange,200,"OK", taskManager.getSubtasks());
                break;
            case "/tasks/subtask/?id=":
                sendResponseSubTask(exchange,200,"OK",taskManager.getSubTaskById(taskId));
                break;
            case "/tasks/epic/":
                sendResponseEpic(exchange,200,"OK", taskManager.getEpics());
                break;
            case "/tasks/epic/?id=":
                sendResponseEpic(exchange,200,"OK",taskManager.getEpicById(taskId));
                break;
        }



    }

    private void handlePOSTRequest(HttpExchange exchange, String method, Integer taskId) throws IOException {
        switch (method) {
            case "/tasks/task/":
                handleTaskCreation(exchange);
                break;
            case "/tasks/subtask/":
                handleSubTaskCreation(exchange);
            case "/tasks/epic/":
                handleEpicCreation(exchange);
        }


    }

    private void handleDELETERequest(HttpExchange exchange, String method, Integer taskId) throws IOException {
        switch (method) {
            case "/tasks/task/?id=":
                taskManager.clearTaskById(taskId);
                sendResponse(exchange, 200, "OK");
                break;
            case "/tasks/task/":
                taskManager.clearTasks();
                sendResponse(exchange, 200, "OK");
                break;
            case "/tasks/subtask/?id=":
                taskManager.clearSubTaskById(taskId);
                sendResponse(exchange,200,"OK");
                break;
            case "/tasks/subtask/":
                taskManager.clearSubTask();
                sendResponse(exchange,200,"OK");
                break;
            case "/tasks/epic/?id=":
                taskManager.clearEpicById(taskId);
                sendResponse(exchange,200,"OK");
                break;
            case "/tasks/epic/":
                taskManager.clearEpic();
                sendResponse(exchange,200,"OK");
                break;
        }



    }

    private void sendResponse(HttpExchange exchange, int statusCode, String statusMessage) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);
        exchange.getResponseBody().close();
    }

    private void sendResponseTask(HttpExchange exchange, int statusCode, String statusMessage, Task responseData) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);
        OutputStream os = exchange.getResponseBody();
        String jsonResponse = gson.toJson(responseData);
        os.write(jsonResponse.getBytes());
        os.close();
    }

    private void sendResponseSubTask(HttpExchange exchange, int statusCode, String statusMessage, SubTask responseData) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);
        OutputStream os = exchange.getResponseBody();
        String jsonResponse = gson.toJson(responseData);
        os.write(jsonResponse.getBytes());
        os.close();
    }

    private void sendResponseEpic(HttpExchange exchange, int statusCode, String statusMessage, Epic responseData) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);
        OutputStream os = exchange.getResponseBody();
        String jsonResponse = gson.toJson(responseData);
        os.write(jsonResponse.getBytes());
        os.close();
    }

    private void sendResponseTask(HttpExchange exchange, int statusCode, String statusMessage, Collection<Task> responseData) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);
        OutputStream os = exchange.getResponseBody();
        String jsonResponse = gson.toJson(responseData);

        os.write(jsonResponse.getBytes());


        os.close();
    }

    private void sendResponseSubTask(HttpExchange exchange, int statusCode, String statusMessage, Collection<SubTask> responseData) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);
        OutputStream os = exchange.getResponseBody();
        String jsonResponse = gson.toJson(responseData);

        os.write(jsonResponse.getBytes());


        os.close();
    }

    private void sendResponseEpic(HttpExchange exchange, int statusCode, String statusMessage, Collection<Epic> responseData) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);
        OutputStream os = exchange.getResponseBody();
        String jsonResponse = gson.toJson(responseData);

        os.write(jsonResponse.getBytes());


        os.close();
    }

    private void sendResponseSubTask(HttpExchange exchange, int statusCode, String statusMessage, List<Integer> responseData) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, 0);
        OutputStream os = exchange.getResponseBody();
        String jsonResponse = gson.toJson(responseData);
        os.write(jsonResponse.getBytes());
        os.close();
    }

    private void handleTaskCreation(HttpExchange exchange) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody());
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String body = bufferedReader.lines().collect(Collectors.joining("\n"));
        try {
            Task task = gson.fromJson(body, Task.class);
            if (task != null && task.getName() != null && task.getDescription() != null) {
                taskManager.createTask(task);
                sendResponse(exchange, 201, "Created");
            } else {
                sendResponse(exchange, 400, "Bad Request - Missing Required Parameters");
            }
        } catch (JsonSyntaxException e) {
            sendResponse(exchange, 400, "Bad Request - Invalid JSON Format");
        }
    }

    private void handleSubTaskCreation(HttpExchange exchange) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody());
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String body = bufferedReader.lines().collect(Collectors.joining("\n"));
        try {
            SubTask subTask = gson.fromJson(body, SubTask.class);
            if (subTask != null && subTask.getName() != null && subTask.getDescription() != null && subTask.getEpicId()!=null && taskManager.getEpicById(subTask.getEpicId())!=null) {
                taskManager.createTask(subTask);
                sendResponse(exchange, 201, "Created");
            } else {
                sendResponse(exchange, 400, "Bad Request - Missing Required Parameters");
            }
        } catch (JsonSyntaxException e) {
            sendResponse(exchange, 400, "Bad Request - Invalid JSON Format");
        }
    }

    private void handleEpicCreation(HttpExchange exchange) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody());
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String body = bufferedReader.lines().collect(Collectors.joining("\n"));
        try {
            Epic epic = gson.fromJson(body, Epic.class);
            if (epic != null && epic.getName() != null && epic.getDescription() != null ) {
                taskManager.createTask(epic);
                sendResponse(exchange, 201, "Created");
            } else {
                sendResponse(exchange, 400, "Bad Request - Missing Required Parameters");
            }
        } catch (JsonSyntaxException e) {
            sendResponse(exchange, 400, "Bad Request - Invalid JSON Format");
        }
    }


}
