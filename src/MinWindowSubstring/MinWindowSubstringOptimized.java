package MinWindowSubstring;

import java.util.*;

public class MinWindowSubstringOptimized {
    public String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        Map<Character, Integer> tFreq = new HashMap<>();
        for (char c : t.toCharArray()) {
            tFreq.put(c, tFreq.getOrDefault(c, 0) + 1);
        }

        Map<Character, Integer> windowFreq = new HashMap<>();
        int have = 0; // how many chars meet the freq required
        int need = tFreq.size();

        int left = 0, right = 0;
        int minLength = Integer.MAX_VALUE;
        String ans = "";

        while (right < s.length()) {
            char c = s.charAt(right);
            windowFreq.put(c, windowFreq.getOrDefault(c, 0) + 1);

            if (tFreq.containsKey(c) && windowFreq.get(c).intValue() == tFreq.get(c).intValue()) {
                have++;
            }

            while (have == need) {
                // update answer
                if (right - left + 1 < minLength) {
                    minLength = right - left + 1;
                    ans = s.substring(left, right + 1);
                }

                char leftChar = s.charAt(left);
                windowFreq.put(leftChar, windowFreq.get(leftChar) - 1);
                if (tFreq.containsKey(leftChar) && windowFreq.get(leftChar) < tFreq.get(leftChar)) {
                    have--;
                }
                left++;
            }

            right++;
        }

        return ans;
    }
}
