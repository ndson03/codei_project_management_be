package dpp.codei_project_management_be.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public final class ProjectFieldCodec {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ProjectFieldCodec() {
    }

    public static String encodeStrings(List<String> values) {
        return write(values == null ? List.of() : values);
    }

    public static List<String> decodeStrings(String payload) {
        if (payload == null || payload.isBlank()) {
            return new ArrayList<>();
        }
        return read(payload, new TypeReference<List<String>>() {});
    }

    private static String write(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to encode project field", ex);
        }
    }

    private static <T> T read(String payload, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(payload, typeReference);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to decode project field", ex);
        }
    }
}
