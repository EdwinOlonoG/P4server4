import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.time.Duration;
public class WebClient {
    //Objeto de tipo HttpClient
    private HttpClient client;
    //Constructor crea un objeto HttpClient
    public WebClient() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }
    
    //Recibe la direccion y los datos a enviar al servidor
    public CompletableFuture<String> sendTask(String url, byte[] requestPayload) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestPayload))
                .uri(URI.create(url))
                .timeout(Duration.ofMinutes(5))
                .header("Content-Type","application")
                .header("x-Debug","true")
                .build();
        //Envio de la solicitud asincrona
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(respuesta->{
                    return respuesta.body();
                });
    }
}