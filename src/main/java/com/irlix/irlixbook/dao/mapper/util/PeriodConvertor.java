package com.irlix.irlixbook.dao.mapper.util;

import com.irlix.irlixbook.dao.entity.enams.PeriodTypeEnum;
import com.irlix.irlixbook.exception.ConflictException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;

@Log4j2
public class PeriodConvertor implements Converter<String, PeriodTypeEnum>{

    @Override
    public PeriodTypeEnum convert(String s) {

        try {
            return PeriodTypeEnum.valueOf(s.toUpperCase());
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ConflictException("Error conversion!");
        }
    }

}
