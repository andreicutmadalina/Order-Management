package Bll;
import DAO.OrderItemDao;
import Model.OrderItem;

import java.util.NoSuchElementException;

/**
 * Clasa ce se ocupa de business logic-ul de la nivelul tabelei OrderItem.
 */
public class OrderItemBll {

    private OrderItemDao orderItemDao;

    public  OrderItemBll()
    {
        orderItemDao =  new OrderItemDao();
    }

    /**
     * @param idOrder ID-ul comenzii
     * @param idProduct ID-ul produsului
     * @return Returneaza un orderItem cu ID-ul comenzii si al produsului primite ca parametru.
     * In caz de insucces, se arunca o exceptie.
     */
    public OrderItem findByOrderProduct(int idOrder, int idProduct) {
        OrderItem orderItem = orderItemDao.findProductOrder(idOrder,idProduct);
        if (orderItem == null) {
            throw new NoSuchElementException("The order item with idOrder =" + idOrder + " and idProduct = " + idProduct + " was not found!");
        }
        return orderItem;
    }
}
