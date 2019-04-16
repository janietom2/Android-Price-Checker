package cs4330.cs.utep.edu;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import cs4330.cs.utep.edu.models.ItemManager;
import cs4330.cs.utep.edu.models.PriceFinder;
import cs4330.cs.utep.edu.models.WebParser;

class AddItemSync extends AsyncTask<Void, Void, Void> {
    private String url;
    private double price;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            String url = "https://www.homedepot.com/p/Hampton-Bay-Redwood-Valley-5-Piece-Metal-Patio-Fire-Pit-Seating-Set-with-Quarry-Red-Cushions-FSS60428RST/205374169";
            Document doc = Jsoup.connect(url).timeout(0).userAgent("Opera").get();
//            String store = getDomainName("https://www.homedepot.com/p/Hampton-Bay-Redwood-Valley-5-Piece-Metal-Patio-Fire-Pit-Seating-Set-with-Quarry-Red-Cushions-FSS60428RST/205374169");
            Elements priceParts = doc.select("#ajaxPrice span");
            int counter = 0;
            StringBuilder stringPrice = new StringBuilder();

            for(Element nw : priceParts){
                if(counter != 0) {
                    if(counter == 1) {
                        stringPrice.append(nw.text());
                        stringPrice.append(".");
                    }else {
                        stringPrice.append(nw.text());
                    }
                }
                counter++;
            }

            setPrice(Double.parseDouble(String.valueOf(stringPrice)));


        } catch (IOException e) {
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