package server;

import java.io.Serializable;
import java.io.*;
import intf.Person;

public class PersonImpl implements Person,Serializable{
    String name;
    float money;
    PersonImpl(String name){
        this.name = name;
    }
    //override
    public String getName(){
        return name;
    }
    // public String toString(){
    //     return name;
    // }
    public void write(){
        String filename = "bankBD_A.txt";
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write("es local???");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
}
