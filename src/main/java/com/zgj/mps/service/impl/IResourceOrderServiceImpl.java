package com.zgj.mps.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgj.mps.bean.OrderResourceBean;
import com.zgj.mps.dao.mapper.OrderMapper;
import com.zgj.mps.model.ResourceOrder;
import com.zgj.mps.service.IResourceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 资源标签接口实现
 * @author GaoJunZhang
 */
@Slf4j
@Service
@Transactional
public class IResourceOrderServiceImpl extends ServiceImpl<OrderMapper, ResourceOrder> implements IResourceOrderService {

    @Autowired
    private OrderMapper orderMapper;

    public int batchSave(List<ResourceOrder> list){
        return orderMapper.batchSave(list);
    }

    public List<OrderResourceBean> orders(OrderResourceBean orderResourceBean){
        return orderMapper.orders(orderResourceBean);
    }

    public int deleteByTypeResourceId(String typeResourceId){
        QueryWrapper<ResourceOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_resource_id",typeResourceId);
        return orderMapper.delete(queryWrapper);
    }

    public Page<OrderResourceBean> selectOrderPage(int pageNo, int pageSize, OrderResourceBean orderResourceBean){
        Page<OrderResourceBean> page = new Page<OrderResourceBean>(pageNo, pageSize);
        return page.setRecords(this.baseMapper.selectOrderPage(page, orderResourceBean));
    }

    public int updateIsforever(List<ResourceOrder> list){
        return orderMapper.updateIsforever(list);
    }

    public int updateStatus(List<ResourceOrder> list){
        return orderMapper.updateStatus(list);
    }

    public List<ResourceOrder> findByOrder(ResourceOrder resourceOrder){
        QueryWrapper<ResourceOrder> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(resourceOrder.getDeviceId())){
            queryWrapper.eq("device_id",resourceOrder.getDeviceId());
        }
        if (StrUtil.isNotBlank(resourceOrder.getResourceId())){
            queryWrapper.eq("resource_id",resourceOrder.getResourceId());
        }
        if (StrUtil.isNotBlank(resourceOrder.getTypeId())){
            queryWrapper.eq("type_id",resourceOrder.getTypeId());
        }
        if (StrUtil.isNotBlank(resourceOrder.getDeviceMac())){
            queryWrapper.eq("device_mac",resourceOrder.getDeviceMac());
        }
        queryWrapper.eq("is_delete",0);
        return orderMapper.selectList(queryWrapper);
    }
}