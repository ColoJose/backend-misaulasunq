package com.misaulasunq.exceptions;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class InvalidFileExtensionException extends Throwable {
    public InvalidFileExtensionException(List<String> validFormats) {
        super(
            String.format(
                "La extension del archivo es invalido. Las extenciones permitidas son: %s",
                validFormats.stream().collect(joining(", ","","."))
        ));
    }
}
