package DAO;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Connection.ConnectionFactory;

/**
 * @param <T> tip generic
 *           Clasa ce se ocupa de operatiile pe baza de date pentru toate tabelele si care cuprinde metodele generice.
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Se gaseste tipul clasei.
     */
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * @param field numele unui camp
     * @return Returneaza ca String query-ul format.
     * Selecteaza toate datele dintr-un tabel, pentru care field are o anumita valoare.
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " = ? and deleted = 0;");
        return sb.toString();
    }

    /**
     * @return Returneaza ca String query-ul format.
     * Selecteaza toate datele dintr-un tabel.
     */
    private String selectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE deleted = 0;");
        return sb.toString();
    }

    /**
     * @return Returneaza toate inregistrarile unui tabel.
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = selectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if(!resultSet.isBeforeFirst())
                return null;
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll" + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * @param name nume
     * @return Returneaza inregistrarea dintr-un tabel care are numele trimis ca parametru.
     */
    public T findByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("nume");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            if(!resultSet.isBeforeFirst())
                return null;

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * @param id id
     * @param field field
     * @return Returneaza inregistrarile care au ID-ul id.
     */
    public List<T> findById(int id, String field) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery(field);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if(!resultSet.isBeforeFirst())
                return null;
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * @param resultSet rezultatul unui query
     * @return Returneaza o lista a rezultatelor query-ului corespunzatoare tabelului.
     */
    public List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param t obiect generic de tipul unei tabele
     * @return Returneaza query-ul specific inserarii obiectului t in tabela.
     */
    public String insertQuery(T t){
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(type.getSimpleName());
        query.append(" VALUES (");
        try {
                for (Field field : type.getDeclaredFields()) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getReadMethod();
                    query.append(method.invoke(t));
                    query.append(", ");
                }
                query.deleteCharAt(query.length()-2);
                query.append(");");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return String.valueOf(query);
    }

    /**
     * @param t obiect generic de tipul unei tabele
     *          insereaza obiectul t
     */
    public void insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = this.insertQuery(t);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            int done = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert" + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * @param t obiect generic
     * @param field1 camp
     * @return Returneaza query-ul specific actualizarii BD pentru inregistrarile a carui camp field1 are o anumita valoare.
     */
    public String updateQuery(T t, String field1){
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append(type.getSimpleName());
        query.append(" SET ");
        try {
            for (Field field : type.getDeclaredFields()) {
                String camp = field.getName();
                query.append(camp + " = ");
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                Method method = propertyDescriptor.getReadMethod();
                query.append(method.invoke(t));
                query.append(" , ");
            }
            query.deleteCharAt(query.length()-2);
            query.append(" WHERE " + field1 + " = ?;" );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return String.valueOf(query);
    }

    /**
     * @param t
     * @param id
     * @param field
     * Operatia de update va fi folosita si pentru operatia de delete.
     * Fiecare inregistrare are un camp deleted, care e 0 daca inregistarrea exista, respectiv 1 in caz contrar.
     * A sterge o inregistrare din BD inseamna a-i seta campul deleted = 1.
     */
    public void update(T t, int id, String field)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = updateQuery(t,field);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int done = statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}
