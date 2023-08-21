package org.example.task_2;

import com.google.gson.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileWriter;
import java.io.IOException;

public class JsonPlaceholderApiPosts {

    public static JsonObject findLastPost(int userId) throws IOException {

        String jsonStr = Jsoup.connect("https://jsonplaceholder.typicode.com/users/" + userId + "/posts")
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.GET)
                .execute()
                .body();

        // find last post
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement json = gson.fromJson(jsonStr, JsonElement.class);
        JsonArray jsonArray = json.getAsJsonArray();

        JsonObject lastPost = jsonArray.get(0).getAsJsonObject();
        JsonObject currentPost;

        for (int i = 1; i < jsonArray.size(); i++) {
            currentPost = jsonArray.get(i).getAsJsonObject();
            if (currentPost.get("id").getAsInt() > lastPost.get("id").getAsInt()) {
                lastPost = currentPost;
            }
        }

        return lastPost;
    }

    public static void getAllCommentsToLastPost(int userId) throws IOException {
        if (userId < 1 || userId > 10) {
            System.out.println("Користувача з таким id не існує!");
            return;
        }

        int lastPostId = findLastPost(userId).get("id").getAsInt();

        String jsonStr = Jsoup.connect("https://jsonplaceholder.typicode.com/posts/" + lastPostId + "/comments")
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.GET)
                .execute()
                .body();

        System.out.println(jsonStr);

        try (FileWriter writer = new FileWriter(String.format("src/main/java/org/example/task_2/user-%s-post-%s-comments.json", userId, lastPostId))) {
            writer.write(jsonStr);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Всі коментарі до останнього поста користувача:\n");
        getAllCommentsToLastPost(5);
    }
}
