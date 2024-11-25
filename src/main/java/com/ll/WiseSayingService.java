package com.ll;

import com.ll.domain.entity.WiseSaying;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WiseSayingService {
    private final WiseSayingRepository repository = new WiseSayingRepository();

    public void checkHistory() {
        int lastId = WiseSayingRepository.loadLastId();
        if (lastId > 0) {
            System.out.println("프로그램 다시 시작...");
            List<WiseSaying> loadedWiseSayings = WiseSayingRepository.loadWiseSayings(lastId);
            repository.setList(loadedWiseSayings);
            repository.setTotalData(lastId);
        }
    }

    public WiseSaying createWiseSaying(String quotes, String author) {
        WiseSaying wiseSaying = new WiseSaying(quotes, author);
        repository.save(wiseSaying);
        return wiseSaying;
    }

    public List<WiseSaying> getWiseSayings() {
        List<WiseSaying> list = repository.getList();
        return list;
    }

    public boolean deleteWiseSaying(String id) {
        return repository.delete(id);
    }

    public WiseSaying getWiseSayingById(String id) {
        List<WiseSaying> list = repository.getList();
        Optional<WiseSaying> opList = list.stream()
                .filter(e -> e.getId() == Integer.parseInt(id))
                .findFirst();

        return opList.orElse(null);
    }

    public void modifyWiseSaying(String id, String newQuotes, String newAuthor) {
        List<WiseSaying> list = repository.getList();
        Optional<WiseSaying> opList = list.stream()
                .filter(e -> e.getId() == Integer.parseInt(id))
                .findFirst();
        opList.ifPresent(wiseSaying -> {
            if (newQuotes != null && !newQuotes.isEmpty()) {
                wiseSaying.setQuotes(newQuotes);
            }
            if (newAuthor != null && !newAuthor.isEmpty()) {
                wiseSaying.setAuthor(newAuthor);
            }
            repository.updateWiseSayingFile(wiseSaying);
        });
    }

    public void updateLastId() {
        repository.updateLastId();
    }

    public void buildData() {
        repository.buildDataJsonFile();
    }
}
