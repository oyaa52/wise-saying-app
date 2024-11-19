package com.ll;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static int totalData = 0;
    static ArrayList<String[]> list = new ArrayList<>();
    public static class Data {
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text;
        System.out.println("== 명언 앱 ==");
        do {
            System.out.print("명령) ");
            text = scanner.nextLine();
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
            }
        } while (!text.equals("종료"));
    }
}
