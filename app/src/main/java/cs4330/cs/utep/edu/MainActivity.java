package cs4330.cs.utep.edu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import cs4330.cs.utep.edu.models.DatabaseHelper;
import cs4330.cs.utep.edu.models.ItemManager;
import cs4330.cs.utep.edu.models.PriceFinder;

public class MainActivity extends AppCompatActivity implements DeleteDialog.DeleteDialogListener {

    private DatabaseHelper appDb;
    private ItemManager itm;
    private PriceFinderAdapter itemAdapter;
    private String FILE_NAME = "items.json";
    private Gson gson = new Gson();
    private String jsonText;
    private EditText filter;
    ListView itemsList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variables
        appDb                      = new DatabaseHelper(this);
        String text                = null;
        ArrayList<PriceFinder> tmp = new ArrayList<PriceFinder>();
        this.filter                = findViewById(R.id.searchFilter);
        this.itm                   = new ItemManager();

        // Set visibility of search bar off from the beginning
        this.filter.setVisibility(View.GONE);

        // Check network connection
        if (isNetworkOn()) {
             Toast.makeText(getBaseContext(), "Welcome", Toast.LENGTH_SHORT).show();
        } else {
             showNetWorkDialog();
             Toast.makeText(getBaseContext(), "You are Offline", Toast.LENGTH_SHORT).show();
        }

