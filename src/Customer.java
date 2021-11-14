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

    public abstract double payForOrder(ArrayList<MerchandiseItem> itemsInCart);

    public double payOutstandingBalance(){
        return 0.0;
    }

    public void arrangeDelivery(){
        System.out.println("Delivery for "+Name+" can be delivered anytime");
    }

    public String getName() {
        return Name;
    }

}



