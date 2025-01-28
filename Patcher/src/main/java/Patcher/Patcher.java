package Patcher;

import com.hawolt.logger.Logger;
import com.seb.io.Reader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class Patcher {

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            patch(args[0], args[1]);
        } else if (args.length == 3) {
            patch(new URL(args[0]), args[1], args[2]);
        }
    }

    public static void patch(String url, String appName) throws IOException {
        patch(new URL(url), appName);
    }

    public static void patch(URL url, String appName) throws IOException {
        patch(url, appName, "jdk-23");
    }

    public static void patch(URL url, String appName, String javaLocation) throws IOException {
        String version = Reader.read(new File("version.txt"));
        float f;
        try {
            f = Float.parseFloat(version);
        } catch (NullPointerException e) {
            f = 0.0f;
        }
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        JSONObject obj = new JSONObject(content.toString());
        float versionNew = Float.parseFloat(obj.getString("name"));
        con.disconnect();
        if (f < versionNew) {
            JSONArray assets = obj.getJSONArray("assets");
            AtomicReference<String> downloadlink = new AtomicReference<>("");
            assets.forEach(asset -> {
                if (((JSONObject) asset).getString("name").equals(appName))
                    downloadlink.set(((JSONObject) asset).getString("browser_download_url"));
            });
            try (BufferedInputStream bis = new BufferedInputStream(new URL(downloadlink.get()).openStream());
                 FileOutputStream fos = new FileOutputStream(appName)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(buffer, 0, 1024)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                Logger.error(e);
            }
        }
        new ProcessBuilder(javaLocation + "/bin/java", "-jar", appName).start();
    }
}
