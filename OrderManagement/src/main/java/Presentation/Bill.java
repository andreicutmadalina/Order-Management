package Presentation;
import DAO.ClientDao;
import DAO.FinalOrderDao;
import DAO.OrderItemDao;
import DAO.ProductDao;
import Model.Client;
import Model.FinalOrder;
import Model.OrderItem;
import Model.Product;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Clasa ce se ocupa de generarea facturilor, daca este posibil.
 */
public class Bill {

    private String nume ;
    private FinalOrderDao order;
    private OrderItemDao orderItem;
    private ClientDao clientDao;
    private ProductDao productDao;
    private int comandaEfectuata;
    private int idOrder;

    /**
     * @param nume nume pdf
     * @param done comanda realizata sau nu
     * @param idOrder idOrder
     *                Seteaza variabilele instanta
     */
    public Bill(String nume, int done, int idOrder)
    {
        this.nume = nume;
        this.idOrder = idOrder;
        comandaEfectuata = done;
        order =  new FinalOrderDao();
        orderItem = new OrderItemDao();
        clientDao = new ClientDao();
        productDao = new ProductDao();
        generatePdf();
    }

    /**
     * Genereaza un docment pdf cu numele din variabila nume.
     * Creeaza un tabel cu 3 coloane.
     * Daca s-a efectuat comanda, se apeleaza comandaEfectuataTable, altfel comandaNeefectuata
     */
    public void generatePdf()
    {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(nume));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();

        PdfPTable table = new PdfPTable(3);
        if(comandaEfectuata == 1) { //daca comanda s-a putut efectua
            addTableHeader(table);
            comandaEfectuataTable(table, document);
        }
        else
            comandaNeefectuata(document);

        document.close();
    }

    /**
     * @param table tabel
     * @param document document pdf
     *                 Completeqza pdf-ul cu datele introduse in tabel, dar si informatii cu privire la comanda, client si pretul total.
     */
    private void comandaEfectuataTable(PdfPTable table, Document document) {
        FinalOrder comanda = order.findById(idOrder, "id").get(0); //cauta comanda plasata
        Client client = clientDao.findById(comanda.getIdClient(), "id").get(0); //cauta clientul

        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph p1 = new Paragraph("Bill\n");
        Paragraph p2 = new Paragraph("Order" + idOrder + "\n");
        Paragraph p3 = new Paragraph("The bill for " + client.getSimpleNume() + ":\n\n");
        Paragraph p4 = new Paragraph("Total: " + comanda.getTotalPrice() + " lei");

        p1.setAlignment(Element.ALIGN_CENTER);
        p2.setAlignment(Element.ALIGN_CENTER);

        ArrayList<OrderItem> orderItems = (ArrayList<OrderItem>) orderItem.findById(comanda.getId(), "idOrder"); //cauta lista produselor comandate
        for(OrderItem item : orderItems) {
            Product p = productDao.findById(item.getIdProduct(), "id").get(0); //cauta produsul curent
            table.addCell(p.getNume1());
            table.addCell("" + item.getQuantity() + " kg");
            table.addCell("" + p.getPrice() + " lei/kg");
        }

        try {
            document.add(p1);
            document.add(p2);
            document.add(p3);
            document.add(table);
            document.add(p4);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param document document pdf
     *                 Afiseaza in pdf un mesaj sugestiv.
     */
    private void comandaNeefectuata(Document document)
    {
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph p1 = new Paragraph("Order" + idOrder + "\n\n\n\n\n");
        Paragraph p2 = new Paragraph("The products are out of the stock.");

        p1.setAlignment(Element.ALIGN_CENTER);
        p2.setAlignment(Element.ALIGN_CENTER);

        try {
            document.add(p1);
            document.add(p2);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param table tabelul
     *              Creeaza header-ul tabelului.
     */
    private void addTableHeader(PdfPTable table) {
        Stream.of("Product", "Quantity", "Price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

}
