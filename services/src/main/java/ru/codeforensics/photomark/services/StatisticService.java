package ru.codeforensics.photomark.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.model.entities.StatData;
import ru.codeforensics.photomark.model.entities.StatDataId;
import ru.codeforensics.photomark.model.repo.StatDataRepo;

@Service
public class StatisticService {

  private static final Logger logger = LoggerFactory.getLogger(StatisticService.class);

  private final Map<StatDataId, Integer> cash = new ConcurrentHashMap<>();

  @Autowired
  private StatDataRepo statDataRepo;

  public void registerEvent(Client client, String line) {
    StatDataId statDataId = new StatDataId(client.getId(), LocalDate.now(), line);
    cash.putIfAbsent(statDataId, 0);
    cash.computeIfPresent(statDataId, (key, value) -> value + 1);
  }

  @Scheduled(fixedDelay = 10_000)
  public void flushCashToBase() {
    Map<StatDataId, Integer> tmpCash;
    synchronized (cash) {
      tmpCash = new HashMap<>(cash);
      cash.clear();
    }

    for (Entry<StatDataId, Integer> entry : tmpCash.entrySet()) {
      StatDataId statDataId = entry.getKey();
      Integer count = entry.getValue();

      updateStatData(statDataId, count, tmpCash);
    }
  }

  private void updateStatData(StatDataId statDataId, Integer count,
      Map<StatDataId, Integer> tmpCash) {
    StatData statData = getStatData(statDataId);
    statData.addCount(count);
    try {
      statDataRepo.save(statData);
      tmpCash.remove(statDataId);
    } catch (OptimisticLockException e) {
      logger.warn("updateStatData: {}", e.getMessage());
      updateStatData(statDataId, count, tmpCash);
    }
  }

  private StatData getStatData(StatDataId statDataId) {
    Optional<StatData> statDataOptional = statDataRepo.findById(statDataId);
    StatData statData;
    if (statDataOptional.isPresent()) {
      return statDataOptional.get();
    } else {
      statData = new StatData();
      statData.setId(statDataId);
      return statData;
    }
  }

}
