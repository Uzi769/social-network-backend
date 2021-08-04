package com.irlix.irlixbook.dao.model.content.enam;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PeriodType {

    @JsonProperty("day")
    DAY,

    @JsonProperty("week")
    WEEK,

    @JsonProperty("month")
    MONTH;
}
