# Goal of the Code
#### Find the smallest window in a string s that contains all characters of another string t (including duplicates).

# Code Breakdown
### Class: `MinWindowSubstring`
```
public class MinWindowSubstring {
    public String minWindow(String s, String t) {
```

* minWindow is the main method.
* It takes in two strings: s (the full string), and t (the required characters).

### Step 1: Initial Setup
```
int m = s.length();
int n = t.length();
Map<Character, Integer> tFreq = new HashMap<>();
Map<Character, Integer> sFreq = new HashMap<>();
String ans = "";
int minLength = Integer.MAX_VALUE;
```

* m, n: lengths of s and t.
* tFreq: holds character counts from t.
* sFreq: holds character counts for the current window in s.
* ans: stores the best (smallest valid) window found so far.
* minLength: tracks the smallest length of a valid window.

### Step 2: Build Frequency Map for t
```
for (char c : t.toCharArray()) {
    tFreq.put(c, tFreq.getOrDefault(c, 0) + 1);
}
```
* This loop fills tFreq with the required character counts.
* Example: If `t = "AABC"`, then `tFreq = { A:2, B:1, C:1 }`.

### Step 3: Sliding Window Pointers
```
int left = 0, right = 0;
```
* `left` and `right` define the current window: `[left, right]`.

### Step 4: Traverse s Using Sliding Window
```
while (right < m) {
    char c = s.charAt(right);
    sFreq.put(c, sFreq.getOrDefault(c, 0) + 1);
```
* Expand the window by moving `right` and update the count of the current character in `sFreq`.

### Step 5: Check for Valid Window
```
while (search(sFreq, tFreq)) {
    if (right - left + 1 < minLength) {
        ans = s.substring(left, right + 1);
        minLength = right - left + 1;
    }
```
* If `search()` returns `true`, it means the current window has all the required characters.
* Check if this is the smallest valid window seen so far. If yes, update `ans` and `minLength`.

### Step 6: Shrink the Window from the Left
```
    char leftChar = s.charAt(left);
    sFreq.put(leftChar, sFreq.get(leftChar) - 1);
    left++;
}
right++;
```
* Reduce the count of the character at `left`, and move `left` to the right to try and shrink the window while it’s still valid.

### Step 7: Return Final Answer
```
return ans;
```

### Helper Function: `search`
```
public static boolean search(Map<Character, Integer> sFreq, Map<Character, Integer> tFreq) {
    for (char c : tFreq.keySet()) {
        if (!sFreq.containsKey(c) || sFreq.get(c) < tFreq.get(c)) {
            return false;
        }
    }
    return true;
}
```
* This function verifies if all characters from tFreq are present in sFreq with at least the required frequency.

### Example
For:
* `s = "ADOBECODEBANC"`
* `t = "ABC"`

Your method finds:
* Smallest valid window is `"BANC"`.

### Complexity
* 🎯 Time Complexity: O(m), where m is the length of s
  * This is because every character is added and removed from the window at most once.
  * search() is treated as constant time due to the fixed-size alphabet.
* 🎯 Space Complexity: O(1) (assuming only lowercase letters), or O(k) where k = number of unique characters