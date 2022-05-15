import java.math.BigDecimal;

public class Order {
    private String userId;
    private String orderId;
    private BigDecimal amount;
    private String email;

    public Order(String userId, String orderId, BigDecimal amount, String email) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }
}
