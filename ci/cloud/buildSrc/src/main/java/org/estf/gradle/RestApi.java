/**
 * Rest API class
 *
 *
 * @author  Liza Dayoub
 *
 */

package org.estf.gradle;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Base64;

public class RestApi {

    private String username;
    private String password;
    private String basicAuthPayload;
    private String version;
    private String upgradeVersion;
    private int majorVersion;
    private int majorUpgradeVersion;

    public RestApi(String username, String password, String version, String upgradeVersion) {
        this.username = username;
        this.password = password;
        this.version = version;
        this.upgradeVersion = upgradeVersion;
        setCredentials();
    }

    // -----------------------------------------------------------------------------------------------------------------
    public HttpResponse post(String path, String jsonstr, Boolean postToKbn) throws IOException {
        HttpPost postRequest = new HttpPost(path);
        postRequest.setHeader(HttpHeaders.AUTHORIZATION, basicAuthPayload);
        if (postToKbn) {
            postRequest.setHeader("kbn-xsrf", "automation");
        }
        System.out.println(path);
        System.out.println(jsonstr);
        postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        StringEntity entity = new StringEntity(jsonstr);
        entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        postRequest.setEntity(entity);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(response.getStatusLine());
        if (statusCode != 200) {
            throw new IOException("FAILED! POST: " + response.getStatusLine() + " " + path);
        }
        return response;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public HttpResponse put(String path, String jsonstr, Boolean postToKbn) throws IOException {
        HttpPut putRequest = new HttpPut(path);
        putRequest.setHeader(HttpHeaders.AUTHORIZATION, basicAuthPayload);
        if (postToKbn) {
            putRequest.setHeader("kbn-xsrf", "automation");
        }
        System.out.println(path);
        System.out.println(jsonstr);
        putRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        StringEntity entity = new StringEntity(jsonstr);
        entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        putRequest.setEntity(entity);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(putRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(response.getStatusLine());
        if (statusCode != 200) {
            throw new IOException("FAILED! PUT: " + response.getStatusLine() + " " + path);
        }
        return response;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public HttpResponse get(String path) throws IOException {
        HttpGet getRequest = new HttpGet(path);
        getRequest.setHeader(HttpHeaders.AUTHORIZATION, basicAuthPayload);
        getRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        System.out.println(path);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(getRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new IOException("FAILED! GET: " + response.getStatusLine() + " " + path);
        }
        return response;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private int parseMajorVersion(String version) throws IOException {
        int dotInd = version.indexOf(".");
        int ret_version;
        if (dotInd != -1) {
            ret_version = Integer.parseInt(version.substring(0,dotInd));
        } else {
            ret_version = Integer.parseInt(version);
        }
        return ret_version;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int setMajorVersion() throws IOException {
        majorVersion = parseMajorVersion(version);
        return majorVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getMajorVersion() {
        return majorVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int setMajorUpgradeVersion() throws IOException {
        majorUpgradeVersion = parseMajorVersion(upgradeVersion);
        return majorUpgradeVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    public int getUpgradeMajorVersion() {
        return majorUpgradeVersion;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private void setCredentials() {
        String credentials = username + ":" + password;
        this.basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
