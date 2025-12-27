package dev.baecher.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoundaryInputStreamTest {
    static class TestCase {
        public String input;
        public String boundary;
        public String expectedData;
        public int bufferSize;

        public TestCase(String input, String boundary, String expectedData, int bufferSize) {
            this.input = input;
            this.boundary = boundary;
            this.expectedData = expectedData;
            this.bufferSize = bufferSize;
        }
    }

    @Test
    void testVariousCases() throws IOException {
        var testCases = List.of(
                new TestCase("", "ab", "", 2),
                new TestCase("a", "ab", "a", 2),
                new TestCase("a", "a", "", 1),
                new TestCase("aa", "ab", "aa", 2),
                new TestCase("aab", "ab", "a", 2),
                new TestCase("ab", "ab", "", 2),
                new TestCase(".ab", "ab", ".", 2),
                new TestCase("..ab", "ab", "..", 2),
                new TestCase("...ab", "ab", "...", 2),
                new TestCase("....ab", "ab", "....", 2)
        );

        testCases.forEach(testCase -> {
            try (var bis = BoundaryInputStream
                    .builder(new ByteArrayInputStream(testCase.input.getBytes()))
                    .boundary(testCase.boundary.getBytes())
                    .bufferSize(testCase.bufferSize)
                    .build()) {
                var data = new String(bis.readAllBytes());

                assertEquals(testCase.expectedData, data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    void testBoundaryMutation() throws IOException {
        for (int bufferSize = 1; bufferSize <= 3; bufferSize++) {
            var input = new ByteArrayInputStream("...a..b.".getBytes());

            try (var bis = BoundaryInputStream.builder(input).bufferSize(bufferSize).build()) {

                bis.setBoundary("a".getBytes());
                assertFalse(bis.atBoundary());

                assertEquals("...", new String(bis.readAllBytes()));
                assertTrue(bis.atBoundary());

                bis.clearBoundary();
                assertFalse(bis.atBoundary());

                bis.setBoundary("a".getBytes());
                assertTrue(bis.atBoundary());

                bis.clearBoundary();
                assertFalse(bis.atBoundary());

                bis.setBoundary("b".getBytes());
                assertEquals("a..", new String(bis.readAllBytes()));
                assertTrue(bis.atBoundary());

                bis.clearBoundary();
                assertEquals("b.", new String(bis.readAllBytes()));
                assertFalse(bis.atBoundary());
            }
        }
    }
}
