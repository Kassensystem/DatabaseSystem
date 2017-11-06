import com.sa.exceptions.ControllerConnectionException;
import com.sa.exceptions.MySQLException;
import com.sa.exceptions.NoContentException;
import com.sa.restApi.client.RestApiClient;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;


public class RestApiClient_Test {

    public static void main(String[] args) {


        try {
            System.out.println(RestApiClient.test());
        } catch (ControllerConnectionException e) {
            e.printStackTrace();
        } catch (MySQLException e) {
            e.printStackTrace();
        } catch (NoContentException e) {
            e.printStackTrace();
        }

         /*catch(HttpServerErrorException e) {
            System.out.println("--------\nSQL-Service ist nicht erreichbar!\n-----------");
        } catch(Exception e) { //TODO
            System.out.println("--------\nTabelle hat keinen Inhalt!\n-----------");
        }*/

        /*
        try {
            for(Item i: getAllItems()) {
                System.out.println(i.getItemID() + " \t  " + i.getName() + " \t " + i.getRetailprice() + " \t " + i.getQuantity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        try {
            for(Table t: getAllTables()) {
                System.out.println(t.getTableID() + " \t " + t.getName());
            }
            System.out.println();

            Order newOrder = new Order(getAllItems(), getAllTables().get(2), 7.89);
            createOrder(newOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}
