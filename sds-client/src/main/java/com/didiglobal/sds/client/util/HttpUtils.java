/**
 *
 */
package com.didiglobal.sds.client.util;

import com.didiglobal.sds.client.log.SdsLoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http工具类
 *
 * @author manzhizhen
 * @version $Id: HttpUtils.java, v 0.1 2016年2月21日 上午11:03:05 Administrator Exp $
 */
public class HttpUtils {

    private static Logger logger = SdsLoggerFactory.getDefaultLogger();

    private static final String CHARSET_NAME = "UTF-8";
    private static final String HTTP_METHOD = "POST";

    public static String post(String urlPath, Map<String, Object> params) throws Exception {

        URL url = new URL(urlPath);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
        connection.setUseCaches(false);
        connection.addRequestProperty("encoding", CHARSET_NAME);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(HTTP_METHOD);

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        OutputStream os = connection.getOutputStream();
        OutputStreamWriter osr = new OutputStreamWriter(os, CHARSET_NAME);
        BufferedWriter bw = new BufferedWriter(osr);

        /**
         * 构建请求参数
         */
        StringBuilder paramStr = new StringBuilder();
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            paramStr.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue().toString(), CHARSET_NAME))
                    .append("&");
        }

        String paramValue = paramStr.toString();
        if (paramValue.endsWith("&")) {
            paramStr.deleteCharAt(paramValue.length() - 1);
        }

        bw.write(paramStr.toString());
        bw.flush();

        InputStream is = connection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, CHARSET_NAME);
        BufferedReader br = new BufferedReader(isr);

        String line;
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        bw.close();
        osr.close();
        os.close();

        br.close();
        isr.close();
        is.close();

        return response.toString();
    }
}
