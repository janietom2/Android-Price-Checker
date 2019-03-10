package cs4330.cs.utep.edu;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cs4330.cs.utep.edu.models.Item;
import cs4330.cs.utep.edu.models.ItemManager;
import cs4330.cs.utep.edu.models.JSONReader;
import cs4330.cs.utep.edu.models.PriceFinder;

public class MainActivity extends AppCompatActivity {

    private ItemManager itm;
    private ListView itemsList;
    private PriceFinderAdapter itemAdapter;
    private static Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.ctx = getApplicationContext();

        this.itm = new ItemManager();
        this.itm.setFilename("items.json");
        this.itemsList = (ListView) findViewById(R.id.items_list); // Get Items List View
//        ArrayList<PriceFinder> items = new ArrayList<PriceFinder>();

//
        for (int i = 0; i < 5; i++) {
            try {
                itm.addItem(new PriceFinder( "Item"+i+"", "http://google.com", 100*i+10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//
//        JSONReader jr = new JSONReader(ctx,"items.json");
//        JSONArray ja = jr.getArray();
//
//        for(int k = 0; k < ja.size(); k++) {
//            JSONObject jo = (JSONObject) ja.get(k);
//            items.add(new PriceFinder( jo.get("name").toString(), jo.get("url").toString(), Double.valueOf(jo.get("price").toString())));
//        }

//        while(jr.getIterator().hasNext()) {
//            Log.i("##", "onCreate: "+jr.getIterator().next());
//        }

        itemAdapter = new PriceFinderAdapter(this, itm.getList());
        itemsList.setAdapter(itemAdapter);
        Intent itemIntent = new Intent(this, showItem.class);

        registerForContextMenu(itemsList);
        itemsList.setOnCreateContextMenuListener(this);

        itemsList.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Gson gson = new Gson();
            String itemDataAsString = gson.toJson(itm.getList().get(position)); // Serialize Object to pass it
            itemIntent.putExtra("itemDataAsString", itemDataAsString);
            startActivity(itemIntent);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
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
                    itemAdapter.notifyDataSetChanged();
                try {
                    this.itm.removeItem(pf);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
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
}
