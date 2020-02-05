package ru.codeforensics.photomark.model.repo;

import org.springframework.data.repository.CrudRepository;
import ru.codeforensics.photomark.model.entities.StatData;
import ru.codeforensics.photomark.model.entities.StatDataId;

public interface StatDataRepo extends CrudRepository<StatData, StatDataId> {

}
