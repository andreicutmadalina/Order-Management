package DAO;
import Model.OrderItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import Connection.ConnectionFactory;

/**
 * Clasa ce se ocupa de lucrul cu baza de date pe tabela OrderItem.
 * Extinde AbstractDAO.
 */
public class OrderItemDao extends AbstractDAO<OrderItem>{

    /**
     * @return Returneaza query-ul specific gasirii tuturor inregistrarilor din OrderItem  care au idOrder si idProduct de o anumita valoare.
     */
    private String selectQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * ");
        sb.append(" FROM ");
        sb.append(" OrderItem ");
        sb.append(" WHERE deleted = 0 and idOrder = ? and idProduct = ? ;");
        return sb.toString();
    }

    /**
     * @param idOrder idOrder
     * @param idProduct idProduct
     * @return Returneaza orderItem-l care are produsul cu id-l idProduct si comanda idOrder.
     */
    public OrderItem findProductOrder(int idOrder, int idProduct) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = selectQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrder);
            statement.setInt(2, idProduct);
            resultSet = statement.executeQuery();
            if(!resultSet.isBeforeFirst())
                return null;
            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Find product from an order" + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * @return Returneaza query-ul specific actualizarii unui orderItem corespunzator unui produs si unei comenzi.
     */
    public String updateQuery(){
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append(" OrderItem ");
        query.append(" SET ");
        query.append(" quantity = ?");
        query.append(" WHERE idOrder = ? and idProduct = ?");
        return String.valueOf(query);
    }

    /**
     * @param quantity cantitate
     * @param idProduct id produs
     * @param idOrder id comanda
     *                Realizeaza un update unui orderItem cu ce are produsul si comanda cu id-urile transmise ca parametri.
     */
    public void update(float quantity, int idProduct, int idOrder)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = updateQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setFloat(1, quantity);
            statement.setInt(2, idOrder);
            statement.setInt(3, idProduct);
            int done = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "OrderItem update" + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}
