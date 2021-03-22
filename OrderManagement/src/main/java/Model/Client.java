package Model;

/**
 * Clasa corespunzatoare tabelului Client din baza de date.
 */
public class Client {

    private int id;
    private String nume;
    private String address;
    private int deleted;

    /**
     * @param id id(primary key)
     * @param nume nume
     * @param address adresa
     * @param deleted deleted
     * Seteaza valorile variabilelor instanta.
     */
    public Client(int id, String nume, String address, int deleted) {
        this.id = id;
        this.nume = nume;
        this.address = address;
        this.deleted = deleted;
    }

    /**
     * Constructor fara parametrii.
     */
    public Client(){

    }

    /**
     * @return Returneaza id-ul clientului.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id noul id
     *           Seteaza id-ul clientului.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Returneaza numele impreuna cu caracterele ' '.
     * Getter utilizat la inserare.
     */
    public String getNume() {
        return "'" + nume + "'";
    }

    /**
     * @return Returneaza numele.
     */
    public String getSimpleNume() {
        return nume ;
    }

    /**
     * @return Returneaza adresa.
     */
    public String getSimpleAddress() {
        return address ;
    }

    /**
     * @param nume noul nume
     *             Seteaza numele
     */
    public void setNume(String nume) {
        this.nume = nume;
    }

    /**
     * @return Returneaza adresa impreuna alaturi de caracterele ' '.
     * Getter folosit la inserare.
     */
    public String getAddress() {
        return "'" + address + "'";
    }

    /**
     * @param address noua adresa
     *                Seteaza adresa.
     */
    public void setAddress(String address) {
        this.address = address;
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
        return "Model.Client{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", address='" + address + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
