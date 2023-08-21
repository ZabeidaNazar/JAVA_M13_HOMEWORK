package org.example.task_3;

import com.google.gson.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileWriter;
import java.io.IOException;

public class JsonPlaceholderApiTodos {
    public static void getAllUncompletedTodos(int userId) throws IOException {
        if (userId < 1 || userId > 10) {
            System.out.println("Користувача з таким id не існує!");
            return;
        }

        String jsonStr = Jsoup.connect("https://jsonplaceholder.typicode.com/users/" + userId + "/todos?completed=false")
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.GET)
                .execute()
                .body();

        // Або можна робити запит за таким посиланням:
        // "https://jsonplaceholder.typicode.com/users/" + userId + "/todos?completed=false"
        // і тоді просто виводити jsonStr:
        // System.out.println(jsonStr);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonArray = gson.fromJson(jsonStr, JsonArray.class);

        JsonObject currentTodos;

        for (int i = 0; i < jsonArray.size(); i++) {
            currentTodos = jsonArray.get(i).getAsJsonObject();
            if ("false".equals(currentTodos.get("completed").getAsString())) {
                System.out.println(gson.toJson(currentTodos));
            }
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Всі відкриті задачі для користувача:\n");
        getAllUncompletedTodos(5);
    }
}
