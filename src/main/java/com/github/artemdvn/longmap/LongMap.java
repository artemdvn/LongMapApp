package com.github.artemdvn.longmap;

import java.lang.reflect.Array;

public class LongMap<V> implements TestMap<V> {
	
	/**
     * The default initial capacity - MUST be a power of two.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    
    /**
     * The largest possible table capacity.  This value must be
     * exactly 1<<30 to stay within Java array allocation and indexing
     * bounds for power of two table sizes, and is further required
     * because the top two bits of 32bit hash fields are used for
     * control purposes.
     * The first bit is reserved for the sign bit.
     * It is 1 if the number is negative and 0 if it is positive.
     * 1 << 30 is equal to 1,073,741,824 or 2^30
     * It's two's complement binary integer 01000000-00000000-00000000-00000000.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    /**
     * The table, initialized on first use, and resized as
     * necessary. When allocated, length is always a power of two.
     */
    private Entry<Long, V>[] table;
    
    /**
     * The next size value at which to resize (capacity * load factor).
     */
    private int threshold;
    
    /**
     * The load factor for the hash table.
     */
    private final float loadFactor;
    
    /**
     * The number of key-value pairs in this map.
     */
    private int size;
    
    /**
     * Key-value entry.
     */
	@SuppressWarnings("hiding")
	static class Entry<Long, V> {
		final int hash;
		final Long key;
		V value;
		Entry<Long, V> next;

		public Entry(int hash, Long key, V value, Entry<Long, V> next) {
			this.hash = hash;
			this.key = key;
			this.value = value;
			this.next = next;
		}

		public Long getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public boolean hasNext() {
			return next != null;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}

		@Override
		public final int hashCode() {
			return key.hashCode() ^ value.hashCode();
		}

		@Override
		public final boolean equals(Object o) {
			if (o == this)
				return true;
			if (o instanceof Entry) {
				Entry<?, ?> e = (Entry<?, ?>) o;
				if (key.equals(e.getKey()) && value.equals(e.getValue()))
					return true;
			}
			return false;
		}

	}

