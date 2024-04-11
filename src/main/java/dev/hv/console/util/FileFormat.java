package dev.hv.console.util;

import lombok.Getter;

public enum FileFormat {
    CSV ("-c", ".csv"),
    XML ("-x", ".xml"),
    JSON ("-j", ".json"),
    TXT ("-t", ".txt");

    @Getter
    private String flag;
    @Getter
    private String extension;
    FileFormat (String flag, String extension) {
        this.flag = flag;
        this.extension = extension;
    }
}
