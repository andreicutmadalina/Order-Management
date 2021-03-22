package Presentation;
import DAO.FinalOrderDao;
import DAO.OrderItemDao;
import Model.FinalOrder;
import Model.OrderItem;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Clasa raspunzatoare de report-ul pentru tabela FinalOrder.
 */
public class ReportOrder {

    private String nume ;
    private FinalOrderDao order;
    private OrderItemDao orderItem;

    /**
     * @param nume nume pdf
     *             Seteaza valorile variabilelor instanta.
     *             Apeleaza report()
     */
    public  ReportOrder(String nume)
    {
        this.nume = nume;
        order =  new FinalOrderDao();
        orderItem = new OrderItemDao();
        report();
    }

    /**
     * Creeaza un document pdf.
     * Creeaza un tabel cu 5 coloane.
     * Gaseste toate inregistrarile din tabela FinalOrder.
     */
    public void report()
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

        PdfPTable table = new PdfPTable(5);
        ArrayList<FinalOrder> orders = (ArrayList<FinalOrder>) order.findAll();
        addTableHeader(table);
        addRows(table, orders);

        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }

    /**
     * @param table  tabel
     * @param orders inregistraile din tabel FinalOrder
     *               Adauga linii tabelului cu date despre comanda si despre orderItem-urile specifice.
     */
    private void addRows(PdfPTable table, ArrayList<FinalOrder> orders) {
        for(FinalOrder order : orders) {
            ArrayList<OrderItem> orderItems = (ArrayList<OrderItem>) orderItem.findById(order.getId(), "idOrder");
            for(OrderItem item : orderItems) {
                table.addCell("" + order.getId());
                table.addCell("" + order.getIdClient());
                table.addCell("" + order.getTotalPrice());
                table.addCell("" + item.getIdProduct());
                table.addCell("" + item.getQuantity());
            }
        }
    }

    /**
     * @param table tabel
     *              Creeaza header-ul tabelului.
     */
    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "ID_client", "Total Price", "ID_product", "Quantity")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

}
