package com.example.ace.ace.SOSsignal;

        import android.util.Log;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class sms implements Runnable {

    String msg,phone,chabhi;
    public sms(String msg, String phone) {
        this.msg = msg;
        this.phone = phone;
        this.chabhi = "---------:->---------------";
    }

    @Override
    public void run() {
        Boolean complete = true;
        int x=0;

        try {
            // Construct data
            // Toast.makeText(MainActivity.this, "heya", Toast.LENGTH_SHORT).show();
            String apiKey = "apikey=" + this.chabhi;
            String message = "&message=" + msg;
            String sender = "&sender=" + "RSIPUN";
            String numbers = "&numbers=" + "+91" + phone;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Content-Length", Integer.toString(data.length())+3);
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            Log.e(" SMS ","Sucess "+msg+" "+phone+" "+numbers+" Response Code "+conn.getResponseCode());
            Log.e("SMSResponse",stringBuffer.toString());
            complete = false;

            //  Toast.makeText(MainActivity.this, "Sent!", Toast.LENGTH_SHORT).show();

            //  return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            x++;
//            Log.e("Error SMS ",e.getMessage());
            //   return "Error "+e;
        }

    }



}

