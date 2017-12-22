package science.bbbar326.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App2 {
	public static void main(String[] args) throws Exception {
		Document document = Jsoup.connect("https://www.google.co.jp/search?q=沖縄　高級ホテル").get();
		Elements elements = document.select("h3 a");

		for (Element element : elements) {
			System.out.println("<<< " + element.text() + " >>>");
			System.out.println(element.attr("href"));
			System.out.println("---------------");
		}

	}

}
