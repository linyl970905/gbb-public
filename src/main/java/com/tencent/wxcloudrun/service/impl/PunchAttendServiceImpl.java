package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.PunchAttendMapper;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.PunchAttendService;
import com.tencent.wxcloudrun.utils.OrderNoType;
import com.tencent.wxcloudrun.utils.picc.CryptoUtil;
import com.tencent.wxcloudrun.vo.PunchArrayVO;
import com.tencent.wxcloudrun.vo.PunchCollectVO;
import com.tencent.wxcloudrun.vo.PunchDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: zero
 * @Data: 2022/11/22 17:01
 * @Motto: Nothing is impossible
 */
@Service
public class PunchAttendServiceImpl implements PunchAttendService {

    @Autowired
    private PunchAttendMapper punchAttendMapper;

    @Override
    public PunchAttendRule getRuleByMerId(Integer merId) {
        return punchAttendMapper.getRuleByMerId(merId);
    }

    @Override
    public void addRule(PunchAttendRule rule) {
        punchAttendMapper.addRule(rule);
    }

    @Override
    public ApiResponse punchAttend(String snCode, String userId) {
        // 0.当前时间：打卡流程以此时间一致
        Date nowTime = new Date();

        // 1.数据校验不为空
        if (StringUtils.isEmpty(snCode) || StringUtils.isEmpty(userId)) {
            return ApiResponse.error("提示：打卡必需参数为空！");
        }

        // 2.获取商户信息、终端设备信息、雇员信息
        MerchantManage merchant = punchAttendMapper.getMerchantBySnCode(snCode);
        DeviceManage device = punchAttendMapper.getDeviceBySnCode(snCode);
        EmployeeManage employee = punchAttendMapper.getEmployeeById(Integer.valueOf(userId));
        if (merchant == null || device == null || employee == null) {
            return ApiResponse.error("提示：未查询到对应商户/终端设备/雇员信息！");
        }

        // 3.验证终端设备、人员是否开启考勤功能
        if (device.getIsPunch() == 0 || employee.getIsPunch() == 0) {
            return ApiResponse.ok();
        }

        // 4.获取考勤规则进行打卡
        PunchAttendRule rule = punchAttendMapper.getRuleByMerId(merchant.getId());
        if (rule == null) {
            return ApiResponse.error("提示：企业暂未设置打卡规则！");
        } else {
            // 打卡规则，2、4、6次打卡
            Integer punchType = rule.getPunchType();
            if (punchType == 1) { // 2次打卡规则
                try {
                    // 创建打卡记录
                    PunchAttendRecord record = new PunchAttendRecord()
                            .setMerId(merchant.getId())
                            .setEmpId(employee.getId())
                            .setSnCode(snCode)
                            .setName(employee.getName())
                            .setIdCard(employee.getIdCard())
                            .setPhone(employee.getPhone())
                            .setAddress(employee.getAddress())
                            .setJobCode(employee.getJobCode())
                            .setStatus(0)
                            .setCreateTime(nowTime);

                    // 获取需要打卡的时间
                    Date oneStartTime = formatTime(nowTime, rule.getOneStartTime());
                    Date oneNormalTime = formatTime(nowTime, rule.getOneNormalTime());
                    Date oneEndTime = formatTime(nowTime, rule.getOneEndTime());
                    Date twoStartTime = formatTime(nowTime, rule.getTwoStartTime());
                    Date twoNormalTime = formatTime(nowTime, rule.getTwoNormalTime());
                    Date twoEndTime = formatTime(nowTime, rule.getTwoEndTime());

                    if (nowTime.before(oneStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(oneStartTime) && nowTime.before(oneNormalTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, oneStartTime, oneNormalTime);
                        if (asNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(1);
                        }
                    } else if (nowTime.after(oneNormalTime) && nowTime.before(oneEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, oneStartTime, oneNormalTime);
                        Integer cdNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 2, oneNormalTime, oneEndTime);
                        if(asNum > 0 || cdNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(2);
                        }
                    } else if (nowTime.after(oneEndTime) && nowTime.before(twoStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(twoStartTime) && nowTime.before(twoNormalTime)) {
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, twoStartTime, twoNormalTime);
                        if (ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, twoStartTime, twoNormalTime);
                        }
                        record.setStatus(3);
                    } else if (nowTime.after(twoNormalTime) && nowTime.before(twoEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, twoNormalTime, twoEndTime);
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, twoStartTime, twoNormalTime);
                        if (asNum > 0 || ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, twoStartTime, twoEndTime);
                        }
                        record.setStatus(1);
                    } else if (nowTime.after(twoEndTime)) {
                        record.setStatus(0);
                    }

                    punchAttendMapper.addPunchAttendRecord(record);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (punchType == 2) { // 4次打卡规则
                try {
                    // 创建打卡记录
                    PunchAttendRecord record = new PunchAttendRecord()
                            .setMerId(merchant.getId())
                            .setEmpId(employee.getId())
                            .setSnCode(snCode)
                            .setName(employee.getName())
                            .setIdCard(employee.getIdCard())
                            .setPhone(employee.getPhone())
                            .setAddress(employee.getAddress())
                            .setJobCode(employee.getJobCode())
                            .setStatus(0)
                            .setCreateTime(nowTime);

                    // 获取需要打卡的时间
                    Date oneStartTime = formatTime(nowTime, rule.getOneStartTime());
                    Date oneNormalTime = formatTime(nowTime, rule.getOneNormalTime());
                    Date oneEndTime = formatTime(nowTime, rule.getOneEndTime());
                    Date twoStartTime = formatTime(nowTime, rule.getTwoStartTime());
                    Date twoNormalTime = formatTime(nowTime, rule.getTwoNormalTime());
                    Date twoEndTime = formatTime(nowTime, rule.getTwoEndTime());
                    Date threeStartTime = formatTime(nowTime, rule.getThreeStartTime());
                    Date threeNormalTime = formatTime(nowTime, rule.getThreeNormalTime());
                    Date threeEndTime = formatTime(nowTime, rule.getThreeEndTime());
                    Date fourStartTime = formatTime(nowTime, rule.getFourStartTime());
                    Date fourNormalTime = formatTime(nowTime, rule.getFourNormalTime());
                    Date fourEndTime = formatTime(nowTime, rule.getFourEndTime());

                    if (nowTime.before(oneStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(oneStartTime) && nowTime.before(oneNormalTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, oneStartTime, oneNormalTime);
                        if (asNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(1);
                        }
                    } else if (nowTime.after(oneNormalTime) && nowTime.before(oneEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, oneStartTime, oneNormalTime);
                        Integer cdNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 2, oneNormalTime, oneEndTime);
                        if(asNum > 0 || cdNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(2);
                        }
                    } else if (nowTime.after(oneEndTime) && nowTime.before(twoStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(twoStartTime) && nowTime.before(twoNormalTime)) {
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, twoStartTime, twoNormalTime);
                        if (ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, twoStartTime, twoNormalTime);
                        }
                        record.setStatus(3);
                    } else if (nowTime.after(twoNormalTime) && nowTime.before(twoEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, twoNormalTime, twoEndTime);
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, twoStartTime, twoNormalTime);
                        if (asNum > 0 || ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, twoStartTime, twoEndTime);
                        }
                        record.setStatus(1);
                    } else if (nowTime.after(twoEndTime) && nowTime.before(threeStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(threeStartTime) && nowTime.before(threeNormalTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, threeStartTime, threeNormalTime);
                        if (asNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(1);
                        }
                    } else if (nowTime.after(threeNormalTime) && nowTime.before(threeEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, threeStartTime, threeNormalTime);
                        Integer cdNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 2, threeNormalTime, threeEndTime);
                        if(asNum > 0 || cdNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(2);
                        }
                    } else if (nowTime.after(threeEndTime) && nowTime.before(fourStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(fourStartTime) && nowTime.before(fourNormalTime)) {
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, fourStartTime, fourNormalTime);
                        if (ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, fourStartTime, fourNormalTime);
                        }
                        record.setStatus(3);
                    } else if (nowTime.after(fourNormalTime) && nowTime.before(fourEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, fourNormalTime, fourEndTime);
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, fourStartTime, fourNormalTime);
                        if (asNum > 0 || ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, fourStartTime, fourEndTime);
                        }
                        record.setStatus(1);
                    } else if (nowTime.after(fourEndTime)) {
                        record.setStatus(0);
                    }

                    punchAttendMapper.addPunchAttendRecord(record);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (punchType == 3) { // 6次打卡规则
                try {
                    // 创建打卡记录
                    PunchAttendRecord record = new PunchAttendRecord()
                            .setMerId(merchant.getId())
                            .setEmpId(employee.getId())
                            .setSnCode(snCode)
                            .setName(employee.getName())
                            .setIdCard(employee.getIdCard())
                            .setPhone(employee.getPhone())
                            .setAddress(employee.getAddress())
                            .setJobCode(employee.getJobCode())
                            .setStatus(0)
                            .setCreateTime(nowTime);

                    // 获取需要打卡的时间
                    Date oneStartTime = formatTime(nowTime, rule.getOneStartTime());
                    Date oneNormalTime = formatTime(nowTime, rule.getOneNormalTime());
                    Date oneEndTime = formatTime(nowTime, rule.getOneEndTime());
                    Date twoStartTime = formatTime(nowTime, rule.getTwoStartTime());
                    Date twoNormalTime = formatTime(nowTime, rule.getTwoNormalTime());
                    Date twoEndTime = formatTime(nowTime, rule.getTwoEndTime());
                    Date threeStartTime = formatTime(nowTime, rule.getThreeStartTime());
                    Date threeNormalTime = formatTime(nowTime, rule.getThreeNormalTime());
                    Date threeEndTime = formatTime(nowTime, rule.getThreeEndTime());
                    Date fourStartTime = formatTime(nowTime, rule.getFourStartTime());
                    Date fourNormalTime = formatTime(nowTime, rule.getFourNormalTime());
                    Date fourEndTime = formatTime(nowTime, rule.getFourEndTime());
                    Date fiveStartTime = formatTime(nowTime, rule.getFiveStartTime());
                    Date fiveNormalTime = formatTime(nowTime, rule.getFiveNormalTime());
                    Date fiveEndTime = formatTime(nowTime, rule.getFiveEndTime());
                    Date sixStartTime = formatTime(nowTime, rule.getSixStartTime());
                    Date sixNormalTime = formatTime(nowTime, rule.getSixNormalTime());
                    Date sixEndTime = formatTime(nowTime, rule.getSixEndTime());

                    if (nowTime.before(oneStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(oneStartTime) && nowTime.before(oneNormalTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, oneStartTime, oneNormalTime);
                        if (asNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(1);
                        }
                    } else if (nowTime.after(oneNormalTime) && nowTime.before(oneEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, oneStartTime, oneNormalTime);
                        Integer cdNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 2, oneNormalTime, oneEndTime);
                        if(asNum > 0 || cdNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(2);
                        }
                    } else if (nowTime.after(oneEndTime) && nowTime.before(twoStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(twoStartTime) && nowTime.before(twoNormalTime)) {
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, twoStartTime, twoNormalTime);
                        if (ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, twoStartTime, twoNormalTime);
                        }
                        record.setStatus(3);
                    } else if (nowTime.after(twoNormalTime) && nowTime.before(twoEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, twoNormalTime, twoEndTime);
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, twoStartTime, twoNormalTime);
                        if (asNum > 0 || ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, twoStartTime, twoEndTime);
                        }
                        record.setStatus(1);
                    } else if (nowTime.after(twoEndTime) && nowTime.before(threeStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(threeStartTime) && nowTime.before(threeNormalTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, threeStartTime, threeNormalTime);
                        if (asNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(1);
                        }
                    } else if (nowTime.after(threeNormalTime) && nowTime.before(threeEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, threeStartTime, threeNormalTime);
                        Integer cdNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 2, threeNormalTime, threeEndTime);
                        if(asNum > 0 || cdNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(2);
                        }
                    } else if (nowTime.after(threeEndTime) && nowTime.before(fourStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(fourStartTime) && nowTime.before(fourNormalTime)) {
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, fourStartTime, fourNormalTime);
                        if (ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, fourStartTime, fourNormalTime);
                        }
                        record.setStatus(3);
                    } else if (nowTime.after(fourNormalTime) && nowTime.before(fourEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, fourNormalTime, fourEndTime);
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, fourStartTime, fourNormalTime);
                        if (asNum > 0 || ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, fourStartTime, fourEndTime);
                        }
                        record.setStatus(1);
                    } else if (nowTime.after(fourEndTime) && nowTime.before(fiveStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(fiveStartTime) && nowTime.before(fiveNormalTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, fiveStartTime, fiveNormalTime);
                        if (asNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(1);
                        }
                    } else if (nowTime.after(fiveNormalTime) && nowTime.before(fiveEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, fiveStartTime, fiveNormalTime);
                        Integer cdNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 2, fiveNormalTime, fiveEndTime);
                        if(asNum > 0 || cdNum > 0) {
                            record.setStatus(0);
                        } else {
                            record.setStatus(2);
                        }
                    } else if (nowTime.after(fiveEndTime) && nowTime.before(sixStartTime)) {
                        record.setStatus(0);
                    } else if (nowTime.after(sixStartTime) && nowTime.before(sixNormalTime)) {
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, sixStartTime, sixNormalTime);
                        if (ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, fourStartTime, fourNormalTime);
                        }
                        record.setStatus(3);
                    } else if (nowTime.after(sixNormalTime) && nowTime.before(sixEndTime)) {
                        Integer asNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 1, sixNormalTime, sixEndTime);
                        Integer ztNum = punchAttendMapper.getNumByTime(merchant.getId(), employee.getId(), 3, sixStartTime, sixNormalTime);
                        if (asNum > 0 || ztNum > 0) {
                            punchAttendMapper.syncPunchStatus(merchant.getId(), employee.getId(), 0, fourStartTime, fourEndTime);
                        }
                        record.setStatus(1);
                    } else if (nowTime.after(sixEndTime)) {
                        record.setStatus(0);
                    }

                    punchAttendMapper.addPunchAttendRecord(record);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return ApiResponse.ok();
    }

    /**
     * 根据传入的时间(时分秒)自动拼接上当前年月日
     * @param nowTime
     * @param paramTime
     * @return
     * @throws ParseException
     */
    public static Date formatTime(Date nowTime, String paramTime) throws ParseException {
        // 定义转换格式
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前年月日-后期拼接打卡日期
        String subYmd = sdf1.format(nowTime);
        String jointTime = subYmd + " " + paramTime;
        return sdf2.parse(jointTime);
    }

    /**
     * 时间比较：
     * 当前时间大于目标时间-TRUE
     * 当前时间小于目标时间-FALSE
     * @param nowTime
     * @param targetTime
     * @return
     */
    public static Boolean compareTime(Date nowTime, Date targetTime) {
        if(nowTime.compareTo(targetTime) == 1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public ApiResponse insureApply(String snCode, String userId) throws Exception {
        // 0.当前时间：打卡流程以此时间一致
        Date nowTime = new Date();

        // 1.数据校验不为空
        if (StringUtils.isEmpty(snCode) || StringUtils.isEmpty(userId)) {
            return ApiResponse.error("提示：投保必需参数为空！");
        }

        // 2.获取商户信息、终端设备信息、雇员信息
        MerchantManage merchant = punchAttendMapper.getMerchantBySnCode(snCode);
        DeviceManage device = punchAttendMapper.getDeviceBySnCode(snCode);
        EmployeeManage employee = punchAttendMapper.getEmployeeById(Integer.valueOf(userId));
        if (merchant == null || device == null || employee == null) {
            return ApiResponse.error("提示：未查询到对应商户/终端设备/雇员信息！");
        }

        // 3.验证终端设备、人员是否开启投保功能
        if (device.getIsInsure() == 0 || employee.getIsInsure() == 0) {
            return ApiResponse.ok();
        }

        // 4.验证商户的账户余额是否充足

        // 5.进行投保操作
        InsDayRecord insDayRecord = punchAttendMapper.getInsDayRecordByIdCard(employee.getIdCard());
        if (insDayRecord == null) {
            insureOpera(nowTime, snCode, merchant, employee);
        } else {
            Integer todayNum = punchAttendMapper.getInsDayToday(employee.getIdCard());
            if (todayNum <= 0) {
                if (nowTime.after(insDayRecord.getStartTime()) && nowTime.before(insDayRecord.getEndTime())) {
                    insureOpera(insDayRecord.getEndTime(), snCode, merchant, employee);
                } else if (nowTime.after(insDayRecord.getEndTime())) {
                    insureOpera(nowTime, snCode, merchant, employee);
                }
            }
        }

        return ApiResponse.ok();
    }

    /**
     * 对接人保接口-三方投保接口
     * @param nowTime
     * @param snCode
     * @param merchant
     * @param employee
     * @throws Exception
     */
    public void insureOpera(Date nowTime, String snCode, MerchantManage merchant, EmployeeManage employee) throws Exception {
        // 时间格式1
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        // 时间格式2
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 根据起保时间获取止保时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowTime);
        cal.add(Calendar.YEAR, 1);
        Date endTime = cal.getTime();

        // 定义基本参数
        String orderNo = OrderNoType.getOrderNoType("GBBDAY", 2);

        String nonce = CryptoUtil.uuid();
        String timestamp = sdf1.format(nowTime);
        String projectCode = "GbbInsure";
        String token = "d457bedd038b48acac3fc2e00c605490";
        String encryptionKey = "52cbaaf00b2947e380c664532d0613de";

        String signature = CryptoUtil.sign(token, timestamp, nonce, projectCode);

        // 封装请求体对象
        // requestBody
        Map<String, Object> requestBody = MapUtil.newHashMap();
        requestBody.put("requestId", nonce);
        requestBody.put("requestType", "wesure.autorenewal.openinsure.underwriting");
        requestBody.put("requestTime", timestamp);

        List<Map<String, Object>> requestData = new ArrayList<>();
        Map<String, Object> data = MapUtil.newHashMap();
        data.put("insureId", orderNo);
        data.put("workPost", employee.getJobCode());
        data.put("dutyStation", merchant.getAddress());
        data.put("workTime", sdf2.format(nowTime));
        data.put("insuredNum", 1);
        data.put("premium", employee.getPlan() == 1 ? 900 : 1800);
        data.put("amount", employee.getPlan() == 1 ? 50000000 : 80000000);
        data.put("thirdOrderNo", orderNo);
        data.put("workType", employee.getJobCode());
        data.put("clockDate", nowTime);

        List<Map<String, Object>> insuredInfo = new ArrayList<>();
        Map<String, Object> info1 = MapUtil.newHashMap();
        info1.put("type", "1");
        info1.put("name", "深圳保到家科技有限公司");
        info1.put("identificationType", "37");
        info1.put("identificationNo", "91440300MA5GGJC224");
        insuredInfo.add(info1);
        Map<String, Object> info2 = MapUtil.newHashMap();
        info2.put("type", "2");
        info2.put("name", merchant.getName());
        info2.put("identificationType", "37");
        info2.put("identificationNo", merchant.getBusinessLicense());
        insuredInfo.add(info2);
        Map<String, Object> info3 = MapUtil.newHashMap();
        info3.put("type", "3");
        info3.put("name", employee.getName());
        info3.put("identificationType", "01");
        info3.put("identificationNo", employee.getIdCard());
        insuredInfo.add(info3);

        data.put("insuredInfo", insuredInfo);

        requestData.add(data);

        requestBody.put("requestData", requestData);

        String str = JSONUtil.toJsonStr(requestBody);
        System.out.println(str);

        // 调用第三方接口操作
        String bodyParam = CryptoUtil.LinuxEncrypt(JSONUtil.toJsonStr(requestBody), encryptionKey);
        String requestUrl = "http://39.103.160.140:9001/employer/open/gbbInsure/underwriting?signature=" + signature
                + "&nonce=" + nonce + "&timestamp=" + timestamp + "&projectCode=" + projectCode;

        HttpRequest httpRequest = HttpRequest.post(requestUrl);
        httpRequest.contentType("application/json");
        httpRequest.body(bodyParam);
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
                String respResult = CryptoUtil.LinuxDecrypt(respBody, encryptionKey);

                InsDayRecord insDayRecord = new InsDayRecord()
                        .setMerId(merchant.getId())
                        .setEmpId(employee.getId())
                        .setSnCode(snCode)
                        .setName(employee.getName())
                        .setIdCard(employee.getIdCard())
                        .setPhone(employee.getPhone())
                        .setAddress(employee.getAddress())
                        .setJobCode(employee.getJobCode())
                        .setPlan(employee.getPlan())
                        .setStartTime(nowTime)
                        .setEndTime(endTime)
                        .setResponseMsg(respResult)
                        .setCreateTime(nowTime);
                punchAttendMapper.addInsDayRecord(insDayRecord);
            }
        }
    }

    @Override
    public List<PunchCollectVO> getPunchCollect(String cloudId, String yearMonth) {
        // 0.获取商户管理信息
        MerchantManage merchant = punchAttendMapper.getMerchantByCloudId(cloudId);

        // 1.获取雇员列表
        List<PunchCollectVO> dataList = punchAttendMapper.getPunchEmpList(merchant.getId());

        // 2.遍历雇员列表
        if (dataList != null && dataList.size() > 0) {
            for (PunchCollectVO vo : dataList) {
                // 3.获取考勤天数
                Integer allDays = punchAttendMapper.getAllDays(merchant.getId(), vo.getId(), yearMonth);
                vo.setAllDays(allDays);
                // 4.获取考勤迟到天数
                Integer lateDays = punchAttendMapper.getLateDays(merchant.getId(), vo.getId(), yearMonth);
                vo.setLateDays(lateDays);
                // 5.获取考勤早退天数
                Integer leaveEarlyDays = punchAttendMapper.getLeaveEarlyDays(merchant.getId(), vo.getId(), yearMonth);
                vo.setLeaveEarlyDays(leaveEarlyDays);
                // 6.获取保险天数
                Integer insureDays = punchAttendMapper.getInsureDays(merchant.getId(), vo.getId(), yearMonth);
                vo.setInsureDays(insureDays);
            }
        }
        return dataList;
    }

    @Override
    public List<PunchDetailVO> getPunchDetail(String cloudId, Integer empId, String yearMonth) {
        // 最终返回的结果集
        List<PunchDetailVO> punchDetailVOList = new ArrayList<>();

        // 0.获取商户管理信息
        MerchantManage merchant = punchAttendMapper.getMerchantByCloudId(cloudId);

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
        List<Date> existDateList = punchAttendMapper.getExistPunchDate(merchant.getId(), empId, yearMonth);
        for (Date day : daysList) {
            PunchDetailVO punchDetailVO = new PunchDetailVO();
            punchDetailVO.setYearMonthDay(day);
            if (existDateList.contains(day)) {
                List<PunchArrayVO> arrayList = punchAttendMapper.getPunchArray(merchant.getId(), empId, day);
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
