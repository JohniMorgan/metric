package ru.spb.nicetu.utils;

import java.io.File;

public interface IFileService {
    void write(String content);
    boolean isExisted();
    File getTarget();
}
