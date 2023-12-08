package ru.spb.nicetu.utils;

public interface IJSONProvider {
    Object parse(Class t, IFileService json);
    default String stringify(Object t) { return t.toString(); }
}
