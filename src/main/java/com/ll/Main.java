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
    List<WiseSaying> list = new ArrayList<>();
    File directory = new File("db/wiseSaying");

    public void save(WiseSaying wiseSaying) {
        list.add(wiseSaying);
        System.out.println(wiseSaying.id + "번 명언이 등록되었습니다.");
    }

    public void run() {
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
                WiseSaying wiseSaying = new WiseSaying(++totalData, quotes, name);
                save(wiseSaying);
                FileUtil.updateWiseSayingFile(wiseSaying);
            } else if (text.equals("목록")) {
                System.out.println("번호 | 작가 | 명언");
                System.out.println("--------------------");
                for (WiseSaying value : list.reversed()) {
                    System.out.println(value.id + " | " + value.quotes + " | " + value.name);
                }
            } else if (text.contains("삭제?id=")) {
                boolean isFound = false;
                for (WiseSaying value : list) {
                    if (index.equals(String.valueOf(value.id))) {
                        list.remove(Integer.parseInt(index) - 1);
                        System.out.println(index + "번 명령이 삭제되었습니다.");
                        FileUtil.deleteFile(index);
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    System.out.println(index + "번 명언은 존재하지 않습니다.");
                }
            } else if (text.contains("수정?id=")) {
                boolean isFound = false;
                for (WiseSaying value : list) {
                    if (index.equals(String.valueOf(value.id))) {
                        System.out.println("명언(기존): " + value.quotes + "\n명언(수정): ");
                        String newQuotes = scanner.nextLine();
                        System.out.println("작가(기존): " + value.name + "\n작가(수정): ");
                        String newName = scanner.nextLine();
                        if (!newQuotes.isEmpty() && !newQuotes.equals(value.quotes)) {
                            value.quotes = newQuotes;
                        }
                        if (!newName.isEmpty() && !newName.equals(value.name)) {
                            value.name = newName;
                        }
                        System.out.println(value.id + "번 명언이 수정되었습니다.");
                        FileUtil.updateWiseSayingFile(value);
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    System.out.println(index + "번 명언은 존재하지 않습니다.");
                }
            } else if (text.equals("종료")) {
                if (!list.isEmpty()) {
                    FileUtil.updateLastId(totalData);
                } else {
                    scanner.close();
                }
            } else if (text.equals("빌드")) {
                FileUtil.buildDataJsonFile();
            }
        }
        while (!text.equals("종료"));
    }

    void checkHistory() {
        int lastId = FileUtil.loadLastId();
        if (lastId > 0) {
            System.out.println("프로그램 다시 시작...");
            List<WiseSaying> loadedWiseSayings = FileUtil.loadWiseSayings(lastId);
            list.addAll(loadedWiseSayings);
            totalData = lastId;
        }
    }
}

class WiseSaying {
    int id;
    String quotes;
    String name;

    public WiseSaying(int id, String quotes, String author) {
        this.id = id;
        this.quotes = quotes;
        this.name = author;
    }
}

class FileUtil {
    private static final String BASE_DIRECTORY = "db/wiseSaying";

    public static void createDirectory() {
        File directory = new File(BASE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static String createFileName(WiseSaying wiseSaying) {
        return BASE_DIRECTORY + "/" + wiseSaying.id + ".json";
    }

    public static String wiseSayingToJson(WiseSaying wiseSaying) {
        return "{\n\t\"id\": " + wiseSaying.id + ",\n" +
                "\t\"content\": \"" + wiseSaying.quotes + "\",\n" +
                "\t\"author\": \"" + wiseSaying.name + "\"\n}";
    }

    public static void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    public static void readFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
        }
    }

    public static String loadHistory() {
        String filePath = BASE_DIRECTORY + "/lastId.txt";
        try {
            return Files.readString(Paths.get(filePath)).trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static void updateWiseSayingFile(WiseSaying wiseSaying) {
        String fileName = createFileName(wiseSaying);
        String content = wiseSayingToJson(wiseSaying);
        try {
            writeToFile(fileName, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLastId(int totalData) {
        String filePath = BASE_DIRECTORY + "/lastId.txt";
        try {
            writeToFile(filePath, String.valueOf(totalData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String index) {
        File directory = new File(BASE_DIRECTORY);
        File[] fileList = directory.listFiles();
        for (File file : fileList) {
            if (file.getName().contains(index)) {
                file.delete();
            }
        }
    }

    public static int loadLastId() {
        String filePath = BASE_DIRECTORY + "/lastId.txt";
        try {
            if (new File(filePath).exists()) {
                return Integer.parseInt(Files.readString(Paths.get(filePath)).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<WiseSaying> loadWiseSayings(int lastId) {
        List<WiseSaying> wiseSayings = new ArrayList<>();
        File directory = new File(BASE_DIRECTORY);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".json") && !name.equals("lastId.txt") && !name.equals("data.json"));
            if (files != null) {
                for (File file : files) {
                    try {
                        WiseSaying wiseSaying = parseWiseSayingFile(file);
                        if (wiseSaying != null) {
                            wiseSayings.add(wiseSaying);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return wiseSayings;
    }

    public static WiseSaying parseWiseSayingFile(File file) throws IOException {
        String[] lines = Files.readAllLines(file.toPath()).toArray(new String[0]);
        try {
            int id = Integer.parseInt(lines[1].split(":")[1].trim().replace(",", "").replace("\"", ""));
            String quotes = lines[2].split(":")[1].trim().replace(",", "").replace("\"", "");
            String name = lines[3].split(":")[1].trim().replace(",", "").replace("\"", "");
            return new WiseSaying(id, quotes, name);
        } catch (Exception e) {
            System.err.println("Failed to parse file: " + file.getName());
            return null;
        }
    }

    public static void buildDataJsonFile() {
        StringBuilder combinedJson = new StringBuilder();
        File directory = new File(BASE_DIRECTORY);
        combinedJson.append("[\n");
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile() && !file.getName().equals("lastId.txt") && !file.getName().equals("data.json")) {
                    try {
                        String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                        combinedJson.append(content.trim());
                        if (i < files.length - 1) {
                            combinedJson.append(",\n");
                        }
                        combinedJson.append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            combinedJson.append("]");
            try {
                writeToFile(BASE_DIRECTORY + "/data.json", combinedJson.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}