import java.util.Arrays;
import java.util.List;
public class Application {
    //Endpoints
    private static final String WORKER_ADDRESS_1 = "http://localhost:81/capturarobjeto";
    public static void main(String[] args) {
        Aggregator aggregator = new Aggregator();
        String task = "1757600,IPN";
        Demo object = new Demo("hora1","hora2","hora3","hora4");
        System.out.println("Se le mandara al servidor:");
        object.imprimirDemo();
        byte[] serializado = SerializationUtils.serialize(object);
        //Envio de tareas a los trabajadores
        List<String> results = aggregator.sendTasksToWorkers(Arrays.asList(WORKER_ADDRESS_1),serializado);
        for (String result : results) {
            System.out.println(result);
        }
    }
}