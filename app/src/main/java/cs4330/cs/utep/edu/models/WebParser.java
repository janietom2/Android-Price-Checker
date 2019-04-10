package cs4330.cs.utep.edu.models;

/**
 * This class will use jsoup library to parse the website and get the price and name of the product
 *
 * @author Jose Nieto
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class WebParser {

    private Document doc;

    WebParser(String url) throws IOException {
        // Pase URL
        this.doc = Jsoup.connect(url).userAgent("Opera").get();
    }

    /**
     * Method goes to web and extracts the price
     * For homedepot.com (Comes in 3 parts <span>s (For HomeDepot) (0: "$", 1: "<number> Cost USD", 2: <number> Cents )
     * For www.lowes.com ... //TODO
     * @return Array of Strings with the price
     * @throws IOException
     */
    private String[] webPrice() throws IOException {
        String[] price = new String[3];
        int counter    = 0;

        // Get Price from web
        Elements priceParts = this.doc.select("#ajaxPrice span");

        // Fill array for 3 parts
        for(Element nw : priceParts){
            price[counter] = nw.text();
            counter++;
        }

        return price;
    }

    /**
     * Method goes to web and extracts the name of the product
     * @return String of the name of the Item
     * @throws IOException
     */
    private String productName() throws IOException {
        // Get Name
        Element name = doc.selectFirst(".product-title__title");

        // Print Name
        return name.text();
    }

}
