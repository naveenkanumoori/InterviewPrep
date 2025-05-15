package LFUCache;

import java.util.*;

public class LFUCache {
    private class Node {
        int key, value, freq;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    private final int capacity;
    private int minFreq;
    private final Map<Integer, Node> cache; // key to node
    private final Map<Integer, LinkedHashSet<Integer>> freqMap; // freq to keys
    private final Map<Integer, Integer> keyToFreq; // key to freq

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.cache = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.keyToFreq = new HashMap<>();
    }

    public int get(int key) {
        if (!cache.containsKey(key))
            return -1;
        updateFrequency(key);
        return cache.get(key).value;
    }

    public void put(int key, int value) {
        if (capacity == 0)
            return;
        if (cache.containsKey(key)) {
            cache.get(key).value = value;
            updateFrequency(key);
        } else {
            if (cache.size() >= capacity) {
                evictLFU();
            }
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            keyToFreq.put(key, 1);
            freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
            minFreq = 1;
        }
    }

    private void updateFrequency(int key) {
        int freq = keyToFreq.get(key);
        keyToFreq.put(key, freq + 1);
        freqMap.get(freq).remove(key);
        if (freqMap.get(freq).isEmpty()) {
            freqMap.remove(freq);
            if (freq == minFreq)
                minFreq++;
        }
        freqMap.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
    }

    private void evictLFU() {
        LinkedHashSet<Integer> keys = freqMap.get(minFreq);
        int evictKey = keys.iterator().next();
        keys.remove(evictKey);
        if (keys.isEmpty())
            freqMap.remove(minFreq);
        cache.remove(evictKey);
        keyToFreq.remove(evictKey);
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */