package cs4330.cs.utep.edu;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
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

    PriceFinder item;

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

        Gson gson = new Gson();
        String itemDataAsString = getIntent().getStringExtra("itemDataAsString");
        item = gson.fromJson(itemDataAsString, PriceFinder.class);

        this.itemTitle.setText(item.getName());
        String op = String.valueOf(item.getPrice());
        this.oldPrice.setText("Initial price: $" + op);
        this.newPrice.setText("Current Price: " + String.valueOf(item.getNewPrice()));
        this.itemUrl.setText(item.getUrl());
        this.diff.setText("Price change: 0.00%");


        checkPrice.setOnClickListener( view -> {
            item.randomPrice();
            this.newPrice.setText("Current price: " + f.format(item.getNewPrice()));
            String s;

            if(item.changePositive()) {
                diff.setTextColor(Color.rgb(200, 0, 0));
                s = "+";
            }
            else {
                diff.setTextColor(Color.rgb(0,200,0));
                s = "-";
            }
            diff.setText("Price change: " + s + f.format(item.calculatePrice())+"%");
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
        Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
    }

    //TODO - connect with edit method
    protected void editClicked(View view){
        Toast.makeText(getBaseContext(), "TBD", Toast.LENGTH_SHORT).show();
    }
}
