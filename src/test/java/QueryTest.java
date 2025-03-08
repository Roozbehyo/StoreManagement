import com.storemgmt.Model.ConnectionProvider;
import com.storemgmt.Model.Repo.OrderRepository;

import java.sql.CallableStatement;
import java.sql.Connection;

public class QueryTest {
    public static void main(String[] args) throws Exception {
        try (OrderRepository or = new OrderRepository()){
            or.findAll();
        }
    }
}
