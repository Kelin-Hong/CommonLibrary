package com.me.commonlibrary.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

public interface HttpApi {
    public String doHttpRequest(HttpRequestBase httpRequest) throws Exception;
    public String doHttpPost(String url,NameValuePair... nameValuePairs) throws Exception;
    public HttpGet createHttpGet(String url,NameValuePair... nameValuePairs) throws Exception;
    public HttpPost createHttpPost(String url,NameValuePair... nameValuePairs) throws Exception;
}
