import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try(var orderDispatcher = new kafkaDispatcher<Order>()) {
            try(var emailDispatcher = new kafkaDispatcher<Email>()) {
                for (int i = 0; i < 10; i++) {
                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() + 5000 * 1);
                    var email = Math.random() + "@gmail.com";
                    var order = new Order(userId, orderId, amount, email);
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);
                    var emailCode = new Email("New email", "Welcome! We are processing your order!");
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, emailCode);
                }
            }
        }
    }
}
