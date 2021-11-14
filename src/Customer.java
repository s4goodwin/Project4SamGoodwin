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
    public Customer(String custName){
        Name=custName;
        nextID++;
        customerID=nextID;
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
    public ArrayList<ShippingAddress> getAddresses(){
        return new ArrayList<ShippingAddress>(Addresses);
    }
    public int getCustomerID(){
        return customerID;
    }

    public void addAddress(ShippingAddress newAddress){
        Addresses.add(newAddress);
    }

    @Override
    public String toString(){
        return "Customer Name: " + Name +"\nCustomerID: "+customerID + "\nWith "+Addresses.size() + " addresses on file";
    }

}



