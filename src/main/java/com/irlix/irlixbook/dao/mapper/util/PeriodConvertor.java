package com.irlix.irlixbook.dao.mapper.util;

import com.irlix.irlixbook.dao.entity.enams.PeriodType;
import org.springframework.core.convert.converter.Converter;

public class PeriodConvertor implements Converter<String, PeriodType>{

    @Override
    public PeriodType convert(String s) {
        try {
            return PeriodType.valueOf(s.toUpperCase());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
