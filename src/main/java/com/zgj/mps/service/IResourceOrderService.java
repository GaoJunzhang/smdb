package com.zgj.mps.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zgj.mps.bean.OrderResourceBean;
import com.zgj.mps.model.ResourceOrder;

import java.util.List;

/**
 * 资源标签接口
 *
 * @author GaoJunZhang
 */
public interface IResourceOrderService extends IService<ResourceOrder> {
    int batchSave(List<ResourceOrder> list);

    List<OrderResourceBean> orders(OrderResourceBean orderResourceBean);

    int deleteByTypeResourceId(String typeResourceId);

    Page<OrderResourceBean> selectOrderPage(int pageNo, int pageSize, OrderResourceBean model);

    int updateIsforever(List<ResourceOrder> list);

    int updateStatus(List<ResourceOrder> list);

    List<ResourceOrder> findByOrder(ResourceOrder resourceOrder);
}