package hamk.project.WebService;

// Imports
import java.net.URI;
import java.net.URL;

public class WriteData extends Thread {
    
    // Ulr
    private String urlString;

    /**
     * Thread used for writing the data to the database using {@code http://localhost:8080/lego/rest/lego/save}.
     */
    public WriteData() {
        this.urlString = "http://localhost:8080/lego/rest/lego/save";
    }

    /**
     * Saves data to the database every second.
     */
    @Override
    public void run() {
        
        while (!this.isInterrupted()) {

            // 1s delay
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

            // Save data
            save();

        }
        
    }

    /**
     * Establishes a connection to the webservice.
     * Sends data to the webservice using Query.
     */
    private void save() {

        // Query
        this.urlString += this.createQuery();

        // Establish connection and send data
        try {
            URL url = new URI(this.urlString).toURL();
            url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Creates a Query for the web service
     * @return Query to add to the link
     */
    private String createQuery() {

        // Query
        String query = "?";

        // TODO: Get all the important information here
        query += "speed=50&turn=1";

        // Return
        return query;
    }

}
