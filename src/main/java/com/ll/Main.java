package com.ll;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class App {
    int totalData = 0;
    ArrayList<String[]> list = new ArrayList<>();

    public void run() {
        class Data {
            String[] data = new String[3];
            String quotes;
            String name;

            public Data(String quotes, String name) {
                this.quotes = quotes;
                this.name = name;
                totalData++;
            }

            public String[] save(String quotes, String name) {
                data[0] = String.valueOf(totalData);
                data[1] = quotes;
                data[2] = name;
                list.add(data);
                return data;
            }

            public void saveMessage() {
                System.out.println(data[0] + "번 명언이 등록되었습니다.");
            }
        }

        Scanner scanner = new Scanner(System.in);
        String text;
        System.out.println("== 명언 앱 ==");
        do {
            System.out.print("명령) ");
            text = scanner.nextLine();
            String index = String.valueOf(text.charAt(text.length() - 1));
            if (text.equals("등록")) {
                System.out.print("명언: ");
                String quotes = scanner.nextLine();
                System.out.print("작가: ");
                String name = scanner.nextLine();
                Data data = new Data(quotes, name);
                data.save(quotes, name);
                data.saveMessage();
            } else if (text.equals("목록")) {
                for (String[] value : list) {
                    System.out.println(value[0] + " | " + value[2] + " | " + value[1]);
                }
            } else if (text.contains("삭제?id=")) {
                boolean isFound = false;
                for (String[] value : list) {
                    if (index.equals(value[0])) {
                        list.remove(Integer.parseInt(index) - 1);
                        System.out.println(index + "번 명령이 삭제되었습니다.");
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    System.out.println(index + "번 명언은 존재하지 않습니다.");
                }
            } else if (text.contains("수정?id=")) {
                boolean isFound = false;
                for (String[] value : list) {
                    if (index.equals(value[0])) {
                        System.out.println("명언(기존): " + value[1]);
                        System.out.println("명언(수정): ");
                        String newQuotes = scanner.nextLine();
                        System.out.println("작가(기존): " + value[2]);
                        System.out.println("작가(수정): ");
                        String newName = scanner.nextLine();
                        if (!newQuotes.isEmpty() && !newQuotes.equals(value[1])) {
                            value[1] = newQuotes;
                        }
                        if (!newName.isEmpty() && !newName.equals(value[2])) {
                            value[2] = newName;
                        }
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    System.out.println(index + "번 명언은 존재하지 않습니다.");
                }
            }
        }
        while (!text.equals("종료"));
    }
}
