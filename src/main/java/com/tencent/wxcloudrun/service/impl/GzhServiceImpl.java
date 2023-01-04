package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dao.GzhMapper;
import com.tencent.wxcloudrun.dao.PunchAttendMapper;
import com.tencent.wxcloudrun.model.*;
import com.tencent.wxcloudrun.service.GzhService;
import com.tencent.wxcloudrun.utils.OrderNoType;
import com.tencent.wxcloudrun.vo.CityAreaList;
import com.tencent.wxcloudrun.vo.EmployeeManageInfo;
import com.tencent.wxcloudrun.vo.MerchantDetailVO;
import com.tencent.wxcloudrun.vo.ProvinceCityList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: zero
 * @Data: 2022/11/8 16:00
 * @Motto: Nothing is impossible
 */
@Service
public class GzhServiceImpl implements GzhService {

    @Autowired
    private GzhMapper gzhMapper;

    @Autowired
    private PunchAttendMapper punchAttendMapper;

    @Override
    public ApiResponse getRequestHeader(HttpServletRequest request) {
        request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>(8);
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headerMap.put(name, request.getHeader(name));
        }

        return ApiResponse.ok(headerMap);
    }

    @Override
    public ApiResponse registerLogin(String cloudId) {
        // 1.根据cloudId判断是否存在该用户
        MerchantManage merchant = gzhMapper.getMerchantByCloudId(cloudId);
        if (merchant == null) {
            // 2.不存在时--执行新增操作
            MerchantManage addMerchant = new MerchantManage();
            addMerchant.setCloudId(cloudId);
            Integer result = gzhMapper.addMerchantManage(addMerchant);
            if (result > 0) {
                // 新创建的公众号同步生成打卡规则
                PunchAttendRule rule = new PunchAttendRule()
                        .setMerId(addMerchant.getId())
                        .setPunchType(1)
                        .setOneStartTime("08:30")
                        .setOneNormalTime("09:00")
                        .setOneEndTime("09:30")
                        .setTwoStartTime("17:30")
                        .setTwoNormalTime("18:00")
                        .setTwoEndTime("18:30");
                punchAttendMapper.addPunchAttendRule(rule);
            } else {
                return ApiResponse.error("提示：创建公众号账户失败！");
            }
        }
        return ApiResponse.ok();
    }

    @Override
    public List<ProvinceCityList> getAllArea() {
        // 集合：获取所有省-市-区
        List<ProvinceCityList> allArea = new ArrayList<>();

        // 1.获取所有省
        List<Province> provinceList = gzhMapper.getProvince();
        for(Province p : provinceList) {
            // 2.获取所有市
            List<City> cityList = gzhMapper.getCityByPid(p.getId());

            ProvinceCityList provinceCity = new ProvinceCityList();
            provinceCity.setId(p.getId());
            provinceCity.setName(p.getName());

            List<CityAreaList> cityAreaList = new ArrayList<>();

            for (City c : cityList) {
                // 3.获取所有区
                List<Area> areaList = gzhMapper.getAreaByPid(c.getId());

                CityAreaList cityArea = new CityAreaList();
                cityArea.setId(c.getId());
                cityArea.setName(c.getName());
                cityArea.setAreaList(areaList);

                cityAreaList.add(cityArea);
            }

            provinceCity.setCityAreaList(cityAreaList);

            allArea.add(provinceCity);
        }

        return allArea;
    }

    @Override
    public ApiResponse createOrder(Order order) {
        order.setOrderNo(OrderNoType.getOrderNoType("GBB-D", 2));
        Integer result = gzhMapper.createOrder(order);
        if (result > 0) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.error("提示：创建订单异常！");
        }
    }

    @Override
    public ApiResponse aliveDevice(String cloudId, String snCode, String name, String promotionCode) {
        // 查询该设备是否已激活
        DeviceManage device = gzhMapper.getDeviceBySnCode(snCode);
        if (device != null) {
            return ApiResponse.error("提示：该设备已激活！");
        }

        // 创建设备信息，将设备绑定至商户下面
        DeviceManage addDevice = new DeviceManage()
                .setCloudId(cloudId)
                .setSnCode(snCode)
                .setName(name)
                .setPromotionCode(promotionCode)
                .setStatus(1);
        Integer result = gzhMapper.addDeviceManage(addDevice);
        if (result > 0) {
            return ApiResponse.ok();
        }
        return ApiResponse.error("提示：设备激活异常！");
    }

    @Override
    public ApiResponse getMerchantByCloudId(String cloudId) {
        // 获取商户基本信息
        MerchantManage merchant = gzhMapper.getMerchantByCloudId(cloudId);
        // 获取设备数
        Integer deviceNum = gzhMapper.getDeviceNumByCloudId(cloudId);
        // 获取员工数
        Integer employeeNum = gzhMapper.getEmployeeNumByMerId(merchant.getId());

        // 返回实体类对象
        MerchantDetailVO detail = new MerchantDetailVO()
                .setId(merchant.getId())
                .setCloudId(merchant.getCloudId())
                .setName(merchant.getName())
                .setLinkPerson(merchant.getLinkPerson())
                .setLinkPhone(merchant.getLinkPhone())
                .setAddress(merchant.getAddress())
                .setBusinessLicense(merchant.getBusinessLicense())
                .setPlaceImage(merchant.getPlaceImage())
                .setFaceScore(merchant.getFaceScore())
                .setBalance(merchant.getBalance())
                .setCreateTime(merchant.getCreateTime())
                .setDeviceNum(deviceNum)
                .setEmployeeNum(employeeNum);

        return ApiResponse.ok(detail);
    }

    @Override
    public ApiResponse updateMerchantInfo(MerchantManage merchant) {
        gzhMapper.updateMerchantInfo(merchant);
        return ApiResponse.ok();
    }

    @Override
    public List<EmployeeManageInfo> getEmployeeList(String cloudId) {
        return gzhMapper.getEmployeeList(cloudId);
    }

    @Override
    public void closeEmpPunch(Integer merId, Integer empId, Integer isPunch) {
        gzhMapper.closeEmpPunch(merId, empId, isPunch);
    }

    @Override
    public void closeEmpInsure(Integer merId, Integer empId, Integer isInsure) {
        gzhMapper.closeEmpInsure(merId, empId, isInsure);
    }

    @Override
    public void delMerEmpRelation(String cloudId, Integer empId) {
        MerchantManage merchant = gzhMapper.getMerchantByCloudId(cloudId);
        gzhMapper.delMerEmpRelation(merchant.getId(), empId);
    }

    @Override
    public List<DeviceManage> getDeviceList(String cloudId) {
        return gzhMapper.getDeviceList(cloudId);
    }

    @Override
    public void closeDevicePunch(Integer id, Integer isPunch) {
        gzhMapper.closeDevicePunch(id, isPunch);
    }

    @Override
    public void closeDeviceInsure(Integer id, Integer isInsure) {
        gzhMapper.closeDeviceInsure(id, isInsure);
    }

    @Override
    public void delDevice(Integer id) {
        gzhMapper.delDevice(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRechargeBalance(String cloudId, BigDecimal rechargeAmount) {
        // 1.获取商户管理信息
        MerchantManage merchant = gzhMapper.getMerchantByCloudId(cloudId);
        if (merchant != null) {
            // 2.执行增加余额操作
            gzhMapper.updateBalance(merchant.getId(), rechargeAmount);
            // 3.增加充值记录
            RechargeRecord record = new RechargeRecord()
                    .setCloudId(cloudId)
                    .setRechargeAmount(rechargeAmount)
                    .setBeforeBalance(merchant.getBalance())
                    .setAfterBalance(merchant.getBalance().add(rechargeAmount));
            gzhMapper.addRechargeRecord(record);
        }
    }

    @Override
    public List<RechargeRecord> getRechargeRecordList(String cloudId) {
        return gzhMapper.getRechargeRecordList(cloudId);
    }
}
