package com.secfix.observedrepo.domain.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PageCursorUtilsTest {

    @Test
    public void testEncodePageCursor() {
        Instant instant = Instant.parse("2024-07-11T12:00:00Z");
        PageCursorUtils pageCursorUtils = new PageCursorUtils();
        String encodedCursor = pageCursorUtils.encodePageCursor(instant);
        byte[] expectedBytes = Base64.getEncoder().encode("2024-07-11T12:00:00Z".getBytes(StandardCharsets.UTF_8));
        String expectedEncodedCursor = new String(expectedBytes, StandardCharsets.UTF_8);
        assertEquals(expectedEncodedCursor, encodedCursor);
    }

    @Test
    public void testDecodePageCursor() {
        String encodedCursor = "MjAyNC0wNy0xMVQxMjowMDowMFo=";
        Instant expectedInstant = Instant.parse("2024-07-11T12:00:00Z");
        PageCursorUtils pageCursorUtils = new PageCursorUtils();
        Instant decodedInstant = pageCursorUtils.decodePageCursor(encodedCursor);
        assertCloseEnough(expectedInstant, decodedInstant, Duration.ofSeconds(5));
    }

    @Test
    public void testDecodePageCursor_NullCursor() {
        String encodedCursor = null;
        Instant nowInstant = Instant.now();
        PageCursorUtils pageCursorUtils = new PageCursorUtils();
        Instant decodedInstant = pageCursorUtils.decodePageCursor(encodedCursor);
        assertCloseEnough(nowInstant, decodedInstant, Duration.ofSeconds(5));
    }

    private void assertCloseEnough(Instant expected, Instant actual, Duration tolerance) {
        long expectedEpochMillis = expected.toEpochMilli();
        long actualEpochMillis = actual.toEpochMilli();
        long toleranceMillis = tolerance.toMillis();
        assertEquals(expectedEpochMillis, actualEpochMillis, toleranceMillis, "Instants are not close enough");
    }
}