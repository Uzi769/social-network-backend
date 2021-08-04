package com.irlix.irlixbook.dao.entity.enams;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PeriodType {

    @JsonProperty("day")
    DAY,

    @JsonProperty("week")
    WEEK,

    @JsonProperty("month")
    MONTH
}
