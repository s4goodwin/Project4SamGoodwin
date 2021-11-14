import java.util.ArrayList;

public class BuisnessCustomer extends Customer{
    private double purchaseOrderBalance;

    public BuisnessCustomer(String Name, int ID){
        super(Name, ID);
        purchaseOrderBalance=0;
    }

    public BuisnessCustomer(String name){
        super(name);
        purchaseOrderBalance=0;
    }

    @Override
    public double payForOrder(ArrayList<merchandiseItem> )
}
