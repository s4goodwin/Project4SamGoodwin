package bsu.comp152.java;

import java.util.ArrayList;

public class TaxExemptCustomer extends Customer{

    public TaxExemptCustomer(String Name, int id){
        super(Name, id);
    }
    public TaxExemptCustomer(String name){
        super(name);
    }

    @Override
    public double payForOrder(ArrayList<MerchandiseItem> itemsInOrder){
        var total=0.0;
        for (var item:itemsInOrder){
            total+=item.getPrice();
        }
        return total;
    }

    @Override
    public void arrangeDelivery(){
        System.out.println("please contact "+getName()+" on the day of delivery");
    }
}
