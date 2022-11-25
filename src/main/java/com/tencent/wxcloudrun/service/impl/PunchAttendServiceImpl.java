package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.PunchAttendMapper;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.PunchAttendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
