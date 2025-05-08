package hamk.project.WebService;

// Imports
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import hamk.project.Main;

public class WriteData extends Thread {
    
    // URL
    private final String baseURL;
    private final String IP;

    private HttpURLConnection conn;
    private InputStreamReader inputStreamReader = null;
	private BufferedReader bufferedReader = null;

    /**
     * Thread used for writing the data to the database using {@code http://localhost:8080/lego/rest/lego/save}.
     */
    public WriteData() {
        this.IP = /* "192.168.0.15"; */ "172.31.164.138";
        this.baseURL = "http://" + IP + ":8080/lego/rest/lego/save";
    }

    /**
     * Saves data to the database every second.
     */
    @Override
    public void run() {
        
        while (!this.isInterrupted()) {

            // Save data
            save();

            // 1s delay
            try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

        }
        
    }

    /**
     * Establishes a connection to the webservice.
     * Sends data to the webservice using Query.
     */
    private void save() {

        // Query
        String urlString = baseURL + this.createQuery();

        // Establish connection and send data
        try {
            URL url = new URI(urlString).toURL();
            conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = null;

            try {
                inputStream = conn.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }

            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Close
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();
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
        query += "turn=Straight&" + Main.values();

        // Return
        return query;
    }

}
