package com.tencent.wxcloudrun.utils.zhengmian;

import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public class NameHttpClientPost {
	private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;
    private final static String API_ID = "938f25fbcb317065c333e628b0e0d575";
    private final static String API_SECRET = "29bff1a9be081c19fab6d739eff77b78";
    private final static String API_IDENTIFY_AUTH_URL = "https://121.46.26.97:555/auth/nameIdentifyAuth";
	
    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void Post() throws Exception{
		 
		 CloseableHttpClient httpClient = null;
	        try {
	            httpClient = getHttpClient();
	            HttpPost httpPost = new HttpPost(API_IDENTIFY_AUTH_URL);
	            Map<String, String> params = new HashMap<String,String>();
	            params.put("apiId", API_ID);
	    		params.put("requestId", String.valueOf(System.currentTimeMillis()));
	    		params.put("accessMerchantId", "100053");
	    		params.put("name", encodeData("何剑"));
	    		params.put("certNo", "NDUyNzAxMTk4MDEyMzEwMDEx");
	    		params.put("certSn", encodeData("c3851ade47119121db0c23bfcc7d9c1d"));
	    		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
	    		
	    		// 获取签名
	    		params.put("apiSign",md5Signature(params,API_SECRET));
	            // 设置请求参数
	    		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    //给参数赋值
                    formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);
	            
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity resEntity = httpResponse.getEntity();
	            System.out.println("result--->>>" + EntityUtils.toString(resEntity));
	           
	        } catch (Exception e) {throw e;
	        } finally {
	            if (httpClient != null) {
	                httpClient.close();
	            }
	        }
		 
	 }
	
	 public static CloseableHttpClient getHttpClient() throws Exception {
	        CloseableHttpClient httpClient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .setConnectionManager(cm)
	                .setConnectionManagerShared(true)
	                .build();
	        return httpClient;
	    }
	 
	 public static String md5Signature(Map<String, String> params, String secret) {																						
			Set<String> set = params.keySet();																				
			String[] keys = new String[set.size()];																				
			set.toArray(keys);																				
			Arrays.sort(keys);																				
																							
			StringBuffer buffer = new StringBuffer();																				
			for (String key : keys) {																				
				buffer.append(StringUtils.trimToEmpty(params.get(key)));																			
			}																				
			buffer.append(secret);																				
																							
			return DigestUtils.md5Hex(buffer.toString());																				
		}
	 
	 public static String encodeData(String inputData) {
	        try {
	            if (null == inputData) {
	                return null;
	            }
	            return new String(Base64.encodeBase64(inputData.getBytes("UTF-8")), "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	        	e.printStackTrace();
	        }
	        return null;
	    }
	 
	 public static void main(String[] args) throws Exception {
		 Post();
	  }
}
