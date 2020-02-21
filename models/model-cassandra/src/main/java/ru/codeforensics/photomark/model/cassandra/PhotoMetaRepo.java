package ru.codeforensics.photomark.model.cassandra;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoMetaRepo extends CassandraRepository<PhotoMeta, String> {

}
