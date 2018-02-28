package com.zz.parsehtml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Francis.zz on 2016-06-07.
 * 描述：jsoup实践Demo <br/>
 */
public class Demo {
    @Test
    public void testGetHtml() throws IOException {
        Document doc = Jsoup.connect("https://www.amazon.de/s?marketplaceID=A1PA6795UKMFR9&me=A1AXW5GDKWR00F&merchant=A1AXW5GDKWR00F&redirect=true").timeout(0).get();
        Elements elements = doc.select(".s-result-item");

        for(Element item : elements) {
            Elements link = item.select("a");
            for(Element e : link) {
                e.attr("href", e.attr("abs:href"));
            }

            Elements imgs = item.select("img");
            for(Element e : imgs) {
                e.attr("src", e.attr("abs:src"));
            }

            String html = item.html();
            System.out.println("<li class=\"item\">" + html + "</li>");
        }
    }
}
