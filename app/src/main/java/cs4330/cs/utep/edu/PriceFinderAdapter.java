package cs4330.cs.utep.edu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cs4330.cs.utep.edu.models.Item;
import cs4330.cs.utep.edu.models.PriceFinder;

public class PriceFinderAdapter extends ArrayAdapter<PriceFinder> {

    private Context context;
    private ArrayList<PriceFinder> items;
    private static LayoutInflater inflater = null;


    public PriceFinderAdapter(@NonNull Context context, /*@LayoutRes*/ ArrayList<PriceFinder> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public Filter getFilter(){
        return PriceFinderFilter;
    }

    @SuppressLint("SetTextI18n") // This was added by the IDE
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        DecimalFormat f = new DecimalFormat("##.00");

        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.items_list,parent,false);

        PriceFinder item = items.get(position); // Get current item(object) on the ArrayList of items

        TextView name = (TextView) listItem.findViewById(R.id.itemName);
        name.setText(item.getName().toString());

        TextView price = (TextView) listItem.findViewById(R.id.itemPrice);
        price.setText("$"+f.format(item.getPrice())+" USD");

        String s;
        TextView newPrice = (TextView) listItem.findViewById(R.id.itemPriceNew);

        if(item.changePositive()) {
            newPrice.setTextColor(Color.rgb(200, 0, 0));
            s = "+";
        }
        else {
            newPrice.setTextColor(Color.rgb(0,200,0));
            s = "-";
        }
        newPrice.setText("$" + f.format(item.getNewPrice()) + " USD (" + s + f.format(item.calculatePrice())+"%)");

        return listItem;

    }

    private Filter PriceFinderFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<PriceFinder> suggestions = new ArrayList<>();

            if(constraint == null || constraint.length() == 0 ){
                suggestions.addAll(items);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PriceFinder pf : items) {
                    if(pf.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(pf);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((PriceFinder) resultValue).getName();
        }
    };

    public int getSize(){
        return items.size();
    }

}
