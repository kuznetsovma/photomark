package ru.codeforensics.photomark.restapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONUtil;
import org.apache.http.Consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

class LoginControllerTest {
    @Test
    void login() throws ClientProtocolException, IOException {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            HttpPost httpPost = new HttpPost("http://dev-rest.codeforensics.ru/v1/login");
            String json = "{\"email\":\"qwe@qwe.ru\",\"password\":\"123123\"}";
            StringEntity entity = new StringEntity(json);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(new StringEntity(json, ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8)));

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {

                String result = EntityUtils.toString(response.getEntity());

                ObjectMapper mapper = new ObjectMapper();
                try {
                    Object jsonObject = mapper.readValue(result, Object.class);
                    String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(prettyJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                httpclient.close();
            }
        }
    }
}



