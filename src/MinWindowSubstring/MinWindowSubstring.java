package MinWindowSubstring;

import java.util.*;

public class MinWindowSubstring {
    public String minWindow(String s, String t) {
        int m = s.length();
        int n = t.length();
        Map<Character, Integer> tFreq = new HashMap<>();
        Map<Character, Integer> sFreq = new HashMap<>();

        String ans = "";

        int minLength = Integer.MAX_VALUE;

        for (char c : t.toCharArray()) {
            tFreq.put(c, tFreq.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0;

        while (right < m) {
            char c = s.charAt(right);

            sFreq.put(c, sFreq.getOrDefault(c, 0) + 1);

            while (search(sFreq, tFreq)) {
                if (right-left+1 < minLength) {
                    ans = s.substring(left, right+1);
                    minLength = right-left+1;
                }
                char leftChar = s.charAt(left);
                sFreq.put(leftChar, sFreq.get(leftChar) - 1);
                left++;
            }
            right++;

        }

        return ans;
    }

    public static boolean search(Map<Character, Integer> sFreq, Map<Character, Integer> tFreq) {
        for (char c : tFreq.keySet()) {
            if (!sFreq.containsKey(c) || sFreq.get(c) < tFreq.get(c)) {
                return false;
            }
        }
        return true;
    }
}
