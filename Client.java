package client;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, IOException {
        Scanner sc = new Scanner(System.in);
        Socket s=null ;
        while (true) {
            try {
                s = new Socket("localhost", 5789);
                InputStreamReader in = new InputStreamReader(s.getInputStream());
                BufferedReader bf = new BufferedReader(in);
                String str = bf.readLine();
                System.out.println("Server_MSG: " + str + "\n");
            } catch (Exception e) {
                System.out.println("Ошибка");
            }

        }
    }
    public String getMsg(String str, Socket s){
        
        return str;
    }

}
