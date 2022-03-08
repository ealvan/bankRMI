package client;

public class RunClient{
    public static void main(String args[]){
        String str;
        try{
            RMIClient client = new RMIClient();
            client.startClient();
            str = client.toUpperCase("pepe");
            System.out.println(str);
        }catch(Exception e){
            System.out.println("Error in RMI client");
        }
    }

}