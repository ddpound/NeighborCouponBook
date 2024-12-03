package com.neighborcouponbook.common.response;

import lombok.Getter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Path;

@Getter
public class ZipResource {
    private final Path zipFile;
    private final String filename;

    public ZipResource(Path zipFile, String filename) {
        this.zipFile = zipFile;
        this.filename = filename;
    }

    public Resource getResource() throws IOException {
        return new UrlResource(zipFile.toUri());
    }
}
