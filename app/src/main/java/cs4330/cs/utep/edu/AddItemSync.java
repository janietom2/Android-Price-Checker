package cs4330.cs.utep.edu;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URISyntaxException;

import cs4330.cs.utep.edu.models.ItemManager;
import cs4330.cs.utep.edu.models.PriceFinder;
import cs4330.cs.utep.edu.models.WebParser;

class AddItemSync extends AsyncTask<Void, Void, Void> {
    public String url;
//    ProgressBar progressBar;
    double price;
    String name;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try{
            WebParser wp = new WebParser(this.url);
            Log.i("Pass", "pass");
            this.price = wp.getPrice();
            this.name = wp.getName();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void s) {
        super.onPostExecute(s);
//        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}