        // Load into text "items.json" string (If exist)
        try {
            text = load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.itemsList = findViewById(R.id.items_list);

        // Convert text (json text) into objects to load into the adapter list
        if(text != null){
            tmp = gson.fromJson(text, new TypeToken<ArrayList<PriceFinder>>(){}.getType());
            tmp.forEach(x -> {
                itm.addItem(x);
            });
        }

        this.itemAdapter = new PriceFinderAdapter(this, itm.getList());
        this.itemsList.setAdapter(itemAdapter);
        this.itemsList.setTextFilterEnabled(true);

        // Create search filter
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String action = getIntent().getAction();
        String type   = getIntent().getType();
        if (Intent.ACTION_SEND.equalsIgnoreCase(action) && type != null && ("text/plain".equals(type))){
            String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            FragmentManager fm = getSupportFragmentManager();
            AddDialog addDialogFragment = new AddDialog();
            Bundle args = new Bundle();
            args.putString("url", url);
            addDialogFragment.setArguments(args);
            addDialogFragment.show(fm, "add_item");

        }

        registerForContextMenu(itemsList);
        itemsList.setOnCreateContextMenuListener(this);

        itemsList.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            itemClicked(position);
        });
        itemAdapter.notifyDataSetChanged();
        itemAdapter.setNotifyOnChange(true);

    }

    /**
     * Method to get which PriceItem object (item) gets clicked in the ListView
     * @param position
     */
    public void itemClicked(int position){
        Intent itemIntent = new Intent(this, showItem.class);
        String itemDataAsString = gson.toJson(itm.getList()); // Serialize Object to pass it
        itemIntent.putExtra("position", position);
        itemIntent.putExtra("itemDataAsString", itemDataAsString);
        startActivity(itemIntent);
    }


    /**
     * This method is on Android's API, which on restoring the app (if closed or paused) will
     * reload and/or load
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Resumend", "Done!");
        String text = null;
        itm.clear();
        ArrayList<PriceFinder> tmp = new ArrayList<PriceFinder>();

        try {
            text = load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(text != null){
            tmp = gson.fromJson(text, new TypeToken<ArrayList<PriceFinder>>(){}.getType());
            tmp.forEach(x -> {
                itm.addItem(x);
            });
        }
        this.itemAdapter.notifyDataSetChanged();
    }

    //================================================================================
    // Menus and UI methods
    //================================================================================

    /**
     * Method to create context menu (General)
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * Method to create options menu
     * @param menu of the app
     * @return true if everything loads correctly
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    /**
     * Method to create context menu on selected item on ListView
     * @param item selected
     * @return if input exist true
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        switch (item.getItemId()) {
            case R.id.edit_item:
                showEditDialog(itemPosition);
                return true;
            case R.id.delete_item:
                showDeleteDialog(itemPosition);
                return true;
            case R.id.reload_item:
                setValues(itemPosition);
                return true;
            case R.id.open_detail:
                itemClicked(itemPosition);
                return true;
            case R.id.webpage_item:
                String url = itm.getItem(itemPosition).getUrl();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                try {
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e){
                   // Toast.makeText(getBaseContext(), "Webpage " + url + "does not exist", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Menu of options
     * @param item clicked
     * @return true if option exist, false if don't
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                toggleSearchField();
                return true;
            case R.id.add:
                showAddDialog();
                return true;
            case R.id.reload:
                reloadItems();
                return true;
            case R.id.settings:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    /**
     * This method will toggle the EditText field for searching on the ListView of the Items
     */
    public void toggleSearchField() {
        if(this.filter.getVisibility() == View.VISIBLE) {
            this.filter.setVisibility(View.GONE);
        }else{
            this.filter.setVisibility(View.VISIBLE);
        }
    }

    //================================================================================
    // Item management | CRUD
    //================================================================================

    /**
     * Add PriceFinder into ItemManager and save it into "items.json"
     * @param name of the PriceFinder object
     * @param source link of the PriceFinder Object
     *
     */
    public void addItem(String name, String source){
        double MAX_PRICE = 20000.00;
        double MIN_PRICE = 500.00;
        double price = (Math.random() * (MAX_PRICE - MIN_PRICE) + 1 + MIN_PRICE);
        PriceFinder pf = new PriceFinder(name, source, price);
        itm.addItem(pf);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.itemAdapter.addItem(pf);
        this.itemAdapter.notifyDataSetChanged();
    }

    /**
     * Updates PriceFinder object in ItemManager
     * @param name of the PriceFinder Item to be updated
     * @param source Link of the PriceFinder Item to be updated
     * @param position position of PriceFinder in ItemManager to be updated
     * @param image Link of image of the PriceFinder Item to be updated
     */
    public void editItem(String name, String source, int position, String image){
        PriceFinder pf = this.itm.getItem(position);
        this.itm.editItem(pf, pf.getPrice(), name, source, image);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        itemAdapter.editItem(position, name, source);
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * Creates a new price in Pricefinder object in ItemManager and saves it in JSON file
     * @param position of Pricefinder Object in  ItemManager
     */
    private void setValues(int position){
        itm.getItem(position).randomPrice();
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * Reload the prices of all PriceFinder objects
     */
    private void reloadItems(){
        for(int i = 0; i < itemAdapter.getSize(); i++){
            setValues(i);
        }
    }

    //================================================================================
    // Dialogs
    //================================================================================

    /**
     * Pops a dialog for delete a PriceFinder Object
     * @param position in the PriceFinder in ItemManager(ArrayList)
     */
    public void showDeleteDialog(int position){
        FragmentManager fm = getSupportFragmentManager();
        DeleteDialog deleteDialogFragment = new DeleteDialog();
        Bundle args = new Bundle();
        args.putInt("position", position);
        deleteDialogFragment.setArguments(args);
        deleteDialogFragment.show(fm, "delete_item");
    }

    /**
     * Shows a dialog to add a PriceFinder object and adds it to ItemManager
     */
    public void showAddDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddDialog addDialogFragment = new AddDialog();
        addDialogFragment.show(fm, "add_item");
    }

    public void showNetWorkDialog(){
        FragmentManager fm = getSupportFragmentManager();
        WifiDialog addDialogFragment = new WifiDialog();
        addDialogFragment.show(fm, "wifi_dialog");
    }

    /**
     * Pops a dialog to edit PriceFinder properties (Name, URL)
     * @param position in the arraylist ItemManager
     */
    public void showEditDialog(int position){
        FragmentManager fm = getSupportFragmentManager();
        ЕditDialog editDialogFragment = new ЕditDialog(1);
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("itemName", itm.getItem(position).getName());
        args.putString("itemUrl", itm.getItem(position).getUrl());
        editDialogFragment.setArguments(args);
        editDialogFragment.show(fm, "edit_item");
    }

    /**
     * Pops a dialog for delete a PriceFinder Object
     * @param position in the PriceFinder in ItemManager(ArrayList)
     */
    public void DeleteItemDialog(int position){
        PriceFinder pf = new PriceFinder();
        pf = this.itm.getItem(position);
        this.itm.removeItem(pf);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.itemAdapter.removeItem(position);
        itemAdapter.notifyDataSetChanged();
    }

    //================================================================================
    // Write & Load offline information
    //================================================================================

    /**
     * Saves the instance of the Itemmanager (items) into a JSON file "items.json"
     * @throws IOException
     */
    private void save() throws IOException {
        FileOutputStream fos = null;
        String jsonSerial = this.gson.toJson(this.itm.getList());

        try{
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(jsonSerial.getBytes());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * Loads "items.json" into a String format. Reads the file and convert JSON to a String
     * @return JSON from "items.json" in String format
     * @throws IOException
     */
    private String load() throws IOException {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
        } catch(FileNotFoundException e){
            Log.d("File not found", FILE_NAME);
            return null;
        }
        InputStreamReader isr =  new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();

        while((this.jsonText = br.readLine()) != null) {
            sb.append(this.jsonText);
        }

        return sb.toString();
    }

    //================================================================================
    // Network
    //================================================================================

    private Boolean isNetworkOn() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

    }

}
