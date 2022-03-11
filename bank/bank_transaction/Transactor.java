class KeyException extends Exception {}
class BadAmount extends Exception {}

public interface Transactor {
    public void join(Object key) throws KeyException;
    public boolean canCommit(Object key) throws KeyException;
    public void commit(Object key) throws KeyException;
    public void abort(Object key) throws KeyException;
}