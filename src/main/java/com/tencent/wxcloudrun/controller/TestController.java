package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.TerminalMapper;
import com.tencent.wxcloudrun.service.FaceVerifyService;
import com.tencent.wxcloudrun.service.PunchAttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * @Author: zero
 * @Data: 2022/12/28 16:24
 * @Motto: Nothing is impossible
 */
@RestController
@RequestMapping("/test")
public class TestController implements Serializable {

    @Autowired
    private PunchAttendService punchAttendService;

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    private FaceVerifyService faceVerifyService;

    @GetMapping("/operaPunch")
    public ApiResponse operaPunch(@RequestParam String snCode, @RequestParam String userId) {
        return punchAttendService.punchAttend(snCode, userId);
    }

    @GetMapping("/insureApply")
    public ApiResponse insureApply(@RequestParam String snCode, @RequestParam String userId) throws Exception {
        return punchAttendService.insureApply(snCode, userId);
    }

    /*@GetMapping("/testOne")
    public ApiResponse testOne(@RequestParam String snCode) throws Exception {

        // 公钥 （进行加密）
        String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9CB6yRir7apVzg4HYtPaDHgbKJzXet8qNzmTNmscpqnAtnV3blHY+p9CgJv6FokLgTrn3IaOE7puC3bE7Zuj65VI+ZIbugxV4pzroOUjZ48oisCKgOY4HdUFiIhp/kfuPwocQ4qSsxRClWL3NiZj/AR9ZnvpZTriIA5xggG0xEQIDAQAB";

        // 请求参数
        Map<String, Object> map = MapUtil.newHashMap();
        map.put("snCode", snCode);
        String requestJson = RSAUtils.publicEncrypt(JSONUtil.toJsonStr(map), RSAUtils.getPublicKey(PUBLIC_KEY));

        String requestUrl = "http://gbb.wubaobao.com/terminal/getDeviceBySnCode";

        HttpRequest httpRequest = HttpRequest.post(requestUrl);
        httpRequest.contentType("application/json");
        httpRequest.body(requestJson);
        httpRequest.setConnectionTimeout(18000);
        httpRequest.setReadTimeout(20000);
        HttpResponse execute = null;
        try {
            execute = httpRequest.execute();
        } catch (HttpException e) {
            e.printStackTrace();
        }

        if (execute == null) {
            System.out.println("响应数据为空！");
        } else {
            // 请求接口成功
            if (execute.isOk()) {
                String respBody = execute.body();

                return ApiResponse.ok(respBody);
            }
        }
        return ApiResponse.ok("接口无返回！");
    }*/
}
