package com.ll;

import com.ll.domain.controller.WiseSayingController;

import java.util.Scanner;

public class App {
    private final Scanner scanner;
    private final WiseSayingController controller;
    String command;

    public App() {
        scanner = new Scanner(System.in);
        controller = new WiseSayingController();
    }
    public void run() {
        controller.handleHistory();
        System.out.println("== 명언 앱 ==");
        do {
            System.out.print("명령) ");
            command = scanner.nextLine().trim();

            if (command.equals("종료")) {
                controller.saveLastId();
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            controller.handleCommand(command);
        } while (true);
    }
}

//public class App {
//    private final Scanner scanner;
//    private int totalData;
//    private final List<WiseSaying> list;
//
//    public App() {
//        scanner = new Scanner(System.in);
//        totalData = 0;
//        list = new ArrayList<>();
//    }
//
//    public void run() {
//        checkHistory();
//
//        String text;
//        System.out.println("== 명언 앱 ==");
//
//        do {
//            System.out.print("명령) ");
//            text = scanner.nextLine();
//            String index = String.valueOf(text.charAt(text.length() - 1));
//            if (text.equals("등록")) {
//                actionAdd();
//            } else if (text.equals("목록")) {
//                actionList();
//            } else if (text.contains("삭제?id=")) {
//                actionDelete(index);
//            } else if (text.contains("수정?id=")) {
//                actionModify(index);
//            } else if (text.equals("종료")) {
//                if (!list.isEmpty()) {
//                    WiseSayingRepository.updateLastId(totalData);
//                } else {
//                    scanner.close();
//                }
//            } else if (text.equals("빌드")) {
//                WiseSayingRepository.buildDataJsonFile();
//            }
//        }
//        while (!text.equals("종료"));
//    }
//
//    private void save(WiseSaying wiseSaying) {
//        list.add(wiseSaying);
//        System.out.println(wiseSaying.getId() + "번 명언이 등록되었습니다.");
//    }
//
//    private void actionAdd() {
//        System.out.print("명언: ");
//        String quotes = scanner.nextLine();
//        System.out.print("작가: ");
//        String name = scanner.nextLine();
//        WiseSaying wiseSaying = new WiseSaying(++totalData, quotes, name);
//        save(wiseSaying);
//        WiseSayingRepository.updateWiseSayingFile(wiseSaying);
//    }
//
//    private void actionList() {
//        System.out.println("번호 | 작가 | 명언");
//        System.out.println("--------------------");
//        for (WiseSaying value : list.reversed()) {
//            System.out.println(value.getId() + " | " + value.getQuotes() + " | " + value.getName());
//        }
//    }
//
//    private void actionDelete(String index) {
//        boolean remove = list.removeIf(entry -> entry.getId() == Integer.parseInt(index));
//        if (remove) {
//            WiseSayingRepository.deleteFile(index);
//            System.out.println(index + "번 명언이 삭제되었습니다.");
//        } else System.out.println(index + "번 명언은 존재하지 않습니다.");
//    }
//
//    private void actionModify(String index) {
//        boolean isFound = false;
//        for (WiseSaying value : list) {
//            if (index.equals(String.valueOf(value.getId()))) {
//                System.out.println("명언(기존): " + value.getQuotes() + "\n명언(수정): ");
//                String newQuotes = scanner.nextLine();
//                System.out.println("작가(기존): " + value.getName() + "\n작가(수정): ");
//                String newName = scanner.nextLine();
//                if (!newQuotes.isEmpty() && !newQuotes.equals(value.getQuotes())) {
//                    value.setQuotes(newQuotes);
//                }
//                if (!newName.isEmpty() && !newName.equals(value.getName())) {
//                    value.setName(newName);
//                }
//                System.out.println(value.getId() + "번 명언이 수정되었습니다.");
//                WiseSayingRepository.updateWiseSayingFile(value);
//                isFound = true;
//                break;
//            }
//        }
//        if (!isFound) {
//            System.out.println(index + "번 명언은 존재하지 않습니다.");
//        }
//    }
//
//    void checkHistory() {
//        int lastId = WiseSayingRepository.loadLastId();
//        if (lastId > 0) {
//            System.out.println("프로그램 다시 시작...");
//            List<WiseSaying> loadedWiseSayings = WiseSayingRepository.loadWiseSayings(lastId);
//            list.addAll(loadedWiseSayings);
//            totalData = lastId;
//        }
//    }
//}
