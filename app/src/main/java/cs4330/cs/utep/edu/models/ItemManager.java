package cs4330.cs.utep.edu.models;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    private ArrayList<PriceFinder> itemsList;
    private String filename;
    private Gson gson;

    public ItemManager() {
        this.itemsList = new ArrayList<PriceFinder>();
        this.gson = new Gson();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private Boolean validate(PriceFinder it) {
        return it.getName().isEmpty() || !it.getUrl().isEmpty() || !(it.getPrice() == 0.0);
    }

    public Boolean addItem(PriceFinder it) throws IOException {
        if(!validate(it)){
            return false;
        }
        this.save();
        this.itemsList.add(it);
        // Save to JSON
        return true;
    }

    public Boolean removeItem(PriceFinder it) throws IOException {
        if(this.itemsList.contains(it)){
            this.itemsList.remove(it);
            this.save();
            return true;
        }
        return false;
    }

    public Boolean editItem(PriceFinder it, double price, String name, String weblink) throws IOException {
        if(this.itemsList.contains(it)){
            it.setName(name);
            it.setPrice(price);
            it.setLink(weblink);
            this.save();
            return true;
        }
        return false;
    }

    private Boolean save() throws IOException {
        try {
            this.gson.toJson(this.itemsList, new FileWriter(this.filename));
            return true;
        } catch (JsonIOException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public PriceFinder getItem(int key){
        return this.itemsList.get(key);
    }

    public ArrayList<PriceFinder> getList(){
        return this.itemsList;
    }


}
