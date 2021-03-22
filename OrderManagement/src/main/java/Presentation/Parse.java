package Presentation;
import Bll.ClientBll;
import Bll.FinalOrderBll;
import Bll.ProductBll;
import Model.Client;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clasa raspunzatoare de parsarea fisierului.
 */
public class Parse {

    private String inputFile;
    private ArrayList<String[]> date;

    /**
     * @param inputFile fisierul de intrare ce contine comenzile.
     *                  Seteaza valorile variabilelor instanta.
     */
    public Parse(String inputFile)
    {
        this.inputFile = inputFile;
        date = new ArrayList<String[]>();
        read();
    }

    /**
     * Citeste din fisier comenzi si separa datele, adaugandu-le in array-ul date.
     */
   public void read()
    {
        File file = new File(inputFile);
        Scanner sc;
        String [] parts = new String[4];
        String [] aux = null;
        String s = null;
        try {
            sc = new Scanner(file);
            while(sc.hasNextLine()) {
                s = sc.nextLine();
                parts = s.split(":");//separam in comanda si valori
                String [] result = new String[4];
                result[0] = parts[0].toLowerCase();
                if (parts.length > 1) {
                    aux = parts[1].split(",");
                    for (int i = 1; i <= aux.length; i++) {
                        result[i] = aux[i - 1];
                    }
                }
                date.add(result);
            }
            sc.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return Returneaza array-ul cu datele din fisier..
     */
    public ArrayList<String[]> getDate() {
        return date;
    }
}
