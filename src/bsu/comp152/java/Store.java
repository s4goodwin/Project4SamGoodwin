package bsu.comp152.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Store {

    private ArrayList<Order> Orders;
    private ArrayList<Customer> Customers;
    private ArrayList<MerchandiseItem> Stock;
    private double revenue;

    public Store(){
        Orders=new ArrayList<Order>();
        Customers=new ArrayList<Customer>();
        Stock=new ArrayList<MerchandiseItem>();
        revenue=0.0;
    }

    public static void main(String[] args) throws IOException {
        var comp152Inc = new Store();
        comp152Inc.runStore();
    }

    public void runStore() throws IOException{
        var inputReader=new Scanner(System.in);
        loadStartingCustomers(inputReader);
        loadStockItems();
        while (true){
            printMainMenu();
            var userChoice= inputReader.nextInt();
            switch (userChoice){
                case 1:
                    addCustomer(inputReader);
                    break;
                case 2:
                    var selectedCustomer=selectCustomer(inputReader);
                    if(selectedCustomer.isPresent())
                        manageCustomer(selectedCustomer.get());
                    break;
                case 3:
                    System.exit(0);
                case 4:
                    CollectOutstandingBalancesFromPurchaseOrders();
                    break;
                case 5:
                    System.out.println("our company has collected $"+revenue+" in revenue");
                    break;
                default:
                    System.out.println("\nInvalid selection");
            }
        }
    }

    private void loadStockItems(){
        List<String> allLines=null;

        try{
            Path itemsFilePath= Paths.get("ItemsForSale.txt");
            allLines= Files.readAllLines(itemsFilePath);
        }
        catch (IOException e){
            System.out.println("Failed to read items for sale file, shutting down");
            System.exit(-1);
        }
        for (var entry:allLines){
            var entryValues=entry.split(",");
            ItemType thisItemType=ItemType.Clothing;
            switch (entryValues[2]){
                case "1": thisItemType=ItemType.WICFood;
                case "2": thisItemType=ItemType.Clothing;
                case "3": thisItemType=ItemType.GeneralMerchandise;
            }
            var price=Double.parseDouble(entryValues[1]);
            var newItem= new MerchandiseItem(entryValues[0], price, thisItemType);
            Stock.add(newItem);
        }
    }

    private void loadStartingCustomers(Scanner inputReader) throws IOException{
        Path fullPathName;
        String filename;
        while(true) { //this is for some error checking. It was not required by the assignment
            System.out.print("Enter the name of the file to load customers:");
            filename = inputReader.nextLine();
            fullPathName = Paths.get(filename);
            if (!Files.exists(fullPathName)){ //these three lines checks to see if the file exists, if not go
                System.out.println("No file with that name, please try again....");//do the loop again
            }
            else
                break;
        }
        var allLines=Files.readAllLines(fullPathName);
        Customer currentCustomer=null;
        for(var line: allLines){
            var splitLine = line.split(",");
            switch(splitLine[2]){//I've added a new third item that tells the customer type, R: Residential, B:business, or T:Taxexempt
                case "R"->{
                    currentCustomer = new ResidentialCustomer(splitLine[0], Integer.parseInt(splitLine[1]));
                }
                case "B"->{
                    currentCustomer = new BuisnessCustomer(splitLine[0], Integer.parseInt(splitLine[1]));
                }
                case "T"->{
                    currentCustomer = new TaxExemptCustomer(splitLine[0], Integer.parseInt(splitLine[1]));
                }
                default -> { //this was not needed, but I wanted to model throwing an exception.
                    throw new IOException("Bad file format - invalid customer type specified");
                }
            }

            Customers.add(currentCustomer);
        }
    }

    private static void printMainMenu(){
        System.out.println("======================");
        System.out.println("What would you like to do?");
        System.out.println("   [1] Add customer");
        System.out.println("   [2] Select customer");
        System.out.println("   [3] Exit the program");
        System.out.println("======================");
    }

    public void addCustomer(Scanner inputReader){
        inputReader.nextLine();
        System.out.println("Enter the new customers name:");
        var newName=inputReader.nextLine();
        System.out.println("What kind of customer? \n[1]Residential\n[2]Business\n[3]Tax Exempt");
        var typeNum=inputReader.nextInt();
        switch (typeNum) {
            case 1: {
                var newCustomer = new ResidentialCustomer(newName);
                Customers.add(newCustomer);
            }
            case 2: {
                var newCustomer = new BuisnessCustomer(newName);
                Customers.add(newCustomer);
            }
            case 3: {
                var newCustomer = new TaxExemptCustomer(newName);
                Customers.add(newCustomer);
            }
        }
        System.out.println("Finished adding new customer");
    }

    public Optional<Customer> selectCustomer(Scanner reader){
        System.out.println("enter id of customer");
        var enteredID=reader.nextInt();
        for(var currentCustomer:Customers){
            if (currentCustomer.getCustomerID()==enteredID)
                return Optional.of(currentCustomer);
        }
        System.out.println("No customer with customer ID:"+enteredID);
        return Optional.empty();
    }

    public void manageCustomer(Customer selectedCustomer){
        Scanner secondScanner=new Scanner(System.in);
        while(true){
            printCustomerMenu(selectedCustomer.getName());
            var userChoice=secondScanner.nextInt();
            switch (userChoice){
                case 1 ->addAddress(secondScanner, selectedCustomer);
                case 2->{
                    ShippingAddress selectedAddress = pickAddress(secondScanner,selectedCustomer);
                    makeOrder(selectedAddress,selectedCustomer, secondScanner);
                }
                case 3-> {return;}
                default->System.out.println("Invalid option selected");
            }
        }
    }
    private void printCustomerMenu(String custName){
        System.out.println("======================");
        System.out.println("What do you want to do for Customer " + custName+"?");
        System.out.println("   [1] Add Address to customer");
        System.out.println("   [2] Make an order for the customer");
        System.out.println("   [3] return to the main menu");
        System.out.println("======================");
        System.out.print("Enter the number of your choice:");
    }

    private void addAddress(Scanner secondScanner, Customer selectedCustomer){
        System.out.println("Adding new address for"+selectedCustomer.getName());
        secondScanner.nextLine();
        System.out.println("Enter address line 1:");
        var line1=secondScanner.nextLine();
        System.out.println("Enter address line 2 or <enter> if none:");
        var line2= secondScanner.nextLine();
        System.out.println("Enter City:");
        var city=secondScanner.nextLine();
        System.out.println("Enter State:");
        var state= secondScanner.nextLine();
        System.out.println("Enter postal code");
        var postCode = secondScanner.nextLine();
        var newAddress= new ShippingAddress(line1,line2,city,state,postCode);
        selectedCustomer.addAddress(newAddress);
    }

    private ShippingAddress pickAddress(Scanner secondScanner, Customer selectedCustomer){
        var customerAddresses=selectedCustomer.getAddresses();
        if (customerAddresses.size()==0){
            System.out.println("This customer has no addresses on file");
            addAddress(secondScanner,selectedCustomer);
            return selectedCustomer.getAddresses().get(0);
        }
        var count=0;
        System.out.println("Please select a shipping address from on file");
        for (var address : customerAddresses) {   //I'm being a little 'cute'/clever here
            System.out.print("[" + count + "]"); //but you could do for(int count; count < customerAddresses.size(); count++ for the same effect
            System.out.println(address.toString());
            count++;
        }
        System.out.print("Enter the number of the address for this order:");
        var addressNum = secondScanner.nextInt();

        if (addressNum >= customerAddresses.size()){
            System.out.println("Invalid entry, defaulting to the first address on file...");
            return customerAddresses.get(0);
        }
        else
            return customerAddresses.get(addressNum);
    }

    public void makeOrder(ShippingAddress address, Customer cust, Scanner commandLineInput){
        var cart = new ArrayList<MerchandiseItem>();
        System.out.println("Preparing to make order........");
        while(true){
            printStock();
            System.out.print("type the item number for your order. Select a negative number to end.");
            var choice = commandLineInput.nextInt();
            if (choice<0)
                break;
            if(choice>=Stock.size())//error checking
                continue;
            //if we got here we had a good selection.
            cart.add(Stock.get(choice));//add the item to the cart
        }
        //after adding all the items to the cart,
        var newOrder = new Order(address,cust,cart);
        Orders.add(newOrder);
        System.out.println(".......New order successfully created");
        revenue +=cust.payForOrder(cart);
        cust.arrangeDelivery();
    }

    private void printStock() {
        var count = 0;
        for(var itemForSale: Stock){
            System.out.println("["+count+"] "+itemForSale.getName()+" cost $"+itemForSale.getPrice());
            count++;
        }
    }

    private void CollectOutstandingBalancesFromPurchaseOrders() {
        for(var customer: Customers){
            //loop through all the customers and ask each one to pay their balance, everyone but business customers will return 0
            //then add those new payments to revenue
            var recentPayment = customer.payOutstandingBalance();
            revenue+= recentPayment;
        }
    }
}
