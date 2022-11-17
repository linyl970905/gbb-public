package com.tencent.wxcloudrun.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.TerminalMapper;
import com.tencent.wxcloudrun.model.DeviceManage;
import com.tencent.wxcloudrun.model.EmployeeManage;
import com.tencent.wxcloudrun.model.MerchantManage;
import com.tencent.wxcloudrun.service.FaceVerifyService;
import com.tencent.wxcloudrun.service.TerminalService;
import com.tencent.wxcloudrun.utils.zhengmian.HttpClientPost;
import com.tencent.wxcloudrun.vo.FaceInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: zero
 * @Data: 2022/11/7 16:32
 * @Motto: Nothing is impossible
 */
@Service
public class TerminalServiceImpl implements TerminalService {

    @Autowired
    private TerminalMapper terminalMapper;

    @Autowired
    private FaceVerifyService faceVerifyService;

    @Override
    public ApiResponse getDeviceBySnCode(String snCode) {
        // 根据sn码查询设备状态
        DeviceManage device = terminalMapper.getDeviceBySnCode(snCode);
        if (device == null) {
            return ApiResponse.responseData(201, "未查询到设备，请激活后使用！", "");
        } else if (device.getStatus() == 0) {
            return ApiResponse.responseData(201, "未查询到设备，请激活后使用！", "");
        } else {
            return ApiResponse.responseData(200, "设备已激活！", "");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse operaPunch(String snCode, String faceUrl) throws Exception {
        // 0.根据sn码查询设备信息、商户信息
        DeviceManage device = terminalMapper.getDeviceBySnCode(snCode);
        MerchantManage merchant = terminalMapper.getMerchantByCloudId(device.getCloudId());

        // 1.使用终端人脸照与百度人脸库数据进行对比，返回比对值score
        String response = faceVerifyService.searchFace(faceUrl);
        if (response == null) {
            return ApiResponse.responseData(201, "对比百度智能云人脸识别异常！", "");
        } else {
            cn.hutool.json.JSONObject responseJson = JSONUtil.parseObj(response);
            String result = responseJson.getStr("result");
            cn.hutool.json.JSONObject resultJson = JSONUtil.parseObj(result);
            JSONArray userList = resultJson.getJSONArray("user_list");
            if (userList != null) {
                JSONObject parseObj = JSONUtil.parseObj(userList.get(0));
                String userId = parseObj.getStr("user_id");
                int score = Integer.valueOf(parseObj.getStr("score"));
                if (merchant.getFaceScore() <= score) {
                    // 执行打卡操作

                    return ApiResponse.responseData(200, "打卡成功！", "");
                } else {
                    // 创建脸部-雇员信息记录
                    EmployeeManage employee = new EmployeeManage().setFaceUrl(faceUrl);
                    terminalMapper.addEmployeeManage(employee);

                    // 同时将该雇员绑定至该设备下
                    terminalMapper.addDeviceEmployee(device.getId(), employee.getId());

                    // 将设备编码sn_code、人脸id返回给前端进行注册操作
                    FaceInfoVO faceInfoVO = new FaceInfoVO().setFaceId(employee.getId()).setSnCode(snCode);
                    return ApiResponse.responseData(201, "用户未注册！", faceInfoVO);
                }
            } else {
                return ApiResponse.responseData(201, "对比百度智能云人脸识别异常！", "");
            }
        }
    }


    @Override
    public EmployeeManage getEmployeeByFaceId(String faceId) {
        return terminalMapper.getEmployeeByFaceId(faceId);
    }

    @Override
    public ApiResponse addEmployeeManage(EmployeeManage employee) throws Exception {
        // 1.调用证面提供的实名认证接口，验证实名。
        // 验证通过：调用【员工登记】接口,登记成功后，跳转到“已登记首页”。
        // 接口如失败：弹出失败信息。
        HttpClientPost.Post();

        // 2.更新员工信息
        Integer result = terminalMapper.updateEmployeeManage(employee);
        if (result > 0) {
            // 3.上传人脸照片至百度智能云-人脸识别库
            faceVerifyService.insertFace(employee.getFaceUrl(), employee.getId().toString());
        }

        return ApiResponse.ok();
    }
}
