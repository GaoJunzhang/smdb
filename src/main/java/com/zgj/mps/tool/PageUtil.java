package com.zgj.mps.tool;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zgj.mps.vo.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


/**
* class_name: PageUtil
* package: com.garry.zboot.common.vo
* describe: 分页
* creat_user: ZhangGaoJun@zhanggj@seeyoo.cn
* creat_date: 2019/7/9
* creat_time: 9:31
**/
public class PageUtil {

    /**
     * JPA分页
     * @param page
     * @return
     */
    public static Pageable initPage(PageVo page){

        Pageable pageable=null;
        int pageNumber=page.getPageNo();
        int pageSize=page.getPageSize();
        String sort=page.getSortField();
        String order=page.getSortOrder();
        if (StrUtil.isNotEmpty(order)){
            order = order.replace("end","");
        }

        if(pageNumber<1){
            pageNumber=1;
        }
        if(pageSize<1){
            pageSize=10;
        }
        if(StrUtil.isNotBlank(sort)) {
            Sort.Direction d;
            if(StrUtil.isBlank(order)) {
                d = Sort.Direction.DESC;
            }else {
                d = Sort.Direction.valueOf(order.toUpperCase());
            }
            Sort s = new Sort(d,sort);
            pageable = PageRequest.of(pageNumber-1, pageSize,s);
        }else {
            pageable = PageRequest.of(pageNumber-1, pageSize);
        }
        return pageable;
    }

    /**
     * Mybatis-Plus分页封装
     * @param page
     * @return
     */
    public static Page initMpPage(PageVo page){

        Page p = null;
        int pageNumber = page.getPageNo();
        int pageSize = page.getPageSize();
        String sort = page.getSortField();
        String order = page.getSortOrder();

        if(pageNumber<1){
            pageNumber = 1;
        }
        if(pageSize<1){
            pageSize = 10;
        }
        if(StrUtil.isNotBlank(sort)) {
            Boolean isAsc = false;
            if(StrUtil.isBlank(order)) {
                isAsc = false;
            } else {
                if("desc".equals(order.toLowerCase())){
                    isAsc = false;
                } else if("asc".equals(order.toLowerCase())){
                    isAsc = true;
                }
            }
            p = new Page(pageNumber, pageSize);
            if(isAsc){
                p.setAsc(sort);
            } else {
                p.setDesc(sort);
            }
        } else {
            p = new Page(pageNumber, pageSize);
        }
        return p;
    }

    /**
     * List 分页
     * @param page
     * @param list
     * @return
     */
    public static List listToPage(PageVo page, List list) {

        int pageNumber = page.getPageNo() - 1;
        int pageSize = page.getPageSize();

        if(pageNumber<0){
            pageNumber = 0;
        }
        if(pageSize<1){
            pageSize = 10;
        }

        int fromIndex = pageNumber * pageSize;
        int toIndex = pageNumber * pageSize + pageSize;

        if(fromIndex > list.size()){
            return new ArrayList();
        } else if(toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }
}
