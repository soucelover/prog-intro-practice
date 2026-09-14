package intro.java.run_me;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class SimpleHttp {
    public static boolean attempt(final String flag) {
        final String url = "https://www.kgeorgiy.info/courses/prog-intro/hw1/" + flag;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Accept", "text/html")
            .GET()
            .build();
        HttpResponse<String> response;

        try {
             response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        if (response.body().contains("Поздравляем!")) {
            System.out.println("Поздравляем!");
        } else if (response.body().contains("Уже решено")) {
            System.out.println("Уже решено");
        } else if (response.body().contains("Неверный флаг")) {
            System.out.println("Неверный флаг");
        }

        return !response.body().contains("Поздравляем!");
    }
}