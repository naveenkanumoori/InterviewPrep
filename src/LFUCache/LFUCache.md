# LFU Cache Logic Explained 

The LFU (Least Frequently Used) cache implementation follows a specific strategy for managing cache entries based on their usage frequency. Here's a detailed breakdown of the logic:

## Core Principle
The LFU cache evicts items that are least frequently used when the cache reaches capacity. If multiple items have the same frequency, it evicts the least recently used among them.

## Data Structures Breakdown
1. Node Class
   * Stores the key, value, and frequency count
   * Each new entry starts with frequency = 1


2. Main Components

   * cache: HashMap mapping keys → Node objects
   * freqMap: HashMap mapping frequencies → LinkedHashSet of keys
   * keyToFreq: HashMap mapping keys → current frequency

## Key Operations Logic
### Get Operation
   ```
   public int get(int key) {
        if (!cache.containsKey(key))
            return -1;
        updateFrequency(key);
        return cache.get(key).value;
   }
   ```
   #### Logic Flow:
   1. Check if key exists 
   2. If exists:
      * Update its frequency (via updateFrequency)
      * Return the value 
   3. If not exists: return -1

### Put Operation

```
public void put(int key, int value) {
    if (capacity == 0) return;
    
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
```
   #### Logic Flow:

1. Handle zero capacity edge case
2. For existing key:
   * Update value
   * Update frequency
3. For new key:
   * If at capacity, evict LFU item
   * Create new node
   * Add to all data structures
   * Set minimum frequency to 1 (new items)

### Update Frequency (Helper)
   
   ```
   private void updateFrequency(int key) {
    int freq = keyToFreq.get(key);
    keyToFreq.put(key, freq + 1);
    
    // Remove from current frequency set
    freqMap.get(freq).remove(key);
    
    // Clean up if empty
    if (freqMap.get(freq).isEmpty()) {
        freqMap.remove(freq);
        if (freq == minFreq) {
            minFreq++;
        }
    }
    
    // Add to new frequency set
    freqMap.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
}
   ```
   #### Logic Flow:

1. Get current frequency
2. Increment frequency in keyToFreq
3. Remove from old frequency set
4. If old frequency set becomes empty:
   * Remove the frequency entry
   * If this was minFreq, increment minFreq
5. Add to new frequency set

### Evict LFU (Helper)
   ```
   private void evictLFU() {
    LinkedHashSet<Integer> keys = freqMap.get(minFreq);
    int evictKey = keys.iterator().next();
    keys.remove(evictKey);
    
    if (keys.isEmpty()) {
        freqMap.remove(minFreq);
    }
    
    cache.remove(evictKey);
    keyToFreq.remove(evictKey);
  }
```
   #### Logic Flow:

1. Get keys with minimum frequency
2. Select first key (LRU among LFU)
3. Remove from frequency set
4. Clean up if set becomes empty
5. Remove from cache and keyToFreq maps

## Why These Data Structures?
#### 1. LinkedHashSet:
*   Maintains insertion order (for LRU within same frequency)
* O(1) add/remove operations

#### 2. Multiple HashMaps:
* Provide O(1) access to nodes, frequencies, and frequency groups
* Enable efficient frequency updates

## Example Walkthrough
### Capacity = 2

1. **put(1,10)**
   * Cache: {1:10(freq=1)}
   * freqMap: {1: [1]}
   * minFreq = 1

2. **put(2,20)**
   * Cache: {1:10(freq=1), 2:20(freq=1)}
   * freqMap: {1: [1,2]}
   * minFreq = 1

3. **get(1)**
   * Update 1's frequency to 2
   * freqMap: {1: [2], 2: [1]}
   * minFreq = 1

4. **put(3,30) (cache full)**
   * Evict key 2 (LFU)
   * Add key 3
   * Cache: {1:10(freq=2), 3:30(freq=1)}
   * freqMap: {1: [3], 2: [1]}
   * minFreq = 1

This implementation efficiently maintains usage frequencies while handling evictions in constant time on average.