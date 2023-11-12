package uk.ac.ed.inf;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is responsible for retrieving data from REST server
 */
public class RetrieveRestData {
    private static final String BASE_URL_STRING = "https://ilp-rest.azurewebsites.net";
    private ObjectMapper mapper;

    public RetrieveRestData() {
        this.mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        this.mapper.registerModule(new JavaTimeModule());
    }

    public <T> T retrieveData(String urlExtension, Class<T> valueType) {
        try {
            URL baseUrl = new URL(BASE_URL_STRING + urlExtension);
            return this.mapper.readValue(baseUrl, valueType);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Restaurant[] retrieveRestaurantData() {
        String restaurantsEndpoint = "/restaurants";
        return retrieveData(restaurantsEndpoint, Restaurant[].class);
    }

    public Order[] retrieveOrderDataByDate(String dateString) {
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateString);
            String formattedDate = sdf.format(date);
            String ordersEndpoint = "/orders/" + formattedDate;
            return retrieveData(ordersEndpoint, Order[].class);
        } catch (ParseException | RuntimeException e) {
            // Log or handle the exception appropriately
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public NamedRegion[] retrieveNoFlyZones() {
        String nfzEndpoint = "/noFlyZones";
        return retrieveData(nfzEndpoint, NamedRegion[].class);
    }

    public NamedRegion retrieveCentralArea() {
        String centralAreaEndpoint = "/centralArea";
        return retrieveData(centralAreaEndpoint, NamedRegion.class);
    }
}

















//    private static final String BASE_URL_STRING = "https://ilp-rest.azurewebsites.net";
//    private static final String ORDER_URL = "/orders";

//    private Order[] orders; // Add this field
//
//
//        public void retrieveOrderData() {
//            ObjectMapper mapper = new ObjectMapper();
//
//            // Register the JavaTimeModule to handle Java 8 date/time types
//            mapper.registerModule(new JavaTimeModule());
//
//            try {
//                URL baseUrl = new URL(BASE_URL_STRING);
//                URL orderUrl = new URL(baseUrl, ORDER_URL);
//                this.orders = mapper.readValue(orderUrl, Order[].class);
//                System.out.println("Read all orders");
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            }
//        }
//
//    public Order[] getOrders() {
//        return orders;
//    }

