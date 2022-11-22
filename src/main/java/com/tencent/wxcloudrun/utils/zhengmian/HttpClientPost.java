package com.tencent.wxcloudrun.utils.zhengmian;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import sun.misc.BASE64Encoder;

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
	
	public static String Post(String name, String certNo, String faceUrl) throws Exception{
		 
		 CloseableHttpClient httpClient = null;
	        try {
	            httpClient = getHttpClient();
	            HttpPost httpPost = new HttpPost(API_IDENTIFY_AUTH_URL);
	            Map<String, String> params = new HashMap<String,String>();
	            params.put("apiId", API_ID);
	    		params.put("requestId", "1");
	    		params.put("accessMerchantId", "100053");
	    		params.put("name", encodeData(name));
	    		params.put("certNo", encodeData(certNo));
				String imageContent = imageUrlToBase64(faceUrl);
				params.put("imageContent", imageContent);
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

	            return EntityUtils.toString(resEntity);
	           
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

	public static String imageUrlToBase64(String imgUrl) {
		URL url = null;
		InputStream is = null;
		ByteArrayOutputStream outStream = null;
		HttpURLConnection httpUrl = null;

		try {
			url = new URL(imgUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			httpUrl.getInputStream();

			is = httpUrl.getInputStream();
			outStream = new ByteArrayOutputStream();

			//创建一个Buffer字符串
			byte[] buffer = new byte[1024];
			//每次读取的字符串长度，如果为-1，代表全部读取完毕
			int len = 0;
			//使用输入流从buffer里把数据读取出来
			while( (len = is.read(buffer)) != -1 ){
				//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				outStream.write(buffer, 0, len);
			}

			// 对字节数组Base64编码
			return encode(outStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(is != null) {
					is.close();
				}
				if(outStream != null) {
					outStream.close();
				}
				if(httpUrl != null) {
					httpUrl.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static String encode(byte[] image){
		BASE64Encoder decoder = new BASE64Encoder();
		return replaceEnter(decoder.encode(image));
	}

	public static String replaceEnter(String str){
		String reg ="[\n-\r]";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	public static void main(String[] args) throws Exception {

		//Post("殷杰", "320621197005290539", "https://7072-prod-9gdfw13rcabb4e9a-1314621229.tcb.qcloud.la/others/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20221118102909.png?sign=7b108e87a5616f2d84b31004a7c0c5d2&t=1668738602");

		//Post("林玉亮", "362202199709052835", "https://7072-prod-9gdfw13rcabb4e9a-1314621229.tcb.qcloud.la/others/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20221118102909.png?sign=7b108e87a5616f2d84b31004a7c0c5d2&t=1668738602");

	}

}
