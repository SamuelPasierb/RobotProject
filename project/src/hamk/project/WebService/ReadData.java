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

    @Override
    public void run() {

        while (!Button.ESCAPE.isDown()) {

            try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

            try { 
                url = new URL("http://localhost:8080/lego/rest/lego/get");
                conn = (HttpURLConnection) url.openConnection();
                InputStream is = null;
                try {
                    is = conn.getInputStream();
                } catch (Exception e) {
                    e.printStackTrace();
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

                System.out.println(br.readLine());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}