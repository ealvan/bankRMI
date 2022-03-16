package bank2;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
class Client implements Serializable{
    private String name=null;
    private int age = -1;
	private String userID = null;
    Client(String name, int age){
        this.name = name;
        this.age = age;        
    }
    Client(String userID, String name, int age){
        this.name = name;
        this.age = age;        
        this.userID = userID;
    }
    public String toString(){
        return "UserID = "+userID+" -- "+name+" -- "+age;
    }
};

public class Storage {
    public static final String thisPackage = "bank1";
    public static final String UserFile="./"+thisPackage+"/users.ser";
    public static final String AccountFile="./"+thisPackage+"/accounts.ser";

    
    public static void saveObject(Object obj,final String filename){
        //write to file
        try{
            FileOutputStream writeData = new FileOutputStream(filename);
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(obj);
            writeStream.flush();
            writeStream.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object retrieveObject(final String filename){
        try{
            FileInputStream readData = new FileInputStream(filename);
            ObjectInputStream readStream = new ObjectInputStream(readData);
        
            Object people2 = readStream.readObject();
            readStream.close();
            return people2;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public static void main(String args[]){
        ArrayList<Client> list = new ArrayList<Client>() {
            {
                add(new Client("pepe1",12));
                add(new Client("pepe2",123));
                add(new Client("pepe3",124));
            }
        };        
        // Storage Storage = new Storage();
        Storage.saveObject(list, Storage.UserFile);

        ArrayList<Client> newList =(ArrayList<Client>) Storage.retrieveObject(Storage.UserFile);

        System.out.println(newList);
        HashMap<String,Client> map = new HashMap<String,Client>(){
            {
                put("user1",new Client("user1","user1",2));
                put("user2",new Client("user2","user2",3));
                put("user4",new Client("user4","user4",3));
                put("user5",new Client("user5","user5",3));
            }
        };
        Storage.saveObject(map, Storage.AccountFile);
        HashMap<String,Client> newMap =(HashMap<String,Client>) Storage.retrieveObject(Storage.AccountFile);

        for(String key: newMap.keySet()){
            System.out.println(newMap.get(key));
        }
    }
}

