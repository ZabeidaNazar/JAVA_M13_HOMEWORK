package org.example.task_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class JsonPlaceholderApiUsers {
    public static void addUser(String pathToJsonUserData) throws IOException {
        StringBuilder newUser = new StringBuilder();
        try (FileReader fileReader = new FileReader(pathToJsonUserData)) {
            char[] buf = new char[1024];
            int c;
            while ((c = fileReader.read(buf)) > 0) {
                if (c < 1024) {
                    buf = Arrays.copyOf(buf, c);
                }
                newUser.append(buf);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Файлу з даними про нового користувача не знайдено за переданим шляхом!");
            return;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        String jsonStr = Jsoup.connect("https://jsonplaceholder.typicode.com/users")
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .followRedirects(false)
                .requestBody(newUser.toString())
                .post()
                .body()
                .text();

        // print
        // System.out.println(jsonStr);

        // pretty print
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement json = gson.fromJson(jsonStr, JsonElement.class);
        System.out.println(gson.toJson(json));
    }

    public static void changeUser(String pathToJsonUserData, int userToUpdate) throws IOException {
        if (userToUpdate < 1 || userToUpdate > 10) {
            System.out.println("Користувача з таким id не існує!");
            return;
        }

        StringBuilder updateUser = new StringBuilder();
        try (FileReader fileReader = new FileReader(pathToJsonUserData)) {
            char[] buf = new char[1024];
            int c;
            while ((c = fileReader.read(buf)) > 0) {
                if (c < 1024) {
                    buf = Arrays.copyOf(buf, c);
                }
                updateUser.append(buf);
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Файлу з даними для оновлення користувача не знайдено за переданим шляхом!");
            return;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        String jsonStr = Jsoup.connect("https://jsonplaceholder.typicode.com/users/" + userToUpdate)
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .followRedirects(false)
                .requestBody(updateUser.toString())
                .method(Connection.Method.PUT)
                .execute()
                .body();

        // print
        // System.out.println(jsonStr);

        // pretty print
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement json = gson.fromJson(jsonStr, JsonElement.class);
        System.out.println(gson.toJson(json));
    }

    public static void deleteUser(int userToDelete) throws IOException {
        if (userToDelete < 1 || userToDelete > 10) {
            System.out.println("Користувача з таким id не існує!");
            return;
        }


        Connection.Response execute = Jsoup.connect("https://jsonplaceholder.typicode.com/users/" + userToDelete)
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.DELETE)
                .execute();

        System.out.println("statusCode = " + execute.statusCode());
        System.out.println("statusMessage = " + execute.statusMessage());
    }

    public static void getAllUsers() throws IOException {

        String jsonStr = Jsoup.connect("https://jsonplaceholder.typicode.com/users")
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.GET)
                .execute()
                .body();

        // print
        System.out.println(jsonStr);
    }

    public static void getUserById(int userToGet) throws IOException {
        if (userToGet < 1 || userToGet > 10) {
            System.out.println("Користувача з таким id не існує!");
            return;
        }


        String jsonStr = Jsoup.connect("https://jsonplaceholder.typicode.com/users/" + userToGet)
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.GET)
                .execute()
                .body();

        // print
        System.out.println(jsonStr);
    }

    public static void getUserByUserName(String username) throws IOException {

        String jsonStr = Jsoup.connect("https://jsonplaceholder.typicode.com/users?username=" + username)
                .ignoreContentType(true)
                .followRedirects(false)
                .method(Connection.Method.GET)
                .execute()
                .body();

        // print
        System.out.println(jsonStr);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("\n\nстворення нового об'єкта:\n");
        addUser("src/main/java/org/example/task_1/new_user.json");

        System.out.println("\n\nоновлення об'єкту:\n");
        changeUser("src/main/java/org/example/task_1/update_user.json", 1);

        System.out.println("\n\nвидалення об'єкта:\n");
        deleteUser(1);

        System.out.println("\n\nотримання інформації про всіх користувачів:\n");
        getAllUsers();

        System.out.println("\n\nотримання інформації про користувача за id:\n");
        getUserById(8);

        System.out.println("\n\nотримання інформації про користувача за username:\n");
        getUserByUserName("Delphine");
    }
}
