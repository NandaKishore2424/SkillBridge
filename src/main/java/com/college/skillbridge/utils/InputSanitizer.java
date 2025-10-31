package com.college.skillbridge.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class InputSanitizer {
    
    public static String sanitizeHTML(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.basic());
    }
    
    public static String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        // Escape HTML
        String escaped = StringEscapeUtils.escapeHtml4(input);
        // Remove any potential script tags
        escaped = escaped.replaceAll("<script>", "")
                       .replaceAll("</script>", "");
        return escaped.trim();
    }
    
    public static String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase();
    }
    
    public static String sanitizeSearchQuery(String query) {
        if (query == null) {
            return null;
        }
        // Remove special characters that could be used for SQL injection
        return query.replaceAll("[;'\"]", "").trim();
    }
}