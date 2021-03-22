package Model;

/**
 * Clasa corespunzatoare tabelului Product din baza de date.
 */
public class Product {
    private int id;
    private String nume;
    private float price;
    private float stock;
    private int deleted;

    /**
     * @param id id
     * @param nume nume
     * @param price pret
     * @param stock stock
     * @param deleted daca e 0 elementul exista, altfel nu
     * Seteaza valorile variabilelor instanta.
     */
    public Product(int id, String nume, float price, float stock, int deleted) {
        this.id = id;
        this.nume = nume;
        this.price = price;
        this.stock = stock;
        this.deleted = deleted;
    }

    /**
     * Constructor fara parametrii.
     */
    public Product()
    {

    }

    /**
     * @return returneaza id-ul.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id noua valoare a id-ului
     *           Seteaza id-ul cu valoarea id
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return "'" + nume + "'";
    }

    /**
     * @return Returneaza numele produsului.
     */
    public String getNume1() {
        return  nume;
    }

    /**
     * @param nume nou nume al produsului
     *             Seteaza numele produsului.
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /**
     * @return Returneaza pretul.
     */
    public float getPrice() {
        return price;
    }

    /**
     * @param price noul pret
     *              Seteaza pretul.
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * @return Retunreaza valoarea stock-ului.
     */
    public float getStock() {
        return stock;
    }

    /**
     * @param stock noul stock
     *              Seteaza stock-ul.
     */
    public void setStock(float stock) {
        this.stock = stock;
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
        return "Model.Product{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", deleted=" + deleted +
                '}';
    }
}
