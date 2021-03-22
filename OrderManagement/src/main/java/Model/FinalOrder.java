package Model;

/**
 * Clasa corespunzatoare tabelului FinalOrder din baza de date.
 */
public class FinalOrder {
    private int id;
    private int idClient;
    private float totalPrice;
    private int deleted;

    /**
     * @param id id
     * @param idClient idClient
     * @param totalPrice pret total
     * @param deleted deleted
     * Seteaza valorile variabilelor instanta.
     */
    public FinalOrder(int id, int idClient, float totalPrice, int deleted) {
        this.id = id;
        this.idClient = idClient;
        this.totalPrice = totalPrice;
        this.deleted = deleted;
    }

    /**
     * Constructor fara parametrii.
     */
    public FinalOrder()
    {

    }

    /**
     * @return Returneaza id-ul comenzii.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id noul id
     *           Seteaza id-ul comenzii.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returneaza id-ul clientului.
     */
    public int getIdClient() {
        return idClient;
    }

    /**
     * @param idClient noul idClient
     *                 Seteaza id-ul clientului.
     */
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    /**
     * @return Returneaza pretul total al comenzii.
     */
    public float getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice noul totalPrice
     *                   Seteaza pretul total al comenzii.
     */
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

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
        return "Model.FinalOrder{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", totalPrice=" + totalPrice +
                ", deleted=" + deleted +
                '}';
    }
}
