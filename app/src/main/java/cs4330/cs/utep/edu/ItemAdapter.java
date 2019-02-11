package cs4330.cs.utep.edu;

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

public class ItemAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;
    private static LayoutInflater inflater = null;

    public ItemAdapter(@NonNull Context context, @LayoutRes ArrayList<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.items_list,parent,false);

        Item item = items.get(position); // Get current item(object) on the ArrayList of items

        TextView name = (TextView) listItem.findViewById(R.id.itemName);
        name.setText(item.getName());

        TextView price = (TextView) listItem.findViewById(R.id.itemPrice);
        price.setText("$"+item.getPrice());

        return listItem;

    }

}
