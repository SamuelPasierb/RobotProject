package hamk.project.WebService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.server.ExportException;

import hamk.project.Main;
import lejos.hardware.Button;

public class ReadData extends Thread {

	private static URL url = null;
	private static HttpURLConnection conn = null;
	private static InputStreamReader isr = null;
	private static BufferedReader br=null;

    String s=null;

    public ReadData() {
        
    }

    @Override
    public void run() {

        while (!this.isInterrupted()) {

            try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

            try { 
                url = new URL("http://172.31.164.138:8080/lego/rest/lego/get");
                conn = (HttpURLConnection) url.openConnection();
                InputStream is = null;
                try {
                    is = conn.getInputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("here");
                }
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                while ((s=br.readLine())!=null){
					String [] values=s.split("#");
                    int speed = Integer.parseInt(values[0]);
                    Main.getPilot().setSpeed(speed, speed, true);
				}
				br.close();
				isr.close();
				is.close();
				conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}