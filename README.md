# boundary-input-stream

A `java.io.InputStream`, but you can set a boundary up to which the stream is readable. When the boundary is found, the stream indicates end of stream. You can then clear the boundary (or set a different one) and continue reading.

One classic use case is parsing various variants of `multipart/form-data`, but anything that uses a boundary marker to demarcate content can be streamed efficiently with this library.


## Getting it

 * Group id: `dev.baecher.io`
 * Artifact id: `boundary-input-stream`
 * Version: `1.0.1`


## API

This library exposes a single class called `dev.baecher.io.BoundaryInputStream`.

```java
// construction of BoundaryInputStream
BoundaryInputStream.builder(sourceStream)
  .bufferSize(4711)                          // Optionally set a different buffer size
  .boundary("===myboundary123=".getBytes())  // Optionally set an initial boundary
  .build()

// usage of BoundaryInputStream
void setBoundary(byte[] boundary)
void clearBoundary()
boolean atBoundary()
```


## Release notes

### 1.0.1

 * Fixed initial release, version number was incorrect.

### 1.0.0

 * Initial public release.


## Usage example

```java
package example;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var source = new ByteArrayInputStream("here is text.[BOUNDARY]more text.".getBytes());

        try (var stream = BoundaryInputStream.builder(source).build()) {
            stream.setBoundary("[BOUNDARY]".getBytes());
            assert "here is text.".equals(new String(stream.readAllBytes()));
            assert stream.atBoundary();

            stream.clearBoundary();
            assert "[BOUNDARY]more text.".equals(new String(stream.readAllBytes()));
            assert !stream.atBoundary();
        }
    }
}
```


## Limitations

 * No thread safety.
