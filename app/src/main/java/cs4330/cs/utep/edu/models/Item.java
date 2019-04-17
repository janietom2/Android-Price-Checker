package cs4330.cs.utep.edu.models;

/**
 *
 * Item class gets each individual item properties and will load them into a PriceFinder Object
 * @Author Jose Nieto
 */

public class Item {

    private double price = 0.0;
    private double newPrice;
    private String name;
    private String link;
    private String image;

    Item(){}

    public Item(double price, String name, String link, String image) {
        this.price = price;
        this.newPrice = price;
        this.name = name;
        this.link = link;
        this.image = image;
    }

    //================================================================================
    // Gets
    //================================================================================

    public String getName() {
        return this.name;
    }

    public String getLink(){
        return this.link;
    }

    public String getImage(){
        return this.image;
    }

    public double getPrice(){
        return this.price;
    }

    public double getNewPrice(){
        return this.newPrice;
    }

    //================================================================================
    // Sets
    //================================================================================

    public void setPrice(double price){
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImage(String image) {this.image = image;}

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public boolean changePositive(){
        return newPrice < price;
    }
}
