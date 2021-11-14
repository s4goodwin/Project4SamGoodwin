public class MerchandiseItem {
    private ItemType taxibleType;
    private String Name;
    private double price;

    public MerchandiseItem(String itemName, double cost, ItemType type){
        Name=itemName;
        price=cost;
        taxibleType=type;
    }

    public ItemType getTaxibleType() {
        return taxibleType;
    }
    public double getPrice(){
        return price;
    }
    public String getName(){
        return Name;
    }
}
