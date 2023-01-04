package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResultMsg;
import com.tencent.wxcloudrun.dao.PunchAttendMapper;
import com.tencent.wxcloudrun.dao.TerminalMapper;
import com.tencent.wxcloudrun.dto.EmpRegisterDTO;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.FaceVerifyService;
import com.tencent.wxcloudrun.service.PunchAttendService;
import com.tencent.wxcloudrun.service.TerminalService;
import com.tencent.wxcloudrun.utils.JsapiTicketUtil;
import com.tencent.wxcloudrun.utils.RSAUtils;
import com.tencent.wxcloudrun.utils.zhengmian.HttpClientPost;
import com.tencent.wxcloudrun.vo.EmployeePageVO;
import com.tencent.wxcloudrun.vo.PunchArrayVO;
import com.tencent.wxcloudrun.vo.PunchDetailVO;
import com.tencent.wxcloudrun.vo.uploadFile.UploadFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private PunchAttendMapper punchAttendMapper;

    @Autowired
    private FaceVerifyService faceVerifyService;

    @Autowired
    private PunchAttendService punchAttendService;

    @Override
    public ResultMsg getDeviceBySnCode(String requestJson) {
        // 解密请求json--获取snCode
        try {
            String json = RSAUtils.getDecodedData(requestJson);
            cn.hutool.json.JSONObject result = JSONUtil.parseObj(json);
            String snCode = result.getStr("snCode");

            // 根据sn码查询设备状态
            DeviceManage device = terminalMapper.getDeviceBySnCode(snCode);
            if (device == null) {
                return ResultMsg.respData(101, "设备未激活！", null);
            } else if (device.getStatus() == 0) {
                return ResultMsg.respData(101, "设备未激活！", null);
            } else {
                return ResultMsg.respData(0, "成功！", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMsg.respData(999, "接口异常！", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultMsg operaPunch(String requestJson) throws Exception {
        // 解密请求json--获取snCode
        try {
            String json = RSAUtils.getDecodedData(requestJson);
            cn.hutool.json.JSONObject result = JSONUtil.parseObj(json);
            String snCode = result.getStr("snCode");
            String faceUrl = result.getStr("faceUrl");

            // 0.根据sn码查询设备信息、商户信息
            DeviceManage device = terminalMapper.getDeviceBySnCode(snCode);
            MerchantManage merchant = terminalMapper.getMerchantByCloudId(device.getCloudId());

            // 1.使用终端人脸照与百度人脸库数据进行对比，返回比对值score
            String response = faceVerifyService.searchFace(faceUrl);
            if (response == null) {
                return ResultMsg.respData(201, "对比百度智能云人脸识别异常！", null);
            } else {
                cn.hutool.json.JSONObject responseJson = JSONUtil.parseObj(response);
                String resultValue = responseJson.getStr("result");
                cn.hutool.json.JSONObject resultJson = JSONUtil.parseObj(resultValue);
                JSONArray userList = resultJson.getJSONArray("user_list");
                if (userList != null) {
                    JSONObject parseObj = JSONUtil.parseObj(userList.get(0));
                    String userId = parseObj.getStr("user_id");
                    int score = NumberUtil.parseInt(parseObj.getStr("score"));
                    if (score >= 80) { // 人脸比对值：85%相似度
                        // 查询该用户绑定的商户
                        Integer checkRelation = terminalMapper.checkRelation(merchant.getId(), Integer.valueOf(userId));
                        if(checkRelation > 0) {
                            // 执行打卡操作
                            //punchAttendService.punchAttend(snCode, userId);
                            // 执行投保操作
                            //punchAttendService.insureApply(snCode, userId);

                            return ResultMsg.respData(0, "成功！", null);
                        } else {
                            // 用户未与该商户进行绑定
                            Map<String, String> mapData = new HashMap<>();
                            mapData.put("faceId", userId);
                            mapData.put("snCode", snCode);
                            return ResultMsg.respData(202, "用户未与商户进行绑定！", mapData);
                        }
                    } else {
                        // 创建脸部-雇员信息记录
                        EmployeeManage employee = new EmployeeManage().setFaceUrl(faceUrl);
                        terminalMapper.addEmployeeManage(employee);

                        // 雇员绑定商户(设备)
                        terminalMapper.addMerEmpRelation(merchant.getId(), employee.getId(), 1);

                        // 将设备编码sn_code、人脸id返回给前端进行注册操作
                        Map<String, String> mapData = new HashMap<>();
                        mapData.put("faceId", employee.getId().toString());
                        mapData.put("snCode", snCode);
                        return ResultMsg.respData(203, "用户未注册！", mapData);
                    }
                } else {
                    return ResultMsg.respData(201, "对比百度智能云人脸识别异常！", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultMsg.respData(999, "接口异常！", null);
    }

    @Override
    public ResultMsg employeeRegister(String requestJson) {
        try {
            String json = RSAUtils.getDecodedData(requestJson);
            cn.hutool.json.JSONObject result = JSONUtil.parseObj(json);
            String name = result.getStr("name");
            String idCard = result.getStr("idCard");
            String snCode = result.getStr("snCode");
            String faceUrl = result.getStr("faceUrl");

            // 1.获取设备信息、商户信息
            DeviceManage device = terminalMapper.getDeviceBySnCode(snCode);
            MerchantManage merchant = terminalMapper.getMerchantByCloudId(device.getCloudId());
            if (device == null) {
                return ResultMsg.respData(301, "设备未激活！", null);
            } else if (device.getStatus() == 0) {
                return ResultMsg.respData(301, "设备未激活！", null);
            }

            // 2.先查询员工表中是否已存在该用户
            EmployeeManage employee = terminalMapper.getEmployeeByInfo(name, idCard);
            if (employee != null) {
                Integer checkRelation = terminalMapper.checkRelation(merchant.getId(), employee.getId());
                if (checkRelation <= 0) {
                    // 3.将该设备与员工绑定
                    terminalMapper.addMerEmpRelation(merchant.getId(), employee.getId(), 0);
                } else {
                    return ResultMsg.respData(302, "用户已注册及绑定，请勿重复操作！", null);
                }
            } else {
                EmployeeManage addEmployee = new EmployeeManage()
                        .setFaceUrl(faceUrl)
                        .setName(name)
                        .setIdCard(idCard)
                        .setPhone(null)
                        .setAddress(null);
                Integer num = terminalMapper.addEmployeeManage(addEmployee);
                if (num > 0) {
                    terminalMapper.addMerEmpRelation(merchant.getId(), addEmployee.getId(), 0);
                    // 4.上传人脸照片至百度智能云-人脸识别库
                    faceVerifyService.insertFace(employee.getFaceUrl(), addEmployee.getId().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultMsg.respData(0, "成功！", null);
    }

    @Override
    public ResultMsg deviceHeartBeat(String requestJson) {
        try {
            String json = RSAUtils.getDecodedData(requestJson);
            cn.hutool.json.JSONObject result = JSONUtil.parseObj(json);
            String snCode = result.getStr("snCode");

            // 更新当前设备的心跳时间
            Integer num = terminalMapper.deviceHeartBeat(snCode);
            if (num <= 0) {
                return ResultMsg.respData(999, "失败！", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMsg.respData(0, "成功！", null);
    }

    @Override
    public EmployeeManage getEmployeeByFaceId(String faceId) {
        return terminalMapper.getEmployeeByFaceId(Integer.valueOf(faceId));
    }

    @Override
    public ApiResponse updateEmployeeFace(String faceId, String faceUrl) {
        // 1.获取之前的人脸信息、设备信息、商家信息
        EmployeeManage employee = terminalMapper.getEmployeeByFaceId(Integer.valueOf(faceId));

        // 2.对比人脸库，替换后的照片是否已存在
        String response = faceVerifyService.searchFace(faceUrl);
        if (response == null) {
            return ApiResponse.error("对比百度智能云人脸识别异常！");
        } else {
            cn.hutool.json.JSONObject resultJson = JSONUtil.parseObj(response);
            JSONArray userList = resultJson.getJSONArray("user_list");
            if (userList != null) {
                JSONObject parseObj = JSONUtil.parseObj(userList.get(0));
                // String userId = parseObj.getStr("user_id");
                int score = NumberUtil.parseInt(parseObj.getStr("score"));
                if (score >= 80) {
                    return ApiResponse.error("该人脸照对应的雇员已存在！");
                } else {
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
                    .setFaceUrl(registerDTO.getFaceUrl())
                    .setName(registerDTO.getName())
                    .setIdCard(registerDTO.getIdCard())
                    .setPhone(registerDTO.getPhone())
                    .setAddress(registerDTO.getAddress());
            Integer result = terminalMapper.updateEmployeeManage(employee);
            if (result > 0) {
                // 3.将该雇员绑定至商户下
                DeviceManage device = terminalMapper.getDeviceBySnCode(registerDTO.getSnCode());
                MerchantManage merchant = terminalMapper.getMerchantByCloudId(device.getCloudId());
                terminalMapper.updateMerEmpRelation(merchant.getId(), employee.getId(), 0);
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

    @Override
    public ApiResponse getAppletsPhone(String code) {
        // 请求参数
        Map<String, Object> requestBody = MapUtil.newHashMap();
        requestBody.put("code", code);

        // 获取当前access_token
        String appid="wx8a2aaf3c34eab56b";
        String appSecret="949e663f7ea974270b0f7ccf4ee6285a";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + appSecret + "";
        String backData = JsapiTicketUtil.sendGet(url, "utf-8", 10000);
        String accessToken = (String) net.sf.json.JSONObject.fromObject(backData).get("access_token");

        // 请求上传文件链接接口
        HttpRequest httpRequest = HttpRequest.post("https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken);
        httpRequest.contentType("application/json");
        httpRequest.body(JSONUtil.toJsonStr(requestBody));
        httpRequest.setConnectionTimeout(18000);
        httpRequest.setReadTimeout(20000);
        HttpResponse execute = null;

        try {
            execute = httpRequest.execute();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        if (execute == null) {
            return ApiResponse.error("提示：请求失败！");
        } else {
            if (execute.isOk()) {
                String respBody = execute.body();

                cn.hutool.json.JSONObject responseJson = JSONUtil.parseObj(respBody);
                String phoneInfo = responseJson.getStr("phone_info");
                cn.hutool.json.JSONObject resultJson = JSONUtil.parseObj(phoneInfo);

                return ApiResponse.ok(resultJson);
            }
        }

        return ApiResponse.error("提示：请求失败！");
    }

    @Override
    public ApiResponse checkEmployeeByPhone(String phone) {
        EmployeeManage employee = terminalMapper.getEmployeeByPhone(phone);
        if (employee != null) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.error("该用户尚未绑定！");
        }
    }

    @Override
    public ApiResponse checkEmployeeInfo(String name, String idCard) {
        EmployeeManage employee = terminalMapper.getEmployeeByInfo(name, idCard);
        if (employee == null) {
            return ApiResponse.error("您输入的姓名、身份证号码对应的人员不存在！");
        } else {
            if (employee.getPhone() != null) {
                return ApiResponse.error("该人员已绑定其他手机号！");
            }
        }
        return ApiResponse.ok(employee);
    }

    @Override
    public ApiResponse bindAppletsPhone(Integer id, String phone) {
        Integer result = terminalMapper.bindAppletsPhone(id, phone);
        if (result > 0) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.error("提示：绑定失败，请稍后重试！");
        }
    }

    @Override
    public EmployeePageVO getEmployeePage(String phone) {
        // 1.获取雇员基本信息
        EmployeePageVO employeePage = terminalMapper.getEmployeePage(phone);
        // 2.获取按天投保最后一次保险过期时间
        InsDayRecord insDayRecord = terminalMapper.getRecordByIdCard(employeePage.getIdCard());
        if (insDayRecord == null) {
            employeePage.setEndTime(null);
        } else {
            employeePage.setEndTime(insDayRecord.getEndTime());
        }
        return employeePage;
    }

    @Override
    public List<MerchantManage> getMerchantList(Integer empId) {
        return terminalMapper.getMerchantByEmpId(empId);
    }

    @Override
    public List<PunchAttendRecord> getRecordByTime(Integer merId, Integer empId, String yearMonthDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        if (yearMonthDay != null) {
            try {
                today = sdf.parse(yearMonthDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return terminalMapper.getRecordByTime(merId, empId, today);
    }

    @Override
    public List<PunchDetailVO> getPunchDetail(Integer merId, Integer empId, String yearMonth) {
        // 最终返回的结果集
        List<PunchDetailVO> punchDetailVOList = new ArrayList<>();

        // 1.获取当月的天数集合
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        yearMonth = yearMonth == null ? sdf.format(new Date()) : yearMonth;
        int days = getMonthDays(yearMonth);
        List<Date> daysList = new ArrayList<>();
        try {
            for (int i = 1; i <= 9; i++) {
                daysList.add(sdf.parse(yearMonth + "-0" + i));
            }
            for (int i = 10; i <= 28; i++) {
                daysList.add(sdf.parse(yearMonth + "-" + i));
            }
            if (days == 29) {
                daysList.add(sdf.parse(yearMonth + "-29"));
            } else if(days == 30) {
                daysList.add(sdf.parse(yearMonth + "-29"));
                daysList.add(sdf.parse(yearMonth + "-30"));
            } else if (days == 31) {
                daysList.add(sdf.parse(yearMonth + "-29"));
                daysList.add(sdf.parse(yearMonth + "-30"));
                daysList.add(sdf.parse(yearMonth + "-31"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 2.获取当月存在的打卡记录
        List<Date> existDateList = punchAttendMapper.getExistPunchDate(merId, empId, yearMonth);
        for (Date day : daysList) {
            PunchDetailVO punchDetailVO = new PunchDetailVO();
            punchDetailVO.setYearMonthDay(day);
            if (existDateList.contains(day)) {
                List<PunchArrayVO> arrayList = punchAttendMapper.getPunchArray(merId, empId, day);
                int i = 0;
                for (PunchArrayVO vo : arrayList) {
                    if(vo.getStatus() != 1) {
                        i++;
                    }
                }
                punchDetailVO.setStatus(i == 0 ? 1 : 2);
                punchDetailVO.setArrayList(arrayList);
            } else {
                punchDetailVO.setStatus(0);
                punchDetailVO.setArrayList(null);
            }
            punchDetailVOList.add(punchDetailVO);
        }

        return punchDetailVOList;
    }

    /**
     * 根据年-月返回当月的天数
     * @param yearMonth
     * @return
     */
    public static int getMonthDays(String yearMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(yearMonth));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
