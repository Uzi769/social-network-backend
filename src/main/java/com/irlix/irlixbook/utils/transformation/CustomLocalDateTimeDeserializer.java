package com.irlix.irlixbook.utils.transformation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public CustomLocalDateTimeDeserializer() {
        this((Class<?>)null);
    }

    protected CustomLocalDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    protected CustomLocalDateTimeDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected CustomLocalDateTimeDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException {

        String date = jsonparser.getText();
        return LocalDateTime.parse(date, formatter);

    }

}
