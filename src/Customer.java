import java.util.ArrayList;

public abstract class Customer {

    private ArrayList<ShippingAddress> Addresses;
    private String Name;
    private int customerID;
    private static int nextID=5000;

    public Customer (String Name, int ID){
        this.Name=Name;
        customerID=ID;
        Addresses=new ArrayList<ShippingAddress>();
    }

    public abstract double payForOrder(ArrayList<MerchandiseItem> itemsInOrder);
}
