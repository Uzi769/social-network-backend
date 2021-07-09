package com.irlix.irlixbook.dao.model.content.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentSearchRequest {

    private String name;
    private String type;
}
