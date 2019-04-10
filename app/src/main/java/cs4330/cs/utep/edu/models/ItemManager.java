package cs4330.cs.utep.edu.models;

/**
 * ItemManager class will contain multiple PriceFinder Objects, and will be used to provide the list
 * of PriceFinder objects. Also provide methods to add, update, delete PriceFinder objects.
 * @author Jose Nieto
 */

import java.util.ArrayList;

public class ItemManager {

    private ArrayList<PriceFinder> itemsList;

    public ItemManager() {
        this.itemsList = new ArrayList<PriceFinder>();
    }


    private Boolean validate(PriceFinder it) {
        return it.getName().isEmpty() || !it.getUrl().isEmpty() || !(it.getPrice() == 0.0);
    }

    public Boolean addItem(PriceFinder it) {
        if(!validate(it)){
            return false;
        }
        this.itemsList.add(it);
        return true;
    }

    public Boolean removeItem(PriceFinder it) {
        if(this.itemsList.contains(it)){
            this.itemsList.remove(it);
            return true;
        }
        return false;
    }

    public void clear(){
        this.itemsList.clear();
    }

    public Boolean editItem(PriceFinder it, double price, String name, String weblink, String image){
        if(this.itemsList.contains(it)){
            it.setName(name);
            it.setPrice(price);
            it.setLink(weblink);
            it.setImage(image);
            return true;
        }
        return false;
    }

    public PriceFinder getItem(int key){
        return this.itemsList.get(key);
    }

    public ArrayList<PriceFinder> getList(){
        return this.itemsList;
    }

}
