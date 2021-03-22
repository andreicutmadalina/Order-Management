package Presentation;
import DAO.ClientDao;
import Model.Client;
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
 * Clasa raspunzatoare de report-ul pentru tabela Client.
 */
public class ReportClient {

    private String nume ;
    private ClientDao client;

    /**
     * @param nume nume pdf
     *             Seteaza valorile variabilelor instanta.
     *             Apeleaza report()
     */
    public  ReportClient(String nume)
    {
        this.nume = nume;
        client =  new ClientDao();
        report();
    }

    /**
     * Creeaza un pdf.
     * Gaseste inregistrarile din tabel.
     * Creeaza un tabel cu 3 coloane.
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

        PdfPTable table = new PdfPTable(3);
        ArrayList<Client> clients = (ArrayList<Client>) client.findAll();
        addTableHeader(table);
        addRows(table, clients);

        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }


    /**
     * @param table tabel
     * @param clients inregistrarile despre clienti
     *                Adauga linii cu date despre clienti in tabel.
     */
    private void addRows(PdfPTable table, ArrayList<Client> clients) {
        for(Client c : clients)
        {
            table.addCell("" + c.getId());
            table.addCell(c.getSimpleNume());
            table.addCell(c.getSimpleAddress());
        }
    }

    /**
     * @param table tabel
     *              Creeaza header-ul tabelului.
     */
    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "Name", "Address")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

}
