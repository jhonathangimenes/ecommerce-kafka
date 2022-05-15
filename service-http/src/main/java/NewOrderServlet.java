import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final kafkaDispatcher<Order> orderDispatcher = new kafkaDispatcher<>();
    private final kafkaDispatcher<Email> emailDispatcher = new kafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var email = req.getParameter("email");
            var amount = new BigDecimal(req.getParameter("amount"));
            var userId = UUID.randomUUID().toString();
            var orderId = UUID.randomUUID().toString();
            var order = new Order(userId, orderId, amount, email);
            orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);
            var emailCode = new Email("New email", "Welcome! We are processing your order!");
            emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, emailCode);
            System.out.println("New order sent successfully");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New order sent successfully");
        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }
}
