package com.me.commonlibrary.http;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TrustSSLSocketFactory extends SSLSocketFactory {
    public SSLContext sslContext=SSLContext.getInstance("TLS");
    public TrustSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
            KeyManagementException, KeyStoreException, UnrecoverableKeyException {

        super(truststore);
       TrustManager tm=new X509TrustManager(){

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

            // TODO Auto-generated method stub
            
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {

            // TODO Auto-generated method stub
            
        }

        public X509Certificate[] getAcceptedIssuers() {

            // TODO Auto-generated method stub
            return null;
        }
           
       
     };
     
    sslContext.init(null,new TrustManager[]{ tm },null);
    }
    @Override
    public Socket createSocket() throws IOException {

        // TODO Auto-generated method stub
        return sslContext.getSocketFactory().createSocket();
    }
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException, UnknownHostException {

        // TODO Auto-generated method stub
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }
  
}
