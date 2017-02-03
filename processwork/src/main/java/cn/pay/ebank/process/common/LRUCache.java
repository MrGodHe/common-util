/*
 * www.ebank.pay.cn Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * zhyang@ebank.pay.cn 2015-04-17 15:41 创建
 *
 */
package cn.pay.ebank.process.common;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhyang@ebank.pay.cn
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
	private static final long	serialVersionUID	= -303457149654794582L;

	private final int			maxCapacity;

	private static final float	DEFAULT_LOAD_FACTOR	= 0.75f;

	private final Lock lock				= new ReentrantLock ();

	public LRUCache (int maxCapacity) {
		super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
		this.maxCapacity = maxCapacity;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > maxCapacity;
	}

	@Override
	public boolean containsKey(Object key) {
		try {
			lock.lock();
			return super.containsKey(key);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public V get(Object key) {
		try {
			lock.lock();
			return super.get(key);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		try {
			lock.lock();
			return super.put(key, value);
		} finally {
			lock.unlock();
		}
	}

	public int size() {
		try {
			lock.lock();
			return super.size();
		} finally {
			lock.unlock();
		}
	}

	public Set<K> keySet() {
		return new HashSet<K> (super.keySet());
	}

	public void clear() {
		try {
			lock.lock();
			super.clear();
		} finally {
			lock.unlock();
		}
	}

	public Collection<V> getAll() {
		try {
			lock.lock();
			return new ArrayList<V>(super.values());
		} finally {
			lock.unlock();
		}
	}
}
