package com.zgj.mps.service;

import com.zgj.mps.controller.request.SaveTemplateRequest;
import com.zgj.mps.generator.base.BaseService;
import com.zgj.mps.model.Template;

import java.util.Map;

/**
 * 模板接口
 *
 * @author Wangj
 */
public interface TemplateService extends BaseService<Template, Long> {

    Map<String, Object> templateList(Long userId, String name, Short screen, Integer page, Integer size, String sortOrder, String sortValue);

    Object saveTemplate(Long userId, SaveTemplateRequest saveTemplateRequest);

    Object delTemplate(Long userId, Long templateId);
}