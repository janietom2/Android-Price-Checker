package cs4330.cs.utep.edu.models;

public class Item {

    private double price;
    private String name;
    private String link;

    Item(){}

    public Item(double price, String name, String link) {
        this.price = price;
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return this.name;
    }

    public String getLink(){
        return this.link;
    }

    public double getPrice(){
        return this.price;
    }
}
