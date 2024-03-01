package ressource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPRequestBuilder {
    public enum ResourceTypes{
        CUSTOMER,
        READING,
        USER
    }
    public enum HTTPRequestMethods{
        CREATE,
        GET,
        EDIT,
        DELETE
    }
    static HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> create(String url, ResourceTypes type, String dataInJson) throws IOException, InterruptedException {
        return sendRequest(url, type, HTTPRequestMethods.CREATE, "", dataInJson);
    }
    public static HttpResponse<String> get(String url, ResourceTypes type, String specificResource) throws IOException, InterruptedException {
        return sendRequest(url, type, HTTPRequestMethods.GET, specificResource, "");
    }
    public static HttpResponse<String> edit(String url, ResourceTypes type, String dataInJson) throws IOException, InterruptedException {
        return sendRequest(url, type, HTTPRequestMethods.EDIT, "", dataInJson);
    }
    public static HttpResponse<String> delete(String url, ResourceTypes type, String specificResource) throws IOException, InterruptedException {
        return sendRequest(url, type, HTTPRequestMethods.DELETE, specificResource, "");
    }

    private static HttpResponse<String> sendRequest(String url, ResourceTypes type, HTTPRequestMethods httpMethod, String specificResource, String dataInJson) throws IOException, InterruptedException {

        String resourceTypeStr;
        String resourceURL;

        resourceTypeStr = switch (type) {
            case CUSTOMER -> "/customer";
            case USER -> "/user";
            case READING -> "/reading";
        };

        resourceURL = switch (httpMethod) {
            case CREATE -> url + resourceTypeStr + "/create/";
            case GET -> url + resourceTypeStr + "/get/" + specificResource;
            case EDIT -> url + resourceTypeStr + "/edit/" + specificResource;
            case DELETE -> url + resourceTypeStr + "/delete/" + specificResource;
        };

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(resourceURL));

        HttpRequest.Builder request = switch (httpMethod) {
            case CREATE -> builder
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(dataInJson));
            case GET -> builder
                    .GET();
            case DELETE -> builder
                    .DELETE();
            case EDIT -> builder
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(dataInJson));
        };
        HttpResponse<String> response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());
        return response;
    }
}
