package Bll;
import Model.Client;
import Model.Product;
import Presentation.Bill;
import Presentation.ReportClient;
import Presentation.ReportOrder;
import Presentation.ReportProduct;
import java.util.ArrayList;

/**
 * Clasa ce se ocupa de logica generala a aplicatiei.
 */
public class BLL {
    private int idClient, idProduct, idOrder, lastBuyer;
    private int reportClientCount, reportProductCount, reportOrderCount;
    private ProductBll productBll;
    private ClientBll clientBll;
    private FinalOrderBll orderBll;
    /**
     * Contine datele extrase din fisierul de intrare.
     */
    private ArrayList<String[]> date;

    /**
     * @param date datele extrase din fisiser
     * Constructorul initializeaza ID-urile tabelelor, creeaza cate o instanta de obiect
     * corespunzatoare fiecarui TableBll.
     */
    public BLL(ArrayList<String[]> date)
    {
       this.date = date;

        idClient = idProduct = 1;
        idOrder = 0;
        reportClientCount = reportOrderCount = reportProductCount = 1;
        lastBuyer = -1;

        productBll = new ProductBll();
        clientBll = new ClientBll();
        orderBll = new FinalOrderBll();

        selecteazaComanda();
    }

    /**
     * Se parcurg campurile extrase din comanda preluata din fisier,
     * iar apoi se cauta comanda curenta pentru a realiza operatiile necesare.
     */
    public void selecteazaComanda()
    {
        for(String[] comanda : date) {
            if (comanda[0].compareTo("insert client") == 0)
                clientBll.insert(idClient++, comanda[1], comanda[2]);
            else if (comanda[0].compareTo("insert product") == 0)
            {
                Product p = productBll.findProductByName(comanda[1]);
                if(p!=null)//daca produsul exista deja
                    productBll.prelucreaza(p.getId(), comanda[1], Float.parseFloat(comanda[2]), Float.parseFloat(comanda[3]));
                else
                    productBll.prelucreaza(idProduct++, comanda[1], Float.parseFloat(comanda[2]), Float.parseFloat(comanda[3]));

            }
            else if (comanda[0].contains("delete client"))
                clientBll.sterge(comanda[1]);
            else if (comanda[0].contains("delete product"))
                productBll.sterge(comanda[1]);
            else if (comanda[0].compareTo("order")==0)
                order(comanda);
            else if (comanda[0].contains("report"))
                report(comanda[0]);
        }
    }


    /**
     * @param valori reprezinta datele extrase din comanda.
     * Intai, se gaseste clientul care a plasat comanda.
     * Apoi se incearca plasarea comanzii la id-ul corespunzator
     * Daca s-a efectuat comanda, se emite o factura, altfel se emite un pdf cu un mesaj sugestiv.
     */
    public void order(String valori[])
    {
        Client c = clientBll.findClientByName(valori[1]); //gaseste clientul care a plasat comanda
        if(c.getId() != lastBuyer)//daca NU este acelasi client ca cel anterior, se face o noua comanda
            idOrder++;
        int done = orderBll.plaseazaComanda(idOrder, valori[1], valori[2], Float.parseFloat(valori[3]));
        String nume = null;
        if(done == 1)
            nume = "pdf/Bill" + idOrder +".pdf";
        else
            nume = "pdf/UnderStock" + idOrder +".pdf";
        new Bill(nume +".pdf", done, idOrder);
        lastBuyer = c.getId();
    }

    /**
     * @param comanda reprezinta numele comenzii.
     * Verifica pentru care tabela trebuie facut report.
     */
    public void report(String comanda)
    {
        if(comanda.contains("product"))
            new ReportProduct("pdf/ReportProduct" + reportProductCount++ + ".pdf");
        else if(comanda.contains("client"))
            new ReportClient("pdf/ReportClient" + reportClientCount++ + ".pdf");
        else
            new ReportOrder("pdf/ReportOrder" + reportOrderCount++ + ".pdf");
    }
}
