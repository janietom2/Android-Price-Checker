package cs4330.cs.utep.edu;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import cs4330.cs.utep.edu.models.ItemManager;
import cs4330.cs.utep.edu.models.PriceFinder;

public class MainActivity extends AppCompatActivity {

    private ItemManager itm;
    private PriceFinderAdapter itemAdapter;
    private String FILE_NAME = "items.json";
    private Gson gson = new Gson();
    private String jsonText;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context ctx = getApplicationContext();
        ArrayList<PriceFinder> tmp = new ArrayList<PriceFinder>();

        this.itm = new ItemManager();
        String text = null;
        try {
            text = load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListView itemsList = (ListView) findViewById(R.id.items_list);


        Log.i("JSON", "onCreate: "+text);
        if(text != null){
            tmp = gson.fromJson(text, new TypeToken<ArrayList<PriceFinder>>(){}.getType());
            tmp.forEach(x -> {
                itm.addItem(x);
            });
        }

        itemAdapter = new PriceFinderAdapter(this, itm.getList());
        itemsList.setAdapter(itemAdapter);
        Intent itemIntent = new Intent(this, showItem.class);

        registerForContextMenu(itemsList);
        itemsList.setOnCreateContextMenuListener(this);

        itemsList.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            String itemDataAsString = gson.toJson(itm.getList().get(position)); // Serialize Object to pass it
            itemIntent.putExtra("itemDataAsString", itemDataAsString);
            startActivity(itemIntent);
        });
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

    //TODO - connect with methods
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        Toast.makeText(getBaseContext(), "position" + itemPosition, Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.edit_item:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_item:

//                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                  PriceFinder pf = new PriceFinder();
                  pf = this.itm.getItem(1);
                  this.itm.removeItem(pf);
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemAdapter.notifyDataSetChanged();
                return true;
            case R.id.reload_item:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.open_detail:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.webpage_item:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //TODO - connect with methods
    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.filter:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.add:
                    PriceFinder pf = new PriceFinder("Mac Mini", "www.google.com", 500.50);
                itm.addItem(pf);
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemAdapter.notifyDataSetChanged();
                return true;

            case R.id.reload:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.settings:
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    public void save() throws IOException {
        FileOutputStream fos = null;
        String jsonSerial = this.gson.toJson(this.itm.getList());

        try{
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(jsonSerial.getBytes());
            Toast.makeText(this, "Saved to"+ getFilesDir() + "/ " + FILE_NAME, Toast.LENGTH_LONG).show();
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

        fis = openFileInput(FILE_NAME);
        InputStreamReader isr =  new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();;

        while((this.jsonText = br.readLine()) != null) {
            sb.append(this.jsonText);
        }

        return sb.toString();
    }



}
