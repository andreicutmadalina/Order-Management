package Bll;
import DAO.ClientDao;
import DAO.FinalOrderDao;
import DAO.OrderItemDao;
import DAO.ProductDao;
import Model.Client;
import Model.FinalOrder;
import Model.OrderItem;
import Model.Product;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Clasa ce se ocupa de business logic-ul de la nivelul tabelei FinalOrder.
 */
public class FinalOrderBll {

    private FinalOrderDao finalOrderDao;
    private ProductDao productDao;
    private ProductBll productBll;
    private  ClientBll  clientBll;
    private ClientDao clientDao;
    private OrderItemDao orderItemDao;

    /**
     * Instanteaza variabilele.
     */
    public  FinalOrderBll()
    {
        finalOrderDao = new FinalOrderDao();
        productDao =  new ProductDao();
        clientDao = new ClientDao();
        orderItemDao =  new OrderItemDao();
        clientBll = new ClientBll();
        productBll= new ProductBll();
    }

    /**
     * @return Returneaza comezile gasite, iar in caz de insucces, arunca o exceptie cu un mesaj sugestiv.
     * Gaseste toate inregistrarile unui tabel.
     */
    public ArrayList<FinalOrder> findAll() {
        ArrayList<FinalOrder> finalOrders = (ArrayList<FinalOrder>) finalOrderDao.findAll();
        if (finalOrders == null) {
            throw new NoSuchElementException("Threre are no records");
        }
        return finalOrders;
    }

    /**
     * @param id ID-ul unei comenzi
     * @return Returneaza comanda cu ID-ul id, iar in caz de insucces arunca o exceptie.
     */
    public FinalOrder findOrderById(int id) {
        FinalOrder order = finalOrderDao.findById(id, "id").get(0);
        if (order == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return order;
    }

    /**
     * @param idNewOrder ID-ul noii comenzi
     * @param idClient ID-ul clientului
     * @param order comanda
     * @param product produsul
     * @param cantitate cantitatea
     * Cazul 1: Exista o comanda activa pentru client, deci se adauga produse la acea comanda.
     * Se actualizeaza pretul total si se actualizeaza comanda in BD.
     * Daca tipul produsului e pe factura, se actualizeaza cantitatea.
     * altfel se introduce acest nou produs.
     * Cazul2: Se creeaza o NOUA comanda.
     * Se introduce produsul.
     * la final, trebuie actualizat stock-ul produsului(se scade cantitatea comandata).
     */
    public void comanda(int idNewOrder, int idClient, FinalOrder order, Product product, float cantitate)
    {
        if(order != null ) {
                order.setTotalPrice(order.getTotalPrice() + product.getPrice()*cantitate);
                finalOrderDao.update(order, order.getId(), "id");
                OrderItem produsExistent = orderItemDao.findProductOrder(order.getId(), product.getId());

                if(produsExistent != null) { //daca produsul este deja pe factura
                    produsExistent.setQuantity(produsExistent.getQuantity() + cantitate);
                    orderItemDao.update(produsExistent.getQuantity(), produsExistent.getIdProduct(), produsExistent.getIdOrder());
                }
                else {
                    OrderItem orderItem = new OrderItem(order.getId(), product.getId(), cantitate, 0);
                    orderItemDao.insert(orderItem);
                }
        }
        else{
                order = new FinalOrder(idNewOrder, idClient,product.getPrice()*cantitate, 0);
                finalOrderDao.insert(order);
                OrderItem orderItem = new OrderItem(idNewOrder, product.getId(), cantitate, 0);
                orderItemDao.insert(orderItem);
        }
        product.setStock(product.getStock()-cantitate);
        productDao.update(product, product.getId(), "id");
    }

    /**
     * @param id ID-ul comenzii
     * @param numeClient numele clientului
     * @param numeProdus numele produsului
     * @param cantitate cantitatea produsului
     * Se incearca plasarea unei comenzi.
     * Daca stock-ul nu este suficient pt plasarea comenzii, atunci aceasta nu se va efectua.
     * Daca produsul nu este pe stock, nu se va plasa comanda.
     * Se va verifica existenta unei comenzi active pentru clientul dat.
     * Comanda se efectueaza in metoda apelata in urma verificarilor.
     * @return Se returneaza 0 daca nu s-a putut plasa comanda, respectiv 1 in caz contrar.
     */
    public int plaseazaComanda(int id, String numeClient, String numeProdus, float cantitate)
    {
        Product p =productBll.findProductByName(numeProdus);
        if(p.getStock() < cantitate)
            return 0;

        Client c = clientBll.findClientByName(numeClient);//gaseste clientul care a plasat comanda
        FinalOrder comanda = null;
        ArrayList<FinalOrder> comenziPlasate = (ArrayList<FinalOrder>) finalOrderDao.findById(id, "id");
        if(comenziPlasate != null)//exista o comanda activa
            comanda = comenziPlasate.get(0);
        this.comanda(id, c.getId(), comanda, p, cantitate);
        return 1;
    }
}
