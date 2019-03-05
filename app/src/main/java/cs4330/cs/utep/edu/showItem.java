package cs4330.cs.utep.edu;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import org.w3c.dom.Text;
import java.text.DecimalFormat;
import cs4330.cs.utep.edu.models.PriceFinder;

public class showItem extends FragmentActivity {

    TextView itemTitle;
    TextView newPrice;
    TextView oldPrice;
    TextView diff;
    EditText itemUrl;
    Button checkPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        DecimalFormat f = new DecimalFormat("##.00");
        this.itemTitle = (TextView) findViewById(R.id.item_name);
        this.oldPrice = (TextView) findViewById(R.id.oldPrice);
        this.newPrice = (TextView) findViewById(R.id.newPrice);
        this.itemUrl = (EditText) findViewById(R.id.itemUrl);
        this.checkPrice = (Button) findViewById(R.id.refreshButton);
        this.diff = (TextView) findViewById(R.id.diff);


        Gson gson = new Gson();
        String itemDataAsString = getIntent().getStringExtra("itemDataAsString");
        PriceFinder item = gson.fromJson(itemDataAsString, PriceFinder.class);

        this.itemTitle.setText(item.getName());
        String op = String.valueOf(item.getPrice());
        this.oldPrice.setText(op);
//        String nPrice = f.format(item.getNewPrice());
//        String np = String.valueOf(f.format();
        this.newPrice.setText("0.00");
        this.itemUrl.setText(item.getUrl());


        checkPrice.setOnClickListener( view -> {
            item.randomPrice();
            this.newPrice.setText(f.format(item.getNewPrice()));
            this.diff.setText(f.format(item.calculatePrice())+"%");
        });

    }
}
