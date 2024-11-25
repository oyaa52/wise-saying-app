package com.ll.domain.controller;

import com.ll.domain.entity.WiseSaying;
import com.ll.WiseSayingRepository;
import com.ll.WiseSayingService;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final WiseSayingService service = new WiseSayingService();

    public void handleHistory() {
        service.checkHistory();
    }

    public void saveLastId() {
        service.updateLastId();
    }

    public void handleCommand(String command) {
        Scanner scanner = new Scanner(System.in);
        String id = String.valueOf(command.charAt(command.length() - 1));
        if (command.equals("등록")) {
            System.out.print("명언: ");
            String quotes = scanner.nextLine();
            System.out.print("작가: ");
            String author = scanner.nextLine();
            WiseSaying wiseSaying = service.createWiseSaying(quotes, author);
            System.out.println(wiseSaying.getId() + "번 명언이 등록되었습니다.");
        } else if (command.equals("목록")) {
            System.out.println("번호 | 작가 | 명언");
            System.out.println("--------------------");
            for (WiseSaying wiseSaying : service.getWiseSayings().reversed()) {
                System.out.println(wiseSaying.getId() + " | " + wiseSaying.getQuotes() + " | " + wiseSaying.getAuthor());
            }
        } else if (command.contains("삭제?id=")) {
            if (service.deleteWiseSaying(id)) {
                System.out.println(id + "번 명언이 삭제되었습니다.");
            } else {
                System.out.println(id + "번 명언은 존재하지 않습니다.");
            }

        } else if (command.contains("수정?id=")) {
            if (service.getWiseSayingById(id) == null) {
                System.out.println(id + "번 명언은 존재하지 않습니다.");
            } else {
                WiseSaying listById = service.getWiseSayingById(id);
                System.out.println("명언(기존): " + listById.getQuotes());
                System.out.print("명언(수정): ");
                String newQuotes = scanner.nextLine();
                System.out.println("작가(기존): " + listById.getAuthor());
                System.out.print("작가(수정): ");
                String newAuthor = scanner.nextLine();
                service.modifyWiseSaying(id, newQuotes, newAuthor);
                System.out.println(id + "번 명언이 수정되었습니다.");
            }
        } else if (command.equals("종료")) {
            saveLastId();
            scanner.close();
        } else if (command.equals("빌드")) {
            service.buildData();
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        }
    }


}
