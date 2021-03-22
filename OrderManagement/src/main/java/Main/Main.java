package Main;
import Bll.BLL;
import Connection.ConnectionFactory;
import java.util.logging.Logger;
import Presentation.Parse;


public class Main {

    /**
     * @param args argumentele din linia de comanda
     *             Insteaza un obiect pentru parsarea fisierului preluat din linia de comanda
     *             Porneste aplicatia
     */
    public static void main(String[] args) {
        String fisier = args[0];
        Parse parse = new Parse(fisier);
        BLL bll = new BLL(parse.getDate());
    }
}
