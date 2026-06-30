package util;

import java.io.File;
import java.io.IOException;

public class FileChecker {

    public static void checkAndCreateFile(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Lỗi khi tạo file: " + e.getMessage());
            }
        }
    }
}
