package com.zgj.mps.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.bean.OrderResourceBean;
import com.zgj.mps.model.ResourceOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源标签数据处理层
 *
 * @author GaoJunZhang
 */
public interface OrderMapper extends BaseMapper<ResourceOrder> {

    int batchSave(@Param("list") List<ResourceOrder> list);

    List<OrderResourceBean> orders(@Param("order") OrderResourceBean order);

    List<OrderResourceBean> selectOrderPage(Page page, @Param(value = "order") OrderResourceBean order);

    int updateIsforever(@Param("list") List<ResourceOrder> list);

    int updateStatus(@Param("list") List<ResourceOrder> list);
}