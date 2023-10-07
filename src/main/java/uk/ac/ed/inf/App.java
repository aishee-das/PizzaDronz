package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.time.LocalDate;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        CreditCardInformation creditCardInfo = new CreditCardInformation("1349946965043333", "06/28", "123");
        Order sampleOrder = new Order("19514FE0", LocalDate.now(), OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2400, new Pizza[0], creditCardInfo);
        OrderValidator orderValidator = new OrderValidator();
        Order validatedOrder = orderValidator.validateOrder(sampleOrder, new Restaurant[0]);
        OrderValidationCode validationCode = validatedOrder.getOrderValidationCode();
        System.out.println(validationCode);
    }
}