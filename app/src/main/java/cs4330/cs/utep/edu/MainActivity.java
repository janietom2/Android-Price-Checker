package cs4330.cs.utep.edu;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.ArrayList;

import cs4330.cs.utep.edu.models.Item;
import cs4330.cs.utep.edu.models.JSONReader;
import cs4330.cs.utep.edu.models.PriceFinder;

public class MainActivity extends /*FragmentActivity*/ AppCompatActivity {

    private ArrayList<PriceFinder> items = new ArrayList<PriceFinder>();
    private ListView itemsList;
    private PriceFinderAdapter itemAdapter;
    private static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.ctx = getApplicationContext();

        this.itemsList = (ListView) findViewById(R.id.items_list); // Get Items List View
        ArrayList<PriceFinder> items = new ArrayList<PriceFinder>();
//
//        for (int i = 0; i < 5; i++) {
//            items.add(new PriceFinder( "Item"+i+"", "http://google.com", 100*i+10));
//        }

        JSONReader jr = new JSONReader(ctx,"items.json");
        JSONArray ja = jr.getArray();

        for(int k = 0; k < ja.size(); k++) {
            JSONObject jo = (JSONObject) ja.get(k);
            items.add(new PriceFinder( jo.get("name").toString(), jo.get("url").toString(), Double.valueOf(jo.get("price").toString())));
        }

//        while(jr.getIterator().hasNext()) {
//            Log.i("##", "onCreate: "+jr.getIterator().next());
//        }

        itemAdapter = new PriceFinderAdapter(this, items);
        itemsList.setAdapter(itemAdapter);
        Intent itemIntent = new Intent(this, showItem.class);

        itemsList.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            Gson gson = new Gson();
            String itemDataAsString = gson.toJson(items.get(position)); // Serialize Object to pass it
            itemIntent.putExtra("itemDataAsString", itemDataAsString);
//            itemIntent.putExtra("ITEM_NAME", items.get(position).getName());
//            itemIntent.putExtra("ITEM_URL", items.get(position).getUrl());
//            itemIntent.putExtra("OP", items.get(position).getPrice());
//            items.get(position).randomPrice();
//            itemIntent.putExtra("NP", items.get(position).getNewPrice());
            startActivity(itemIntent);
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
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
