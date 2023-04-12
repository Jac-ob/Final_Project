package sidd0080.mgi.nasafinalproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

class FetchData extends AsyncTask<Void, Void, Void> {
    public List<String> listURL;
    public List<String> listRover;
    public List<String> listCamera;
    private String searchDate;
    private NASARoomViewModel nasaModel;


    public FetchData(List<String> listURL, List<String> listRover, List<String> listCamera, String searchDate, NASARoomViewModel nasaModel) {
        this.listURL = listURL;
        this.listRover = listRover;
        this.searchDate = searchDate;
        this.nasaModel = nasaModel;
        this.listCamera = listCamera;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            String apiKey = "aqBIxjo2ALF77ZHPLKlhHlbCWbDyJBKu6o7RWHTs";
            String apiUrl = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + searchDate + "&page=1&api_key=" + apiKey;

            // Send a GET request to the API URL
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Read the API response as a JSON object
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
            JSONObject response = new JSONObject(sb.toString());

            // Extract the "img_src" and "rover name" fields from the response
            JSONArray photos = response.getJSONArray("photos");
            for (int i = 0; i < photos.length(); i++) {
                JSONObject photo = photos.getJSONObject(i);
                String imgSrc = photo.getString("img_src");
                String roverName = photo.getJSONObject("rover").getString("name");
                String cameraName = photo.getJSONObject("camera").getString("name");
                String urlHTTPS = imgSrc.substring(0, 4) + "s" + imgSrc.substring(4);
                listURL.add(i, urlHTTPS);
                listRover.add(i, roverName);
                listCamera.add(i, cameraName);
//                System.out.println("img_src: " + urlHTTPS);
//                System.out.println("rover name: " + roverName);
//                System.out.println("camera name: " + cameraName);
//                System.out.println("List sizes: " + listRover.size() + ", " + listURL.size() + ", " + listCamera.size());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        for (int i = 0; i < listURL.size(); i++) {
            String url = listURL.get(i);
            String roverName = listRover.get(i);
            String cameraName = listCamera.get(i);
            NASAMessage sendMessage = new NASAMessage(roverName, url, cameraName, true);
            nasaModel.roverNames.getValue().add(sendMessage);
        }
    }

}