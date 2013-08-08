package com.me.commonlibrary.http;

import com.me.commonlibrary.utils.MyLog;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.DownloadManager.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class AbstractHttpApi implements HttpApi  {
    private MyLog LOG=new MyLog(AbstractHttpApi.class);
    private static final String DEFAULT_CLIENT_VERSION = "com.ybole.android";
    private static final String CLIENT_VERSION_HEADER = "User-Agent";
    private static final int TIMEOUT_CONNECTION = 10;
    private static final int TIMEOUT_SOCKET = 65;
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";
    
    private final DefaultHttpClient mHttpClient;
    private final String mClientVersion;
    
    public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion) {

        mHttpClient = httpClient;
        if (clientVersion != null) {
            mClientVersion = clientVersion;
        } else {
            mClientVersion = DEFAULT_CLIENT_VERSION;
        }
    }
    
    
    public String doHttpRequest(HttpRequestBase httpRequest) throws Exception {
        
        mHttpClient.getConnectionManager().closeExpiredConnections();
        HttpResponse response=null;
        try{
            response=mHttpClient.execute(httpRequest);
        }catch(IOException e){
            httpRequest.abort();
            throw e;
        }
        int statusCode=response.getStatusLine().getStatusCode();
        LOG.d(statusCode+"");
        switch(statusCode){
            case 200:
                String content=EntityUtils.toString(response.getEntity());
                LOG.d(content);
                return content;
            case 400:
                response.getEntity().consumeContent();
                LOG.d("HTTP Code:401");
                throw new Exception();
            case 401:
                response.getEntity().consumeContent();
                
                    LOG.d("HTTP Code: 401");
                
                throw new Exception();
            case 402:
                response.getEntity().consumeContent();
                
                LOG.d("HTTP Code: 401");
            
            throw new Exception();

            case 404:
                response.getEntity().consumeContent();
                
                LOG.d("HTTP Code: 401");
            
                throw new Exception();
           
            case 500:
                response.getEntity().consumeContent();
                
                LOG.d("HTTP Code: 401");
            
            throw new Exception();

            case 503:
                response.getEntity().consumeContent();
                
                LOG.d("HTTP Code: 401");
            
                throw new Exception();
            default:
                LOG.d( "Default case for status code reached: "
                            + response.getStatusLine().toString());
                response.getEntity().consumeContent();
                throw new Exception("Error connecting to Server: " + statusCode
                        + ". Try again later.");          
        }
    }

    public String doHttpPost(String url, NameValuePair... nameValuePairs) throws Exception {

        // TODO Auto-generated method stub
        return null;
    }

    public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) throws Exception {

        String query=URLEncodedUtils.format(stripNulls(nameValuePairs),HTTP.UTF_8);
        HttpGet httpGet=new HttpGet(url+'?'+query);
        httpGet.addHeader(CLIENT_VERSION_HEADER,mClientVersion);
        
        return httpGet;
    }

    public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        try{
        httpPost.setEntity(new UrlEncodedFormEntity(stripNulls(nameValuePairs),HTTP.UTF_8));
        }catch(UnsupportedEncodingException e1){
            throw new IllegalArgumentException("Unable to encode http parameters.");
        }
        return httpPost;
    }
    
     public static final DefaultHttpClient createHttpClient()
    {
        final SchemeRegistry register=new SchemeRegistry();
        SocketFactory sf=PlainSocketFactory.getSocketFactory();
        register.register(new Scheme("http", sf, 80));
        HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);
        final ClientConnectionManager ccm=new ThreadSafeClientConnManager(httpParams,register);
        DefaultHttpClient client=new DefaultHttpClient(ccm,httpParams);
        client.addRequestInterceptor(new HttpRequestInterceptor() {
            
            public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            
              //Add header to accept gzip content
                if(!request.containsHeader(HEADER_ACCEPT_ENCODING))
                {
                    request.addHeader(HEADER_ACCEPT_ENCODING,ENCODING_GZIP);
                }
                
            }
        });
        client.addResponseInterceptor(new HttpResponseInterceptor() {
            
            public void process(HttpResponse response, HttpContext context) throws HttpException,
                    IOException {
              final HttpEntity entity=response.getEntity();
              Header headers=entity.getContentEncoding();
              if(headers!=null){
                  for(HeaderElement e: headers.getElements()){
                      if(e.getName().equalsIgnoreCase(HEADER_ACCEPT_ENCODING)){
                          response.setEntity(new InflatingEntity(response.getEntity()));
                      }
                  }
              }
            }
        });
        return client;
    }
     
     public static final DefaultHttpClient createHttpsClient()
     {
         final SchemeRegistry register=new SchemeRegistry();
         try {
            KeyStore trustKeyStore=KeyStore.getInstance(KeyStore.getDefaultType());
            trustKeyStore.load(null, null);
            SSLSocketFactory sf=new TrustSSLSocketFactory(trustKeyStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            register.register(new Scheme("https",sf,443));
         } catch (KeyStoreException e1) {
            
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
           
            e.printStackTrace();
        } catch (CertificateException e) {
           
            e.printStackTrace();
        } catch (IOException e) {
           
            e.printStackTrace();
        } catch (KeyManagementException e) {
            
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
           
            e.printStackTrace();
        }
         SocketFactory sf=PlainSocketFactory.getSocketFactory();
         register.register(new Scheme("http", sf, 80));
         HttpParams httpParams = createHttpParams();
         HttpClientParams.setRedirecting(httpParams, false);
         final ClientConnectionManager ccm=new ThreadSafeClientConnManager(httpParams,register);
         DefaultHttpClient client=new DefaultHttpClient(ccm,httpParams);
         client.addRequestInterceptor(new HttpRequestInterceptor() {
             
             public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
             
               //Add header to accept gzip content
                 if(!request.containsHeader(HEADER_ACCEPT_ENCODING))
                 {
                     request.addHeader(HEADER_ACCEPT_ENCODING,ENCODING_GZIP);
                 }
                 
             }
         });
         client.addResponseInterceptor(new HttpResponseInterceptor() {
             
             public void process(HttpResponse response, HttpContext context) throws HttpException,
                     IOException {
               final HttpEntity entity=response.getEntity();
               Header headers=entity.getContentEncoding();
               if(headers!=null){
                   for(HeaderElement e: headers.getElements()){
                       if(e.getName().equalsIgnoreCase(HEADER_ACCEPT_ENCODING)){
                           response.setEntity(new InflatingEntity(response.getEntity()));
                       }
                   }
               }
             }
         });
         return client;
     }
    private List<NameValuePair> stripNulls(NameValuePair...nameValuePairs){
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        for(NameValuePair pair: nameValuePairs){
        if(pair.getValue()!=null) {
            params.add(pair);
        }
        }
        return params;
    }
    private static final HttpParams createHttpParams(){
        final HttpParams params=new BasicHttpParams();
     // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECTION * 1000);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        return params;
    }
  private static class InflatingEntity extends HttpEntityWrapper{

    public InflatingEntity(HttpEntity wrapped) {

        super(wrapped);
        // TODO Auto-generated constructor stub
    }
    @Override
    public InputStream getContent() throws IOException {
    
        // TODO Auto-generated method stub
        return new GZIPInputStream(wrappedEntity.getContent());
    }
    @Override
 
    public long getContentLength() {
    
        // TODO Auto-generated method stub
        return -1;
    }
  }
}
