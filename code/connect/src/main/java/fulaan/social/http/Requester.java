package fulaan.social.http;

import fulaan.social.constant.Charset;
import fulaan.social.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Requester 提供Http调用
 *
 * @author mosl
 * @see HttpClient
 *
 */
public class Requester {

    private static Logger logger = Logger.getLogger(Requester.class);

    private Method method = Method.GET;
    private String baseUrl;
    private final List<Entry> params = new ArrayList<Entry>();
    private CloseableHttpClient client;

    public Requester(String baseUrl) {
        this.baseUrl = baseUrl;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig).build();
    }

    public Requester method(Method method) {
        this.method = method;
        return this;
    }

    public Requester with(String key, int value) {
        return _with(key, value);
    }

    public Requester with(String key, Integer value) {
        if (value != null)
            _with(key, value);
        return this;
    }

    public Requester with(String key, String value) {
        return _with(key, value);
    }

    public Requester with(Map<String, Object> params) {
        if (params != null) {
            for (String key : params.keySet()) {
                this.params.add(new Entry(key, String.valueOf(params.get(key))));
            }
        }
        return this;
    }

    private Requester _with(String key, Object value) {
        if (value != null) {
            params.add(new Entry(key, value));
        }
        return this;
    }

    public <T> T to(String tailApiUrl, Class<T> type) throws IOException {
        return _to(tailApiUrl, type);
    }

    private <T> T _to(String tailApiUrl, Class<T> type) throws IOException {
        if (this.method == Method.GET) {
            String url = this.baseUrl + tailApiUrl + "?" + urlEncode(this.params);
            HttpGet httpGet = new HttpGet(url);
            String json = execute(httpGet);
            return JsonUtil.fromJson(json, type);
        } else if (this.method == Method.POST) {
            String url = this.baseUrl + tailApiUrl;
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Entry entry : this.params) {
                params.add(new BasicNameValuePair(entry.key, String.valueOf(entry.value)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.UTF_8));
            String json = execute(httpPost);
            return JsonUtil.fromJson(json, type);
        } else if (this.method == Method.PUT) {
            String url = this.baseUrl + tailApiUrl;
            HttpPut httpPut = new HttpPut(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Entry entry : this.params) {
                params.add(new BasicNameValuePair(entry.key, String.valueOf(entry.value)));
            }
            httpPut.setEntity(new UrlEncodedFormEntity(params, Charset.UTF_8));
            String json = execute(httpPut);
            return JsonUtil.fromJson(json, type);
        } else if (this.method == Method.DELETE) {
            String url = this.baseUrl + tailApiUrl + "?" + urlEncode(this.params);
            HttpDelete httpDelete = new HttpDelete(url);
            String json = execute(httpDelete);
            return JsonUtil.fromJson(json, type);
        }
        return null;
    }

    private String execute(HttpUriRequest request) throws IOException {
        logger.debug("start execute =========== in Requester ");
        HttpResponse response = client.execute(request);
        logger.debug("end execute =========== in Requester ");
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            return getContentFromInputStream(entity.getContent());
        }
        return null;
    }

    private static String getContentFromInputStream(InputStream inputStream) {
        try {
            return IOUtils.toString(inputStream, Charset.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String urlEncode(List<Entry> entries) {
        String retVal = "";
        for (Entry entry : entries) {
            try {
                String encodeValue = URLEncoder.encode(String.valueOf(entry.value), Charset.UTF_8);
                retVal += entry.key + "=" + encodeValue + "&";
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        return retVal;
    }

    private static class Entry {
        String key;
        Object value;

        private Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        logger.info("===========>");
    }

}
