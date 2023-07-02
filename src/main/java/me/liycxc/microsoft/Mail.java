package me.liycxc.microsoft;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.liycxc.utils.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.http.HttpClient;

/**
 * This file is part of AutoXGP project.
 * Copyright 2023 Liycxc
 * All Rights Reserved.
 *
 * @author Liycxc
 * @date: 2023-06-30
 * @time: 22:27
 */
@Slf4j
public class Mail {
    public static String MAIL_API = "https://api.yx1024.cc/getAccountApi.aspx?uid=56264&type=69&token=b651978588381cf1c3363daff02705a2&count=1";

    public static String[] getMailByApi() {
        HttpGet httpGet = new HttpGet(URI.create(MAIL_API));

        RequestConfig.Builder builder = RequestConfig
                .custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .setConnectionRequestTimeout(60000);

        RequestConfig defaultRequestConfig = builder.build();
        httpGet.setConfig(defaultRequestConfig);

        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse execute = httpClient.execute(httpGet);) {
            if (execute.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Logger.logger.info("Get mail api error status code: " + execute.getStatusLine().getStatusCode());
                return null;
            }
            HttpEntity entity = execute.getEntity();
            // like -> metelngonyar@hotmail.com----Gn37ms56
            String responseString = EntityUtils.toString(entity);
            String[] parts = responseString.split("\\|+|-{4}");

            String[] result = new String[parts.length];
            System.arraycopy(parts, 0, result, 0, parts.length);

            // 打印结果
            for (String str : result) {
                System.out.println(str);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
