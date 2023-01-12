package com.study.crm.settings.service.impl;

import com.study.crm.settings.domain.DicValue;
import com.study.crm.settings.mapper.DicValueMapper;
import com.study.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
