package cn.lanink.wxnotice.utils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author lt_name
 */
public class Network {

    private Network() {

    }

    public static String get(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            uc.setConnectTimeout(30000);
            uc.setReadTimeout(30000);
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String post(String urlStr, Map<String, String> parameterMap) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setRequestMethod("POST");
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8;");
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3100.0 Safari/537.36");
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(uc.getOutputStream()));
            StringBuilder parameter = new StringBuilder();
            parameter.append("from=WXNotice");
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                parameter.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            pw.write(parameter.toString());
            pw.flush();
            pw.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
