package cs4330.cs.utep.edu;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import cs4330.cs.utep.edu.models.ItemManager;
import cs4330.cs.utep.edu.models.PriceFinder;


//TODO - save data in case of orientation change

public class showItem extends FragmentActivity {
    TextView itemTitle;
    TextView newPrice;
    TextView oldPrice;
    TextView diff;
    EditText itemUrl;

    Button checkPrice;
    Button openWebpage;
    Button editItem;
    Button deleteItem;

    private PriceFinder item;
    private ItemManager itm;
    private Gson gson;
    private String FILE_NAME = "items.json";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        DecimalFormat f = new DecimalFormat("##.00");

        this.itemTitle = (TextView) findViewById(R.id.textViewNameItem);
        this.oldPrice = (TextView) findViewById(R.id.textViewInitialPriceItem);
        this.newPrice = (TextView) findViewById(R.id.textViewCurrentPriceItem);
        this.itemUrl = (EditText) findViewById(R.id.editTextSourceItem);
        this.checkPrice = (Button) findViewById(R.id.buttonReloadItem);
        this.diff = (TextView) findViewById(R.id.textViewPriceChangeItem);
        this.openWebpage = (Button) findViewById(R.id.btnWebItem);
        this.editItem = (Button) findViewById(R.id.btnEditItem);
        this.deleteItem = (Button) findViewById(R.id.btnDeleteItem);

        openWebpage.setOnClickListener(this::WebClicked);
        editItem.setOnClickListener(this::editClicked);
        deleteItem.setOnClickListener(this::deleteClicked);

        this.gson = new Gson();
        this.itm = new ItemManager();
        String itemDataAsString = getIntent().getStringExtra("itemDataAsString");

        ArrayList<PriceFinder> tmp = new ArrayList<PriceFinder>();
        tmp = gson.fromJson(itemDataAsString, new TypeToken<ArrayList<PriceFinder>>(){}.getType());
        tmp.forEach(x -> {
            this.itm.addItem(x);
        });

        int position = getIntent().getIntExtra("position", 0);

        this.itemTitle.setText(this.itm.getItem(position).getName());
        String op = String.valueOf(f.format(this.itm.getItem(position).getPrice()));

        this.oldPrice.setText("Initial price: $" + op);
        this.newPrice.setText("Current Price: $" + String.valueOf(f.format(this.itm.getItem(position).getNewPrice())));
        this.itemUrl.setText(this.itm.getItem(position).getUrl());
        this.diff.setText("Price change: 0.00%");


        checkPrice.setOnClickListener( view -> {
            this.itm.getItem(position).randomPrice();
            this.newPrice.setText("Current price: $" + f.format(this.itm.getItem(position).getNewPrice()));
            String s;

            if(this.itm.getItem(position).changePositive()) {
                diff.setTextColor(Color.rgb(200, 0, 0));
                s = "+";
            }
            else {
                diff.setTextColor(Color.rgb(0,200,0));
                s = "-";
            }
            diff.setText("Price change: " + s + f.format(this.itm.getItem(position).calculatePrice())+"%");
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    protected void WebClicked(View view){
        String url = item.getUrl();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
        try {
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e){
             Toast.makeText(getBaseContext(), "Webpage " + url + "does not exist", Toast.LENGTH_SHORT).show();
        }
    }

    //TODO - connect with delete method
    protected void deleteClicked(View view){
        Toast.makeText(getBaseContext(), "Blame", Toast.LENGTH_SHORT).show();
    }

    //TODO - connect with edit method
    protected void editClicked(View view){
        Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
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

}
