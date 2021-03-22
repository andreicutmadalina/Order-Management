package Bll;
import DAO.ClientDao;
import DAO.FinalOrderDao;
import Model.Client;
import Model.FinalOrder;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Clasa ce se ocupa de business logic-ul de la nivelul tabelei Client.
 */
public class ClientBll {
    private ClientDao clientDao;
    private FinalOrderDao finalOrderDao;

    /**
     * Instanteaza un obiect de tipul ClientDao si unul de tipul FinalOrderDao.
     */
    public ClientBll()
    {
        clientDao = new ClientDao();
        finalOrderDao = new FinalOrderDao();
    }

    /**
     * @param id reprezinta ID-ul clientului.
     * @return In caz de succes, se returneaza clientul,
     * altfel se arunca o exceptie cu un mesaj sugestiv.
     */
    public Client findClientById(int id) {
        Client client = clientDao.findById(id, "id").get(0);
        if (client == null) {
            throw new NoSuchElementException("The client with id =" + id + " was not found!");
        }
        return client;
    }

    /**
     * @param name reprezinta numele clientului.
     * @return In caz de succes, se returneaza clientul,
     * altfel se arunca o exceptie cu un mesaj sugestiv.
     */
    public Client findClientByName(String name) {
        Client client = clientDao.findByName(name);
        if (client == null) {
            throw new NoSuchElementException("The client with name =" + name + " was not found!");
        }
        return client;
    }

    /**
     * @param id ID-ul clientului.
     * @param name numele clientului.
     * @param address adresa clientului.
     */
    public void insert(int id, String name, String address)
    {
        Client client = new Client(id, name, address, 0);
        clientDao.insert(client);
    }

    /**
     * @param name nuemel clientului
     * Se gaseste comanda asociata unui client.
     * Daca exista, aceasta va fi stersa.
     * La final se sterge clientul.
     */
    public void sterge(String name)
    {
        Client client = findClientByName(name);
        ArrayList<FinalOrder> finalOrders = (ArrayList<FinalOrder>)finalOrderDao.findById(client.getId(), "idClient");//cauta comenzile pt client

        if(finalOrders != null)//daca clientul  are asociata cel putin o comanda, stergem intai comanda4
            for(FinalOrder order : finalOrders) {
                order.setDeleted(1);
                finalOrderDao.update(order, order.getId(), "id");
            }
        //apoi stergem clientul
        client.setDeleted(1);
        clientDao.update(client, client.getId(), "id");
    }

}
