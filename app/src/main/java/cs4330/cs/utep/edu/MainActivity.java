package cs4330.cs.utep.edu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import cs4330.cs.utep.edu.models.Item;

public class MainActivity extends AppCompatActivity {

//    private TextView mainAppTitle;
//    private Button changeButton;
    private ArrayList<Item> items = new ArrayList<Item>();
    private ListView itemsList;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.itemsList = findViewById(R.id.items_list); // Get Items List View
        ArrayList<Item> items = new ArrayList<Item>();

        for (int i = 0; i < 5; i++) {
            items.add(new Item(100*i+10, "Item"+i+"", "http://google.com"));
        }

        itemAdapter = new ItemAdapter(this, items);
        itemsList.setAdapter(itemAdapter);


//        this.mainAppTitle = findViewById(R.id.mainTitle);
//        this.changeButton = findViewById(R.id.pressBtn);

//        changeButton.setOnClickListener( view -> {
//            mainAppTitle.setText("Changed!");
//            Log.i("values","clicked!");
//        });

    }
}
