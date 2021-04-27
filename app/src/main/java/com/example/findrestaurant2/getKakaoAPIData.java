package com.example.findrestaurant2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.lang.String;

public class getKakaoAPIData {

    private String location;
    private final String APIKEY =  "0badefdcbbc22dd6ec5c768ea1442ec5";
    private final int display = 10;
    private static String[] dataString;

    public getKakaoAPIData(String location) {
        this.location = location;
        dataString = new String[display];
    }

    public String[] getAPIData() {

        try {
            location = URLEncoder.encode(location,"UTF-8");
            String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+location+"&page=1&size="+display;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            String auth = "KakaoAK " + APIKEY;
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Requested-With", "curl");
            con.setRequestProperty("Authorization", auth);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            }
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();
            con.disconnect();
            System.out.println(sb);

            String data = sb.toString();
            String[] array;
            array = data.split("\"");

            String[] title = new String[display];
            String[] link = new String[display];

            int k = 0;

            for (int i = 0; i < array.length; i++) {
                if (array[i].equals("place_name"))
                    title[k] = array[i + 2];
                if (array[i].equals("place_url")) {
                    link[k] = array[i + 2];
                    k++;
                }
            }

            for(int i = 0; i < display; i++) {
                dataString[i] = title[i];
                dataString[i+5] = link[i];
            }


        }catch (Exception e) {
            System.out.println(e);
        }

        return dataString;
    }
}
