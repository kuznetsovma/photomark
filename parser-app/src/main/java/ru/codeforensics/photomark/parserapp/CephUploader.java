package ru.codeforensics.photomark.parserapp;

public interface CephUploader {

  /**
   * Загрузка содержимого файла в ceph хранилище
   *
   * @param data - данные
   * @return - идентификатор файла в хранилище (может быть url)
   */
  String upload(byte[] data);
}
