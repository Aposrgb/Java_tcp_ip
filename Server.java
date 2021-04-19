package server;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {
    static String ctlg_path="C:/Users/abbos/Downloads";
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5789);
        Socket s;
        Scanner sc = new Scanner(System.in);
        while (true) {
            s = ss.accept();
            System.out.println("Connected");
            if(s.isClosed()==false){
                PrintWriter pr = new PrintWriter(s.getOutputStream());
                pr.println(sc.next());
                pr.flush();
            }
            s.close();
        }
    }
    public static void command (String comm){
        if(comm=="cd.."){
            int tmp=0;
            for(int i=ctlg_path.length()-1;i>0;i--){
                if(ctlg_path.charAt(i)=='/'){
                    tmp=i;
                    break;
                }
                else if(i==1){
                    tmp=0;
                    break;
                }
            }
            if(tmp!=0){
                comm="";
                for(int i=tmp,j=0;i>0;i--,j++){
                    comm+=ctlg_path.charAt(j);
                }
                ctlg_path=comm;
            }
        }
    }
    public static String directory(){
        File f = new File(ctlg_path);
        String str ="Путь: "+ctlg_path+"\nКаталог файлов: \n"; 
        int j=1;
        for(File i : f.listFiles()){
            str+=j+") ";
            if(i.getName().contains(".")){
                str+=i.getName()+"\n";
            }
            else{
                str+="(Папка)"+i.getName()+"\n";
            }
            j++;
        }
        return str;
    }
}
