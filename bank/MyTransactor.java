interface MyTransactor extends Transactor {
    public void deposit(Object key, float amount) throws BadAmount, KeyException;
    public void withdraw(Object key, float amount) throws BadAmount, KeyException;
    public float balance(Object key) throws KeyException;
}
