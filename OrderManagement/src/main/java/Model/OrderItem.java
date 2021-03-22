package Model;

/**
 * Clasa corespunzatoare tabelului OrderItem din baza de date.
 */
public class OrderItem {
    private int idOrder;
    private int idProduct;
    private float quantity;
    private int deleted;

    /**
     * @param idOrder idOrder
     * @param idProduct idProduct
     * @param quantity quantity
     * @param deleted deleted
     *                Seteaza valorile variabilelor instanta.
     */
    public OrderItem(int idOrder, int idProduct, float quantity, int deleted) {
        this.idOrder = idOrder;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.deleted = deleted;
    }

    /**
     * Constructor fara parametrii.
     */
    public OrderItem()
    {

    }

    /**
     * @return Returneaza id-ul comenzii.
     */
    public int getIdOrder() {
        return idOrder;
    }

    /**
     * @param idOrder noul idOrder
     *                Seteaza idOrder.
     */
    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    /**
     * @return Returneaza id-ul produsului.
     */
    public int getIdProduct() {
        return idProduct;
    }

    /**
     * @param idProduct noul idProduct
     *                  Seteaza idProduct.
     */
    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    /**
     * @return Returneaza cantitatea.
     */
    public float getQuantity() {
        return quantity;
    }

    /**
     * @param quantity noua cantitate
     *                 Seteaza cantitatea orderItem-ului.
     */
    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    /**
     * @return Returneaza campul deleted.
     */
    public int getDeleted() {
        return deleted;
    }

    /**
     * @param deleted noua valoarea a campului deleted
     *                Seteaza campul deleted.
     */
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    /**
     * @return Returneaza toString-ul.
     */
    @Override
    public String toString() {
        return "Model.OrderItem{" +
                "idOrder=" + idOrder +
                ", idProduct=" + idProduct +
                ", quantity=" + quantity +
                ", deleted=" + deleted +
                '}';
    }
}
