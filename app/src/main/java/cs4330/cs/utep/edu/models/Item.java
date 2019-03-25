package cs4330.cs.utep.edu.models;

public class Item {

    private double price = 0.0;
    private double newPrice;
    private String name;
    private String link;

    Item(){}

    public Item(double price, String name, String link) {
        this.price = price;
        this.newPrice = price;
        this.name = name;
        this.link = link;
    }

    /** Gets **/
    public String getName() {
        return this.name;
    }

    public String getLink(){
        return this.link;
    }

    public double getPrice(){
        return this.price;
    }

    public double getNewPrice(){
        return this.newPrice;
    }


    /** Sets **/

    public void setPrice(double price){
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public boolean changePositive(){
        return newPrice < price;
    }
}
