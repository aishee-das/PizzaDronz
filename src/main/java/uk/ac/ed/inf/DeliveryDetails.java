package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;

// A class to represent delivery details
class DeliveryDetails {
    public String orderNo;
    public OrderStatus orderStatus;
    public OrderValidationCode orderValidationCode;


    // Constructor, getters, and setters

    // Constructor
    public DeliveryDetails(String orderNo, OrderStatus orderStatus, OrderValidationCode orderValidationCode) {
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
    }
}