package cs4330.cs.utep.edu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;
import cs4330.cs.utep.edu.models.JSONReader;
import cs4330.cs.utep.edu.models.PriceFinder;

public class MainActivity extends AppCompatActivity implements ЕditDialog.OnCompleteListener {

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

        registerForContextMenu(itemsList);
        itemsList.setOnCreateContextMenuListener(this);

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
        itemAdapter.notifyDataSetChanged();
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
        Toast.makeText(getBaseContext(), "size = " + items.size(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.edit_item:
                showEditDialog(itemPosition);
                return true;
            case R.id.delete_item:
                showDeleteDialog();
                return true;
            case R.id.reload_item:
                setValues(itemPosition);
               // items.get(itemPosition).randomPrice();
               // TextView newPrice = findViewById(R.id.itemPriceNew);
               // newPrice.setText(f.format(items.get(itemPosition).getNewPrice()));
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

    public void setValues(int position){
        Toast.makeText(getBaseContext(), "position = " + itemAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
        itemAdapter.getItem(position).randomPrice();
        itemAdapter.notifyDataSetChanged();
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
                showAddDialog();
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

    public void showDeleteDialog(){
        FragmentManager fm = getSupportFragmentManager();
        DeleteDialog deleteDialogFragment = new DeleteDialog();
        deleteDialogFragment.show(fm, "delete_item");

    }

    public void showAddDialog(){
        FragmentManager fm = getSupportFragmentManager();
        ЕditDialog editDialogFragment = new ЕditDialog();
        editDialogFragment.show(fm, "edit_item");
    }

    public void showEditDialog(int position){
        FragmentManager fm = getSupportFragmentManager();
        ЕditDialog editDialogFragment = new ЕditDialog();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("itemName", itemAdapter.getItem(position).getName());
        args.putString("itemUrl", itemAdapter.getItem(position).getUrl());
        editDialogFragment.setArguments(args);
        editDialogFragment.show(fm, "edit_item");

    }

    @Override
    public void onComplete(int position, String name, String url) {
        //editItem(Item it, double price, String name, String weblink)
     //   items.editItem(itemAdapter.getItem(position), itemAdapter.getItem(position).getNewPrice(), name, url);
        Toast.makeText(getBaseContext(), position + " new name: " + name + "url " + url, Toast.LENGTH_SHORT).show();

    }
}
