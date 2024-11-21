package com.ll;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class App {
    int totalData = 0;
    ArrayList<String[]> list = new ArrayList<>();
    File file;

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
        checkHistory();
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
                fileUtil(data.data, "json");
            } else if (text.equals("목록")) {
                System.out.println("번호 | 작가 | 명언");
                System.out.println("--------------------");
                for (String[] value : list) {
                    System.out.println(value[0] + " | " + value[2] + " | " + value[1]);
                }
            } else if (text.contains("삭제?id=")) {
                boolean isFound = false;
                for (String[] value : list) {
                    if (index.equals(value[0])) {
                        list.remove(Integer.parseInt(index) - 1);
                        System.out.println(index + "번 명령이 삭제되었습니다.");
                        deleteFile(index);
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
                        fileUtil(value, "json");
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    System.out.println(index + "번 명언은 존재하지 않습니다.");
                }
            } else if (text.equals("종료")) {
                if (!list.isEmpty()) {
                    String[] lastData = list.get(list.size() - 1);
                    fileUtil(lastData, "txt");
                } else {
                    scanner.close();
                }
            }
        }
        while (!text.equals("종료"));
    }

    String fileNameFormat(String[] data, String fileType) {
        if (fileType.equals("json")) {
            return "db/wiseSaying/" + data[0] + ".json";
        } else if (fileType.equals("txt")) {
            return "db/wiseSaying/lastId.txt";
        } else return "";
    }

    String inputFormat(String[] data, String fileType) {
        if (fileType.equals("json")) {
            return "{\n\"id\": " + data[0] + ",\n" +
                    "\"content\": \"" + data[1] + "\",\n" +
                    "\"author\": \"" + data[2] + "\"\n}";
        } else if (fileType.equals("txt")) {
            return String.valueOf(totalData);
        } else return "";
    }

    void checkHistory() {
        String filePath = "db/wiseSaying/lastId.txt";
        File fileLastId = new File(filePath);
        if (!fileLastId.exists()) return;

        try {
            FileReader fileReader = new FileReader(fileLastId);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                int lastId = Integer.parseInt(bufferedReader.readLine().trim());
                if (lastId > 0) {
                    System.out.println("프로그램 다시 시작...");
                    System.out.println("마지막 id: " + lastId);
                    File directory = new File("db/wiseSaying");
                    if (directory.exists() && directory.isDirectory()) {
                        File[] files = directory.listFiles();
                        if (files != null && files.length > 0) {
                            for (File file : files) {
                                if (file.isFile() && !file.getName().equals("lastId.txt")) {
                                    try {
                                        // TODO 이 부분은 ChatGPT 사용하고 이해 완전히 못했으므로 복습할 것
                                        String[] lines = Files.readAllLines(Paths.get(file.getPath())).toArray(new String[0]);

                                        String id = lines[1].split(":")[1].trim().replace(",", "").replace("\"", "");
                                        String content = lines[2].split(":")[1].trim().replace(",", "").replace("\"", "");
                                        String author = lines[3].split(":")[1].trim().replace(",", "").replace("\"", "");

                                        list.add(new String[]{id, content, author});
                                        totalData = lastId;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    void fileUtil(String[] data, String fileType) {
        String fileName = fileNameFormat(data, fileType);
        try {
            File file = new File(fileName);
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            String input = inputFormat(data, fileType);
            writer.write(input);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void deleteFile(String index) {
        File directory = new File("db/wiseSaying");
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.getName().contains(index)) {
                file.delete();
            }
        }
    }
}