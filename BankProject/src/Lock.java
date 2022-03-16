

public interface Lock {
	
    // associate key with lock
    public void acquire(Object key) throws KeyException;
    // return true i key is associated with lock
    public boolean hasKey(Object key);

    // access protected code
    public void enter(Object key) throws KeyException;

    // get rid of key
    public void release(Object key);
}