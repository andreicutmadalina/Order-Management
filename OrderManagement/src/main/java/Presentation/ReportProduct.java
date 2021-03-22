package Presentation;
import DAO.ProductDao;
import Model.Product;
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
 * Clasa raspunzatoare de report-ul pentru tabela Product.
 */
public class ReportProduct {

    private String nume ;
    private ProductDao product;

    /**
     * @param nume nume pdf
     *             Seteaza valorile variabilelor instanta.
     *             Apeleaza report()
     */
    public  ReportProduct(String nume)
    {
        this.nume = nume;
        product =  new ProductDao();
        report();
    }

    /**
     * Creeaza un pdf.
     * Gaseste inregistrarile din tabelul Product.
     * Creeaza un tabel cu 4 coloane.
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

        PdfPTable table = new PdfPTable(4);
        ArrayList<Product> products = (ArrayList<Product>) product.findAll();
        addTableHeader(table);
        addRows(table, products);

        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }

    /**
     * @param table tabel
     * @param products inregistrarile despre produse
     *                Adauga linii cu date despre produse in tabel.
     */
    private void addRows(PdfPTable table, ArrayList<Product> products) {
        for(Product p : products)
        {
            table.addCell("" + p.getId());
            table.addCell(p.getNume1());
            table.addCell("" + p.getStock());
            table.addCell("" + p.getPrice());
        }
    }

    /**
     * @param table tabel
     *              Creeaza header-ul tabelului.
     */
    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "Name", "Stock", "Price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

}
