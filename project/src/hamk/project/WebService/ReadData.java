package hamk.project.WebService;

// Imports
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import hamk.project.Main;
import lejos.utility.Delay;

public class ReadData extends Thread {

    private final String IP;

	private URL url = null;
	private HttpURLConnection conn = null;
	private InputStreamReader inputStreamReader = null;
	private BufferedReader bufferedReader = null;

    String s = null;

    /**
     * Thread for reading data from the web service from {@code http://172.31.164.138:8080/lego/rest/lego/get}
     */
    public ReadData() {
        this.IP = "192.168.0.15"; //"172.31.164.138";
        try {
            url = new URI("http://" + IP + ":8080/lego/rest/lego/get").toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads data from the web service every 100ms
     */
    @Override
    public void run() {

        while (!this.isInterrupted()) {

            // 100ms delay
            Delay.msDelay(50);

            // Read data
            readData();

        }

    }

    /**
     * Reads data from the web service
     */
    private void readData() {

        try { 
            
            // Establish connection
            conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = null;

            try {
                inputStream = conn.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }

            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Read data
            while ((s = bufferedReader.readLine()) != null){
                String [] values = s.split("#");
                
                // Speed
                Main.getPilot().updateSpeed(Integer.parseInt(values[0]));
                
                // Turn
                Main.getPilot().updateTurnValue(Integer.parseInt(values[1]));

                // Line follower
                Main.lineFollowerSwitch(Boolean.parseBoolean(values[2]));

                // Avoidance types
                Main.avoidType = values[3];
            }

            // Close
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}