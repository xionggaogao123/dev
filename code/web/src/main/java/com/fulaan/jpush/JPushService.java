package com.fulaan.jpush;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by jerry on 2016/9/28.
 * 极光推送服务(推送给App)
 */
@Service
public class JPushService {


  public void sendMessage() {
    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpPost post = new HttpPost("");
    post.addHeader("Authorization", "Basic N2Q0MzFlNDJkZmE2YTZkNjkzYWMyZDA0OjVlOTg3YWM2ZDJlMDRkOTVhOWQ4ZjBkMQ==");
    try {
      HttpResponse response = client.execute(post);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
