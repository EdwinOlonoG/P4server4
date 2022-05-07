//PROYECTO 4
//OLOÑO GARCIA EDWIN
//4CM14
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.io.ByteArrayOutputStream;
public class WebServer {
    //endpoints
    private static final String STATUS_ENDPOINT = "/status";
    private static final String TIME_ENDPOINT = "/time";  
    private static String WORKER_ADDRESS_1;
    private static String WORKER_ADDRESS_2; 
    private static String WORKER_ADDRESS_3; 
    private static String WORKER_ADDRESS_4;
    private static String checkStatus1;
    private static String checkStatus2; 
    private static String checkStatus3; 
    private static String checkStatus4;  
    private final int port;
    private HttpServer server;
    public static void main(String[] args) {
        WORKER_ADDRESS_1 = args[1] + TIME_ENDPOINT;
    WORKER_ADDRESS_2 = args[2] + TIME_ENDPOINT; 
    WORKER_ADDRESS_3 = args[3] + TIME_ENDPOINT; 
    WORKER_ADDRESS_4 = args[4] + TIME_ENDPOINT;
    checkStatus1 = args[1] + STATUS_ENDPOINT;
    checkStatus2 = args[2] + STATUS_ENDPOINT; 
    checkStatus3 = args[3] + STATUS_ENDPOINT; 
    checkStatus4 = args[4] + STATUS_ENDPOINT;
    int serverPort = 80;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }
    //Instancia de WebServer
    WebServer WebServer = new WebServer(serverPort);
    WebServer.startServer();
    System.out.println("Servidor escuchando en el puerto " + serverPort);
    }
    //Constructor
    public WebServer(int port) {
        this.port = port;
    }
    public void startServer() {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    //Creando objetos HttpContext
        HttpContext statusContext = server.createContext(STATUS_ENDPOINT);
        HttpContext timeContext = server.createContext(TIME_ENDPOINT);
    //Asigna un método manejador a los endpoints
        statusContext.setHandler(this::handleStatusCheckRequest);
        timeContext.setHandler(this::handleTimeRequest);
    //Se proveen 8 hilos para que el servidor trabaje
        server.setExecutor(Executors.newFixedThreadPool(8));
    //Se inicia el servidor como hilo en segundo plano
        server.start();
    }
    //Se analiza si la petición es GET para devolver que el servidor está vivo
    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }
        String responseMessage = "El servidor esta vivo";
        sendResponse(responseMessage.getBytes(), exchange);
    }
    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
    //Agrega estatus code 200 de éxito y longitud de la respuesta
        exchange.sendResponseHeaders(200, responseBytes.length);
    //Se escribe en el cuerpo del mensaje
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
        exchange.close();
    }
    private void handleTimeRequest(HttpExchange exchange) throws IOException {
    //Se verifica si se solicitó un método POST
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }
        Headers headers = exchange.getRequestHeaders();
    boolean isDebugMode = false;
        if (headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true")) {
            isDebugMode = true;
        }
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        byte [] responseBytes = String.format("Ok").getBytes();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Aggregator aggregator = new Aggregator();
        Demo objeto = null;
        objeto = (Demo)SerializationUtils.deserialize(requestBytes);
        imprimirObjetoSerializado(objeto);
        imprimeTiempoPromedio(objeto);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        objeto.servidor4 = dtf.format(LocalDateTime.now());
        byte[] serializado = SerializationUtils.serialize(objeto);
        //responseBytes = "Ok".getBytes();
    //Si se activó el modo Debug se imprime el tiempo
        if (isDebugMode) {
            String debugMessage = String.format("");          
            exchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage));            
            byte[] timeBytes = String.format("").getBytes();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(timeBytes);
            responseBytes = outputStream.toByteArray();
            sendResponse(responseBytes, exchange);
        }
        String serverActive = checkStatus(serializado);
        aggregator.sendTasksToWorkers(Arrays.asList(serverActive),serializado);
    }
    
    private void imprimirObjetoSerializado(Demo objeto) {
        System.out.println("Se recibio el objeto con los elementos: ");
        System.out.println(WORKER_ADDRESS_1 + " " + objeto.servidor1);
        System.out.println(WORKER_ADDRESS_2 + " " + objeto.servidor2);
        System.out.println(WORKER_ADDRESS_3 + " " + objeto.servidor3);
        System.out.println(WORKER_ADDRESS_4 + " " + objeto.servidor4);
    }
    private void imprimeTiempoPromedio(Demo objeto){
        int cantidadServidores = 0;
        int horaServidor1 = 0;
        int horaServidor2 = 0;
        int horaServidor3 = 0;
        int horaServidor4 = 0;
        int minutosServidor1 = 0;
        int minutosServidor2 = 0;
        int minutosServidor3 = 0;
        int minutosServidor4 = 0;
        int segundosServidor1 = 0;
        int segundosServidor2 = 0;
        int segundosServidor3 = 0;
        int segundosServidor4 = 0;
        if(!objeto.servidor1.equals("-")){
            horaServidor1 = Integer.parseInt(objeto.servidor1.split(":")[0]);
            minutosServidor1 = Integer.parseInt(objeto.servidor1.split(":")[1]);
            segundosServidor1 = Integer.parseInt(objeto.servidor1.split(":")[2]);
            cantidadServidores++;
        }
        if(!objeto.servidor2.equals("-")){
            System.out.print("Entre al if");
            horaServidor2 = Integer.parseInt(objeto.servidor2.split(":")[0]);
            minutosServidor2 = Integer.parseInt(objeto.servidor2.split(":")[1]);
            segundosServidor2 = Integer.parseInt(objeto.servidor2.split(":")[2]);
            cantidadServidores++;
        }
        if(!objeto.servidor3.equals("-")){
            horaServidor3 = Integer.parseInt(objeto.servidor3.split(":")[0]);
            minutosServidor3 = Integer.parseInt(objeto.servidor3.split(":")[1]);
            segundosServidor3 = Integer.parseInt(objeto.servidor3.split(":")[2]);
            cantidadServidores++;
        }
        if(!objeto.servidor4.equals("-")){
            horaServidor4 = Integer.parseInt(objeto.servidor4.split(":")[0]);
            minutosServidor4 = Integer.parseInt(objeto.servidor4.split(":")[1]);
            segundosServidor4 = Integer.parseInt(objeto.servidor4.split(":")[2]);
            cantidadServidores++;
        }

        int horaPromedio = (horaServidor1 + horaServidor2 + horaServidor3 + horaServidor4)/cantidadServidores;
        int minutoPromedio = (minutosServidor1 + minutosServidor2 + minutosServidor3 + minutosServidor4)/cantidadServidores;
        int segundoPromedio = (segundosServidor1 + segundosServidor2 + segundosServidor3 + segundosServidor4)/cantidadServidores;
        System.out.println("tiempo promedio: " + horaPromedio + ":" + minutoPromedio + ":" + segundoPromedio);
    }
    private String checkStatus(byte[] serializado){
        Aggregator aggregator = new Aggregator();
        try {
            aggregator.sendTasksToWorkers(Arrays.asList(checkStatus1), serializado);
            System.out.println("Enviando a server 1:");
            return WORKER_ADDRESS_1;
        } catch (Exception e1) {
            try {
                System.out.println(e1);
                aggregator.sendTasksToWorkers(Arrays.asList(checkStatus2), serializado);
                System.out.println("Enviando a server 2:");
                return WORKER_ADDRESS_2;
            } catch (Exception e2) {
                try {
                    System.out.println(e2);
                    aggregator.sendTasksToWorkers(Arrays.asList(checkStatus3), serializado);
                    System.out.println("Enviando a server 3:");
                    return WORKER_ADDRESS_3;
                } catch (Exception e3) {
                    System.out.println(e3);
                }
            }
        }
        System.out.println("Enviando a server 4:");
        return WORKER_ADDRESS_4;
    }
}