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
import org.apache.commons.lang3.StringUtils;

public class HttpClientPost {
	private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;
    private final static String API_ID = "938f25fbcb317065c333e628b0e0d575";
    private final static String API_SECRET = "29bff1a9be081c19fab6d739eff77b78";
    private final static String API_IDENTIFY_AUTH_URL = "https://121.46.26.97:555/auth/identifyAuth";
	
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
	    		params.put("requestId", "1");
	    		params.put("accessMerchantId", "100053");
	    		params.put("name", encodeData("何剑"));
	    		params.put("certNo", "NDUyNzAxMTk4MDEyMzEwMDEx");
	    		params.put("imageContent", "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");
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
