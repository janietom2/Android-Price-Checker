package cs4330.cs.utep.edu.models;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private List<Item> itemsList;

    public ItemManager() {
        this.itemsList = new ArrayList<Item>();
    }

    private Boolean validate(Item it) {
        return it.getName().isEmpty() || !it.getLink().isEmpty() || !(it.getPrice() == 0.0);
    }

    public Boolean addItem(Item it) {
        if(!validate(it)){
            return false;
        }

        this.itemsList.add(it);
        // Save to JSON
        return true;
    }

    public Boolean removeItem(Item it){
        if(this.itemsList.contains(it)){
            this.itemsList.remove(it);
            return true;
        }
        return false;
    }

    public Boolean editItem(Item it, double price, String name, String weblink) {
        if(this.itemsList.contains(it)){
            it.setName(name);
            it.setPrice(price);
            it.setLink(weblink);
            return true;
        }

        return false;
    }

    public Boolean save(){
        // Save to GSON
        return false;
    }




}
