package com.tencent.wxcloudrun.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * Base64相关工具
 */
public class BaseToImgUtil {

	/**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理 根据文件生成Base64Encoder
	 * 
	 * @param fileName
	 * @return 文件要有完整的路径
	 */
	public static String fileToBase(String fileName) {
		if (null == fileName || "".equals(fileName)) {
			return "";
		}
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(fileName);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 * 
	 * @param base64Encoder
	 * @param fileName
	 * @return
	 */
	public static boolean baseToImg(String base64Encoder, String fileName) {
		// 图像数据为空
		if (base64Encoder == null) {
			return false;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(base64Encoder);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 新生成的图片
			OutputStream out = new FileOutputStream(fileName.toString());
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
