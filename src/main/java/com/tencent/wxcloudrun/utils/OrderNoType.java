package com.tencent.wxcloudrun.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OrderNoType {

    /**
     *
     * @param orderPayType 订单类型：P-支付 T-提现 R-退款 N-未知
     * @param randomStrLen 订单内随机字符串长度
     * @return 订单号(默认30位)
     */
    public static String getOrderNoType(String orderPayType, int randomStrLen) {
        return orderPayType + getLocalDateTimeNow() + getCharAndNumr(randomStrLen,false);
    }

    /**
     *
     * @param orderPayType
     * @param randomStrLen
     * @param onlyNum true 只有数字 false  数字和字母组合
     * @return
     */
    public static String getOrderNoType(String orderPayType, int randomStrLen,Boolean onlyNum) {
        return orderPayType + getLocalDateTimeNow() + getCharAndNumr(randomStrLen,onlyNum);
    }

    private static String getLocalDateTimeNow() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return formatter.format(now);
    }

    private static String getCharAndNumr(int length,Boolean onlyNum) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if(onlyNum){//只有数字
                val += String.valueOf(random.nextInt(10));
            }else{
                // 输出字母还是数字
                String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
                // 字符串
                if ("char".equalsIgnoreCase(charOrNum)) {
                    // 取得大写字母还是小写字母
                    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    val += (char) (choice + random.nextInt(26));
                } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                    val += String.valueOf(random.nextInt(10));
                }
            }

        }
        return val.toUpperCase();
    }
}
