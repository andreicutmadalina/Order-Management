package Bll;
import DAO.OrderItemDao;
import DAO.ProductDao;
import Model.OrderItem;
import Model.Product;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Clasa ce se ocupa de business logic-ul de la nivelul tabelei Product.
 */
public class ProductBll {

    private ProductDao productDao;
    private OrderItemDao orderItemDao;

    /**
     * Instanteaza variabilele.
     */
    public  ProductBll()
    {
        productDao = new ProductDao();
        orderItemDao = new OrderItemDao();
    }

    /**
     * @param id ID-ul produsului
     * @return Returneaza produsul ce are ID-ul trimis ca parametru.
     * In caz de insucces se arunca o exceptie.
     */
    public Product findProductById(int id) {
        Product product = productDao.findById(id, "id").get(0);
        if (product == null) {
            throw new NoSuchElementException("The product with id =" + id + " was not found!");
        }
        return product;
    }

    /**
     * @param name numele produsului
     * @return Returneaza produsul ce are numele trimis ca parametru.
     * In caz de insucces se arunca o exceptie.
     */
    public Product findProductByName(String name) {
        Product product  = productDao.findByName(name);
        if (product  == null) return null;
        return product ;
    }

    /**
     * @param id ID produs
     * @param productName nume produs
     * @param quantity cantitate
     * @param price pret
     * Primeste ca parametrii datele despre un produs.
     * Daca produsul nu exista, acesta se insereaza in tabela,
     *  altfel se incrementeaza stock-ul cu cantitatea produsului primit.
     */
    public void prelucreaza(int id, String productName, float quantity, float price)
    {
        Product gasit = productDao.findByName(productName);//cauta produsul in baza de date
        if(gasit == null)//daca produsul nu exista
        {
            Product product = new Product(id, productName, price, quantity, 0);
            productDao.insert(product);//il insereaza in BD
        }
        else//altfel actualizam BD
        {
            gasit.setStock(gasit.getStock() + quantity);
            productDao.update(gasit, gasit.getId(), "id");
        }
    }

    /**
     * @param name  numele produsului
     * Gaseste lista orderItems- urilor care au asociate produsul a carui denumire a fost transmisa.
     * Se foloseste aceasta tehnica, deoarece pot exista mai multe inregistrari,
     * cu acelasi tip de produs, dar repartizate diferitor clienti.
     * Trebuie sterse toate aparitiile produselor.
     */
    public void sterge(String name)
    {
        Product produs = findProductByName(name);

        ArrayList<OrderItem> orderItems = (ArrayList<OrderItem>) orderItemDao.findById(produs.getId(), "idProduct");
        if(orderItems != null)
            for(OrderItem orderItem : orderItems)//intai se sterg orderItems-urile asociate produsul
            {
                 orderItem.setDeleted(1);
                 orderItemDao.update(orderItem, orderItem.getIdProduct(), "id");
            }
        produs.setDeleted(1);
        productDao.update(produs, produs.getId(), "id");//iar in final produsul
    }
}
