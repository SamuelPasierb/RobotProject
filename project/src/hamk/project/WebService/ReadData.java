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
import hamk.project.Sensors.UltraSonic;

public class ReadData extends Thread {

	private URL url = null;
	private HttpURLConnection conn = null;
	private InputStreamReader inputStreamReader = null;
	private BufferedReader bufferedReader = null;

    String s=null;

    /**
     * Thread for reading data from the web service from {@code http://172.31.164.138:8080/lego/rest/lego/get}
     */
    public ReadData() {
        try {
            url = new URI("http://172.31.164.138:8080/lego/rest/lego/get").toURL();
            // url = new URI("http://172.31.164.138:8080/lego/rest/lego/save?speed=50&turn=Right&reflection=10&distance=51.2").toURL();
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
            try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

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
                String [] values=s.split("#");
                int speed = Integer.parseInt(values[0]);
                boolean light = Boolean.parseBoolean(values[2]);
                float avoid = Float.parseFloat(values[3]);
                Main.lineFollowerSwitch(light);
                Main.getPilot().setSpeed(speed, speed, true);
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