	/**
     * Constructs an empty @LongMap with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public LongMap() {
    	this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    /**
     * Constructs an empty @LongMap with the specified initial
     * capacity and load factor.
     *
     * @param  initialCapacity the initial capacity
     * @param  loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is nonpositive
     */
	public LongMap(int initialCapacity, float loadFactor) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		}		
		if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
			throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		}
		if (initialCapacity > MAXIMUM_CAPACITY) {
			initialCapacity = MAXIMUM_CAPACITY;
		}
		this.loadFactor = loadFactor;
		this.threshold = (int) (initialCapacity * loadFactor);
	}
    
	/**
     * Constructs an empty @LongMap with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param  initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     */
    public LongMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     */
    public boolean containsKey(long key) {
        return get(key) != null;
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the
     * specified value.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     *         specified value
     */
	public boolean containsValue(V value) {
		Entry<Long, V>[] tab;
		V v;
		if ((tab = this.table) != null && size > 0) {
			for (int i = 0; i < tab.length; ++i) {
				for (Entry<Long, V> elem = tab[i]; elem != null; elem = elem.next) {
					if ((v = elem.value) == value || (value != null && value.equals(v))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
     * Returns an array view of the keys contained in this map.
     *
     * @return an array view of the keys contained in this map
     */
	public long[] keys() {
		long[] keys = new long[size];
		if (table != null && size > 0) {
			int count = 0;
			for (int i = 0; i < table.length; i++) {
				for (Entry<Long, V> elem = table[i]; elem != null; elem = elem.next) {
					keys[count++] = elem.getKey();
				}
			}
		}
		return keys;
	}

	/**
     * Returns an array view of the values contained in this map.
     * 
     * @return an array view of the values contained in this map
     */
	@SuppressWarnings("unchecked")
	public V[] values() {
		V[] values = null;
		if (table != null && size > 0) {
			int count = 0;
			for (int i = 0; i < table.length; i++) {
				for (Entry<Long, V> elem = table[i]; elem != null; elem = elem.next) {
					if (values == null) {
						values = (V[]) getArray(elem.getValue().getClass(), size);
					}
					values[count++] = elem.getValue();
				}
			}
		}
		return values;
	}
	
	/**
	 * Creates new array of generic type
	 */
	@SuppressWarnings("unchecked")
	private <E> E[] getArray(Class<E> clazz, int size) {
		return (E[]) Array.newInstance(clazz, size);
	}

	/**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.     *
     * A return value of {@code null} is also
     * possible if the map explicitly maps the key to {@code null}.
     */
    public V get(long key) {
        if (table == null) {
            return null;
        }
        int index = getIndex(key);
        Entry<Long, V> elem = table[index];

        while (elem != null) {
            if (elem.key.equals(key)) {
                return elem.getValue();
            }
            elem = elem.next;
        }
        return null;
    }
    
    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
	public void clear() {
		Entry<Long, V>[] tab = table;
		if (tab != null && size > 0) {
			size = 0;
			for (int i = 0; i < tab.length; ++i)
				tab[i] = null;
		}
	}

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public long size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     *         (A {@code null} return can also indicate that the map
     *         previously associated {@code null} with {@code key}.)
     */
    public V put(long key, V value) {
        return putVal(getHash(Long.valueOf(key).hashCode()), key, value);
    }
    
    /**
     * Removes the key (and its corresponding value) from this map.
     * This method does nothing if the key is not in the map.
     *
     * @param  key the key that needs to be removed
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}
     */
    public V remove(long key) {
        int index = getIndex(key);

        for (Entry<Long, V> elem = table[index]; elem != null; elem = elem.next) {
            if (elem.key.equals(key)) {
				table[index] = elem.hasNext() ? elem.next : null;
                if (--size <= threshold / 2) {
                    resize(table.length / 2);
                }

                return elem.getValue();
            }
        }
        return null;
    }
    
    /**
     * Implementation for put
     */
    private V putVal(int hash, Long key, V value) {
        V oldValueIfUpdated = addEntry(hash, key, value);
        if (oldValueIfUpdated != null) {
            return oldValueIfUpdated;
        }

        if (++size > threshold) {
			resize((table.length * 2 > MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : table.length * 2);
        }
        return null;
    }

    /**
     * Add new entry or update existing with new value
     */
    private V addEntry(int hash, Long key, V value) {
        Entry<Long, V> existingEntry;
        int tableSize;
        int index;

        if (table == null || (tableSize = table.length) == 0) {
        	resize(DEFAULT_INITIAL_CAPACITY);
            tableSize = table.length;
        }
        
        index = (tableSize - 1) & hash;
        if ((existingEntry = table[index]) == null) {
            table[index] = new Entry<>(hash, key, value, null);
        } else {
            for (; existingEntry != null; existingEntry = existingEntry.next) {
                if (existingEntry.hash == hash && (existingEntry.key == key || (key != null && key.equals(existingEntry.key)))) {
                    V oldValue = existingEntry.value;
                    existingEntry.value = value;
                    return oldValue;
                } else {
                    Entry<Long, V> newEntry = new Entry<>(hash, key, value, table[index]);
                    table[index] = newEntry;
                }
            }
        }
        return null;
    }

    private int getIndex(long key) {
        int hash = getHash(Long.valueOf(key).hashCode());
        return (table.length - 1) & hash;
    }
    
    /**
     * The first shift of 20 positions xor with the second shift of 12 positions creates a mask that can flip 0 or more of the bottom 20 bits of the int. 
     * So you can get some randomness inserted into the bottom bits that makes use of the potentially better distributed higher bits. 
     * This is then applied via xor to the original value to add that randomness to the lower bits. 
     * The second shift of 7 positions xor the shift of 4 positions creates a mask that can flip 0 or more of the bottom 28 bits, 
     * which brings some randomness again to the lower bits and to some of the more significant ones by capitalizing 
     * on the prior xor which already addressed some of the distribution at the lower bits. 
     * The end result is a smoother distribution of bits through the hash value.
     */
    private int getHash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		if (table == null) {
			threshold = (int) (capacity * loadFactor);
			size = 0;
			table = (Entry<Long, V>[]) new Entry[capacity];
		}

		Entry<Long, V>[] newTable = (Entry<Long, V>[]) new Entry[capacity];
		Entry<Long, V>[] oldTable = table;
		table = newTable;
		threshold = (int) (table.length * loadFactor);

		for (int i = 0; i < oldTable.length; i++) {
			Entry<Long, V> oldEntry = oldTable[i];
			if (oldEntry != null) {
				addEntry(oldEntry.hash, oldEntry.key, oldEntry.value);
			}
		}
	} 

}
