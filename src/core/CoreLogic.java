package core;

import io.read.Settings;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.Push;
import net.Reddit;
import entities.Request;

public class CoreLogic {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Program started.");
		Reddit red = new Reddit();
		Set<Request> requests = null;
		System.out.println("Loading settings.");
		Settings settings = new Settings();
		Pattern ignored = prepareFilterPattern(settings.get("ignore-keywords"));
		Pattern include = prepareFilterPattern(settings.get("filter-keywords"));
		System.out.println("Loaded settings.\nSetting up Push messaging.");
		Push push = new Push(settings.get("application-token"),
				settings.get("user-token"));
		System.out.println("Monitoring RRF requests.");
		// Will run indefinately until shut down, can be fixed at some point.
		while (true) {
			// Thread sleeps for 15 secs to not continually consume cpu and
			// bandwidth
			Thread.sleep(15000l);
			// If it's the first run or the set somehow failed to populate the
			// first time it sets the set to the current requests as to not spam
			// the current requests on launch
			if (!(requests == null || requests.size() == 0)) {
				List<Request> current = red.get();
				for (Request req : current) {
					// checks if the retrieved requests are new and sends a push
					// notification if they are
					if (requests.contains(req))
						break;
					System.out.println("New request!");
					requests.add(req);
					System.out.println(req.getTitle() + " by user: "
							+ req.getAuthor());
					if ((include == null || inFilter(req.getTitle(), include))
							&& (ignored == null || !inFilter(req.getTitle(),
									ignored))) {
						push.send(req.getTitle() + " by user: "
								+ req.getAuthor());
					} else {
						System.out.println("Filtered by filter: "
								+ !(include == null || inFilter(req.getTitle(),
										include)));
						System.out.println("Filtered by exclude: "
								+ !(ignored == null || !inFilter(
										req.getTitle(), ignored)));
					}
					// Add actions for new requests here. For example play a
					// sound locally.

				}
			} else {
				requests = new HashSet<Request>();
				requests.addAll(red.get());
			}

		}
	}

	private static Pattern prepareFilterPattern(String string) {
		if (string.trim().length() == 0)
			return null;
		String pattern = "(?i)(";
		for (String s : string.split(","))
			pattern = pattern + s.trim() + "|";
		if (pattern.charAt(pattern.length() - 1) == '|')
			pattern = pattern.substring(0, pattern.length() - 2) + ")";
		return Pattern.compile(pattern);
	}

	public static boolean inFilter(String s, Pattern pattern) {
		Matcher matcher = pattern.matcher(s);
		return matcher.find();
	}

}
