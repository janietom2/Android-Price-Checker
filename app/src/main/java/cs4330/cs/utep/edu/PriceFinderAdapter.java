package cs4330.cs.utep.edu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cs4330.cs.utep.edu.models.Item;
import cs4330.cs.utep.edu.models.PriceFinder;

public class PriceFinderAdapter extends ArrayAdapter<PriceFinder> {

    private Context context;
    private ArrayList<PriceFinder> items;
    private static LayoutInflater inflater = null;


    public PriceFinderAdapter(@NonNull Context context, @LayoutRes ArrayList<PriceFinder> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @SuppressLint("SetTextI18n") // This was added by the IDE
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.items_list,parent,false);

        PriceFinder item = items.get(position); // Get current item(object) on the ArrayList of items

        TextView name = (TextView) listItem.findViewById(R.id.itemName);
        name.setText(item.getName());

        TextView price = (TextView) listItem.findViewById(R.id.itemPrice);
        price.setText("$"+item.getPrice()+" USD");

        //TO DO:  Add link

        return listItem;

    }

}
