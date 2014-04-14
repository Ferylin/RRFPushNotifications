package net;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.Request;

public class Reddit {
	private String url = "http://www.reddit.com/r/RedditRescueForce/search?q=flair%3Ahelp+OR+flair%3Apriority&restrict_sr=on&sort=new&t=all";

	public Reddit() {

	}

	/**
	 * Attempts to retrieve a set of rescue requests from the RRF subreddit list
	 * of active requests.
	 * 
	 * @return A set of current requests on the RRF subreddit
	 */
	public Set<Request> get() {
		Set<Request> requests = new HashSet<Request>();
		try {
			// Retrieves the document from the website
			Document doc = Jsoup.connect(url).get();
			// Will split the html to the divs containing the threads
			Elements table = doc.select("#siteTable > div:has(.title)");
			// extracts the requests from the divs and adds them to the return
			// set
			for (Element e : table) {
				String title = e.select(".title > a").text();
				String author = e.select(".tagline > a").first().text();
				String flair = e.select(".linkflairlabel").html();
				// Since reddit fucks up occasionally we check that requests are
				// help requests.
				if (flair.equals("Need Help") || flair.equals("Priority"))
					requests.add(new Request(title, author));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requests;
	}
}
