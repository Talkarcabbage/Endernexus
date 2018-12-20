package io.github.talkarcabbage.endernexus.util;

/**
 * Allows storing an object as a generic for use with types that could be missing at runtime.
 * @author Talkarcabbage
 *
 * @param <T> An object
 */
public class StorageWrapper<T> {

	T object;
	
	/**
	 * Returns a "lazy" StorageWrapper wrapping the specified object.
	 * @param object
	 */	
	public StorageWrapper(T object) {
		this.object = object;
	}
	
	public T get() {
		return object;
	}
	
	public void set(T t) {
		this.object = t;
	}
}
