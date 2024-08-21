package com.secfix.observedrepo.domain.util;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class PageCursorUtils {

    public Instant decodePageCursor(String cursor) {
        if (cursor == null || cursor.isEmpty()) {
            return Instant.now();
        }
        byte[] decodedBytes = Base64.getDecoder().decode(cursor);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        return Instant.parse(decodedString);
    }

    public String encodePageCursor(Instant instant) {
        String instantString = instant.toString();
        byte[] encodedBytes = Base64.getEncoder().encode(instantString.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }
}
