import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    public void runStore() throws IOException{
        var inputReader=new Scanner(System.in);
        loadStartingCustomers(inputReader);
        loadStockItems();
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
}
