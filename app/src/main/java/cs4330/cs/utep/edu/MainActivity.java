package cs4330.cs.utep.edu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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

import cs4330.cs.utep.edu.models.ItemManager;
import cs4330.cs.utep.edu.models.PriceFinder;

public class MainActivity extends AppCompatActivity implements DeleteDialog.DeleteDialogListener {
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
        Context ctx = getApplicationContext();
        ArrayList<PriceFinder> tmp = new ArrayList<PriceFinder>();

        this.filter = (EditText) findViewById(R.id.searchFilter);
        this.filter.setVisibility(View.GONE);

        this.itm = new ItemManager();
        String text = null;
        try {
            text = load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.itemsList = (ListView) findViewById(R.id.items_list);


        if(text != null){
            tmp = gson.fromJson(text, new TypeToken<ArrayList<PriceFinder>>(){}.getType());
            tmp.forEach(x -> {
                itm.addItem(x);
            });
        }

        this.itemAdapter = new PriceFinderAdapter(this, itm.getList());
        this.itemsList.setAdapter(itemAdapter);
        this.itemsList.setTextFilterEnabled(true);


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



        registerForContextMenu(itemsList);
        itemsList.setOnCreateContextMenuListener(this);

        itemsList.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            itemClicked(position);
        });
        itemAdapter.notifyDataSetChanged();
        itemAdapter.setNotifyOnChange(true);

    }

    public void itemClicked(int position){
        Intent itemIntent = new Intent(this, showItem.class);
        String itemDataAsString = gson.toJson(itm.getList()); // Serialize Object to pass it
        itemIntent.putExtra("position", position);
        itemIntent.putExtra("itemDataAsString", itemDataAsString);
        startActivity(itemIntent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    //TODO - connect edit method
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

    //TODO - save it to JSON so it is consistent with item detail
    private void setValues(int position){
        itm.getItem(position).randomPrice();
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void reloadItems(){
        for(int i = 0; i < itemAdapter.getSize(); i++){
            setValues(i);
        }
    }

    //TODO - implement search, filter and settings
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

    public void toggleSearchField() {
        if(this.filter.getVisibility() == View.VISIBLE) {
            this.filter.setVisibility(View.GONE);
        }else{
            this.filter.setVisibility(View.VISIBLE);
        }
    }

    public void showDeleteDialog(int position){
        FragmentManager fm = getSupportFragmentManager();
        DeleteDialog deleteDialogFragment = new DeleteDialog();
        Bundle args = new Bundle();
        args.putInt("position", position);
        deleteDialogFragment.setArguments(args);
        deleteDialogFragment.show(fm, "delete_item");
    }

    public void showAddDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddDialog addDialogFragment = new AddDialog();
        addDialogFragment.show(fm, "add_item");
    }

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

    public void editItem(String name, String source, int position){
        PriceFinder pf = this.itm.getItem(position);
        this.itm.editItem(pf, pf.getPrice(), name, source);
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        itemAdapter.editItem(position, name, source);
        itemAdapter.notifyDataSetChanged();
    }

    public void save() throws IOException {
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

    public String load() throws IOException {
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


}
