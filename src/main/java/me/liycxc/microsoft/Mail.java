package me.liycxc.microsoft;

import lombok.extern.slf4j.Slf4j;
import me.liycxc.AppMain;
import me.shivzee.JMailTM;
import me.shivzee.callbacks.EventListener;
import me.shivzee.util.JMailBuilder;
import me.shivzee.util.Message;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.security.auth.login.LoginException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
    public static String MAIL_API = "https://api.yx1024.cc/getAccountApi.aspx?uid=56264&type=69&token=" + AppMain.API_MAIL_TOKEN +"&count=1";

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
                System.out.println("Get mail api error status code: " + execute.getStatusLine().getStatusCode());
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

    public static List<JMailTM> mails = new ArrayList<>();
    public static Object creatMail(String password) {
        try {
            JMailTM mailer = JMailBuilder.createDefault(password);
            mailer.init();
            mails.add(mailer);

            return mailer;
        } catch (LoginException exception) {
            System.out.println("Exception Caught " + exception);
            return exception.toString();
        }
    }

    public static String getCodeByMail(JMailTM mailer) {
        final String[] securityCode = {null};
        mailer.openEventListener(new EventListener() {
            @Override
            public void onMessageReceived(Message message) {
                String[] parts = message.getContent().split("Security code: ");
                String securityCodeValue = parts[1].trim();
                System.out.println("SecurityCode : " + securityCodeValue);
                // To Mark Message As Read
                message.markAsRead(status -> {
                    System.out.println("Message " + message.getId() + " Marked As Read");
                });

                securityCode[0] = securityCodeValue;
            }

            @Override
            public void onError(String error) {
                System.out.println("Some Error Occurred " + error);
                securityCode[0] = "Error " + error;
            }
        });

        while (securityCode[0] == null) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        mailer.closeMessageListener();

        return securityCode[0];
    }
}
