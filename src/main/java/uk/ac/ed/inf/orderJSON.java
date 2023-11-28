package uk.ac.ed.inf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.ed.inf.ilp.data.Order;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

class shortOrder{
    private final String orderNo;
    private final String orderStatus;
    private final String orderValidationCode;
    private final int costInPence;

    public shortOrder(Order order){
        this.orderNo = order.getOrderNo();
        this.orderStatus = order.getOrderStatus().toString();
        this.orderValidationCode = order.getOrderValidationCode().toString();
        this.costInPence = order.getPriceTotalInPence();
    }
}

public class orderJSON {
    public static void main(List<Order> orders,String date){
        String deliveriesFileName = "deliveries-"+date+".json";


        List<shortOrder> shortOrderList = new ArrayList<>();

        for (Order order: orders) {
            shortOrderList.add(new shortOrder(order));
        }

        try (Writer writer = new FileWriter("resultFiles/"+deliveriesFileName)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(shortOrderList, writer);
            System.out.println("Delivery file written");
        } catch (IOException e) {
            System.err.println("Unable to write deliveries");
        }

    }


}