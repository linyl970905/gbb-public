package com.tencent.wxcloudrun.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.TerminalMapper;
import com.tencent.wxcloudrun.dto.EmpRegisterDTO;
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
                if (score >= 85) { // 人脸比对值：85%相似度
                    // 查询该用户绑定的商户
                    Integer checkRelation = terminalMapper.checkRelation(merchant.getId(), Integer.valueOf(userId));
                    if(checkRelation > 0) {
                        // 执行打卡操作

                        return ApiResponse.responseData(200, "打卡成功！", "");
                    } else {
                        // 用户未与该商户进行绑定
                        FaceInfoVO faceInfoVO = new FaceInfoVO().setFaceId(Integer.valueOf(userId)).setSnCode(snCode);
                        return ApiResponse.responseData(201, "用户未与该商户进行绑定！", faceInfoVO);
                    }
                } else {

                    // 创建脸部-雇员信息记录
                    EmployeeManage employee = new EmployeeManage().setFaceUrl(faceUrl);
                    terminalMapper.addEmployeeManage(employee);

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
    public ApiResponse updateEmployeeFace(String faceId, String faceUrl) {
        // 1.获取之前的人脸信息、设备信息、商家信息
        EmployeeManage employee = terminalMapper.getEmployeeByFaceId(faceId);
        MerchantManage merchant = terminalMapper.getMerchantByEmpId(employee.getId());

        // 2.对比人脸库，替换后的照片是否已存在
        String response = faceVerifyService.searchFace(faceUrl);
        if (response == null) {
            return ApiResponse.error("对比百度智能云人脸识别异常！");
        } else {
            cn.hutool.json.JSONObject responseJson = JSONUtil.parseObj(response);
            String result = responseJson.getStr("result");
            cn.hutool.json.JSONObject resultJson = JSONUtil.parseObj(result);
            JSONArray userList = resultJson.getJSONArray("user_list");
            if (userList != null) {
                JSONObject parseObj = JSONUtil.parseObj(userList.get(0));
                // String userId = parseObj.getStr("user_id");
                int score = Integer.valueOf(parseObj.getStr("score"));
                if (score >= 85) {
                    return ApiResponse.error("该人脸照对应的雇员已存在！");
                } else {
                    // 替换人脸库中的人脸信息
                    faceVerifyService.updateFace(faceUrl, employee.getId().toString());
                    return ApiResponse.ok();
                }
            } else {
                return ApiResponse.error("对比百度智能云人脸识别异常！");
            }
        }
    }

    @Override
    public ApiResponse addEmployeeManage(EmpRegisterDTO registerDTO) throws Exception {
        // 1.调用证面提供的实名认证接口，验证实名。
        // 验证通过：调用【员工登记】接口,登记成功后，跳转到“已登记首页”。
        // 接口如失败：弹出失败信息。
        String response = HttpClientPost.Post(registerDTO.getName(), registerDTO.getIdCard(), registerDTO.getFaceUrl());
        cn.hutool.json.JSONObject responseJson = JSONUtil.parseObj(response);
        int code = Integer.valueOf(responseJson.getStr("code"));
        String msg = responseJson.getStr("msg");
        if (code == 200) {
            // 2.更新员工信息
            EmployeeManage employee = new EmployeeManage()
                    .setId(registerDTO.getId())
                    .setOpenId(registerDTO.getOpenId())
                    .setFaceUrl(registerDTO.getFaceUrl())
                    .setName(registerDTO.getName())
                    .setIdCard(registerDTO.getIdCard())
                    .setPhone(registerDTO.getPhone())
                    .setAddress(registerDTO.getAddress())
                    .setJobCode(registerDTO.getJobCode());
            Integer result = terminalMapper.updateEmployeeManage(employee);
            if (result > 0) {
                // 3.将该雇员绑定至商户下
                DeviceManage device = terminalMapper.getDeviceBySnCode(registerDTO.getSnCode());
                MerchantManage merchant = terminalMapper.getMerchantByCloudId(device.getCloudId());
                terminalMapper.addMerEmpRelation(merchant.getId(), employee.getId());
                // 4.上传人脸照片至百度智能云-人脸识别库
                faceVerifyService.insertFace(employee.getFaceUrl(), employee.getId().toString());

                return ApiResponse.ok();
            } else {
                return ApiResponse.error("更新雇员信息失败！");
            }
        } else {
            return ApiResponse.error(msg);
        }
    }
}
