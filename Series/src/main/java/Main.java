import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        BasicConfigurator.configure();
        port(getAssignedPort());
        String conteudo = fileDownload("http://api.tvmaze.com/shows");
        ArrayList<Info> arrayList = parseConteudo(conteudo);
        get("/getImages", (req, res) -> listToJson(arrayList));
    }

    static int getAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        } else {
            return 8964;
        }
    }

    static String fileDownload(String link) throws IOException {
        String file = "";
        InputStream in = null;
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.getRequestProperty("GET");
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();

            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            file = new String(response, "UTF-8");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return file;
    }

    static ArrayList<Info> parseConteudo (String file) throws JSONException {
        ArrayList<Info> images = new ArrayList<Info>();
        try {
            JSONArray obj = new JSONArray(file);
            for (int i = 0; i < obj.length();i++){
                JSONObject objMusica =  obj.getJSONObject(i);
                int id = objMusica.getInt("id");
                String nome = objMusica.getString("name");
                String summary = objMusica.getString("summary");
                JSONObject getObj = objMusica.getJSONObject("image");
                String url_image = getObj.getString("original");
                images.add(new Info(id, nome, summary, url_image));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return images;
    }

    static JsonArray listToJson(ArrayList<Info> list){
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();
        return myCustomArray;
    }

}