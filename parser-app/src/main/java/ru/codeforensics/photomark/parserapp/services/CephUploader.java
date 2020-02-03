package ru.codeforensics.photomark.parserapp.services;

public interface CephUploader {

  /**
   * Загрузка содержимого файла в ceph хранилище
   *
   * @param key - идентификатор файла в хранилище
   * @param data - данные
   */
  void upload(String key, byte[] data);
}
