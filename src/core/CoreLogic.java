package core;

import io.read.Settings;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.Push;
import net.Reddit;
import entities.Request;

public class CoreLogic {

	public static void main(String[] args) throws InterruptedException {
		Reddit red = new Reddit();
		Set<Request> requests = null;
		Settings settings = new Settings();
		Push push = new Push(settings.get("application-token"),
				settings.get("user-token"));
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
					requests.add(req);
					System.out.println("Sent: " + req.getTitle() + " by user: "
							+ req.getAuthor());
					push.send(req.getTitle() + " by user: " + req.getAuthor());
					// Add actions for new requests here. For example play a
					// sound locally.

				}
			} else {
				requests = new HashSet<Request>();
				requests.addAll(red.get());
			}

		}
	}
}
