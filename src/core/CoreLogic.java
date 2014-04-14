package core;

import java.util.Set;

import net.Push;
import net.Reddit;
import entities.Request;

public class CoreLogic {

	public static void main(String[] args) throws InterruptedException {
		Reddit red = new Reddit();
		Set<Request> requests = null;
		//Fields in the Push constructor needs to be filled with your Pushover details to work
		Push push = new Push("","");
		// Will run indefinately until shut down, can be fixed at some point.
		while (true) {
			// Thread sleeps for 15 secs to not continually consume cpu and
			// bandwidth
			Thread.sleep(15000l);
			// If it's the first run or the set somehow failed to populate the
			// first time it sets the set to the current requests as to not spam
			// the current requests on launch
			if (!(requests == null || requests.size() == 0)) {
				Set<Request> current = red.get();
				for (Request req : current) {
					// checks if the retrieved requests are new and sends a push
					// notification if they are
					if (!requests.contains(req)) {
						requests.add(req);
						push.send(req.getTitle() + " -- by user: "
								+ req.getAuthor());
						System.out.println("Sent: " + req.getTitle()
								+ " -- by user: " + req.getAuthor());
						// Add actions for new requests here. For example play a
						// sound locally.
					}
				}
			} else {
				requests = red.get();
			}

		}
	}
}
