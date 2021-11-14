public class ShippingAddress {

    private String AddressLine1;
    private String AddressLine2;
    private String City;
    private String State;
    private String PostalCode;




    public ShippingAddress (String Line1, String Line2, String city, String State, String postCode) {
        AddressLine1 = Line1;
        AddressLine2 = Line2;
        City = city;
        this.State = State;
        PostalCode = postCode;
    }
}
