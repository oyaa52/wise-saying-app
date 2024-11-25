package com.ll;

import com.ll.domain.entity.WiseSaying;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    private final List<WiseSaying> list = new ArrayList<>();
    private int totalData = 0;

    public void save(WiseSaying wiseSaying) {
        wiseSaying.setId(++totalData);
        list.add(wiseSaying);
        updateWiseSayingFile(wiseSaying);
    }

    public List<WiseSaying> getList() {
        return list;
    }

    public void setList(List l) {
        list.addAll(l);
    }

    public boolean delete(String id) {
        boolean removed = list.removeIf(e -> e.getId() == Integer.parseInt(id));
        if (removed) {
            deleteFile(id);
        }
        return removed;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int num) {
        totalData = num;
    }

    public void updateLastId() {
        updateLastIdFile();
    }

    //    class FileUtil {
    private static final String BASE_DIRECTORY = "db/wiseSaying";

    public static void createDirectory() {
        File directory = new File(BASE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static String createFileName(WiseSaying wiseSaying) {
        return BASE_DIRECTORY + "/" + wiseSaying.getId() + ".json";
    }

    public static String wiseSayingToJson(WiseSaying wiseSaying) {
        return "{\n\t\"id\": " + wiseSaying.getId() + ",\n" +
                "\t\"content\": \"" + wiseSaying.getQuotes() + "\",\n" +
                "\t\"author\": \"" + wiseSaying.getAuthor() + "\"\n}";
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

    public void updateLastIdFile() {
        String filePath = BASE_DIRECTORY + "/lastId.txt";
        try {
            writeToFile(filePath, String.valueOf(getTotalData()));
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
            String author = lines[3].split(":")[1].trim().replace(",", "").replace("\"", "");
            WiseSaying wiseSaying = new WiseSaying(quotes, author);
            wiseSaying.setId(id);
            return wiseSaying;
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
//    }
}
