package vn.aptech.pixelpioneercourse.until;

import java.util.HashMap;
import java.util.Map;

public class SensitiveWordFilter {
    private static final Map<String, String> SENSITIVE_WORDS_MAP = new HashMap<>();

    static {
        SENSITIVE_WORDS_MAP.put("badword1", "****");
        SENSITIVE_WORDS_MAP.put("badword2", "****");
        // Add more words as needed
    }

    public static String filterSensitiveWords(String content) {
        for (Map.Entry<String, String> entry : SENSITIVE_WORDS_MAP.entrySet()) {
            content = content.replaceAll("(?i)" + entry.getKey(), entry.getValue());
        }
        return content;
    }
}
