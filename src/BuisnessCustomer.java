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
    public double payForOrder(ArrayList<MerchandiseItem> itemsInCart){
        var total=0.0;
        for (var item : itemsInCart){
            switch (item.getTaxibleType()){
                case WICFood -> {
                    total +=item.getPrice();
                }
                case GeneralMerchandise -> {
                    var price=item.getPrice();
                    var tax=price*0.0625;
                    total+=price+tax;
                }
                case Clothing -> {
                    var price=item.getPrice();
                    var tax=0.0;
                    if(price>175){
                        var taxiblePrice=price-175;
                        tax=taxiblePrice*0.0625;
                    }
                    total+=price+tax;
                }

            }
        }
        purchaseOrderBalance+=total;
        return 0.0;
    }
}