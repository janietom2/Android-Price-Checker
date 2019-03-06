package cs4330.cs.utep.edu.models;

public class PriceFinder {

    private Item item;

    public PriceFinder(){

    }

    public PriceFinder(String name, String url, double price) {
        this.item = new Item(price, name, url);
    }

    public String getName(){
        return this.item.getName();
    }

    public String getUrl() {
        return this.item.getLink();
    }

    public double getPrice() {
        return this.item.getPrice();
    }

    public double getNewPrice(){
        return this.item.getNewPrice();
    }

    public void randomPrice() {
        double MAX_PRICE = 20000.00;
        double MIN_PRICE = 500.00;
        this.item.setNewPrice(Math.random() * (MAX_PRICE - MIN_PRICE) + 1 + MIN_PRICE);
    }

    public double calculatePrice() {
      //  return ((this.item.getNewPrice() * 100) / this.item.getPrice()) / 10;
        return (this.item.getNewPrice()*100/this.item.getPrice())-100;
    }

    public boolean changePositive(){
        return item.getNewPrice() > item.getPrice();
    }


}
