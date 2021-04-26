package server;

import java.net.*;
import java.io.*;
import java.util.Base64;
import java.util.Scanner;

public class Server {

    private static String ctlg_path = "C:/Users";
    private static Socket s;
    private static ServerSocket ss;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        ss = new ServerSocket(3000);
        while (true) {
            System.out.println("start");
            s = ss.accept();
            sendMsg(directory());// отправка директории
            reconnect();
            move_ctlg();

            System.out.println("end");
            System.in.read();
        }
    }

    public static void move_ctlg() throws IOException {// перемещение по папкам 
        int i = 0;
        while (true) {
            String str = getMsg();
            System.out.println(str);
            reconnect();

            command(str);
            sendMsg(directory());
            reconnect();

            i++;
            if (i > 30) {
                break;
            }
        }
    }

    public static void reconnect() throws IOException {// проверка соединения
        if (s.isClosed()) {
            s = ss.accept();
        }
    }

    public static void command(String comm) throws IOException {
        if (comm.contains("cd")) {
            if (comm.contains("..")) {
                directory_rollback();
            } else if (directory().contains("(Папка)" + comm.substring(3, comm.length() - 2))) {
                ctlg_path += "/" + comm.substring(3, comm.length() - 2);
            } else if (comm.contains("C:/")) {
                ctlg_path = comm.substring(comm.indexOf("C:/"), comm.length() - 2);
            }
        } else if (directory().contains("(Файл)" + comm.substring(0, comm.length() - 2))) {
            uploads(comm);
        }
    }

    public static void uploads(String comm) throws IOException {
        InputStream f= new FileInputStream(ctlg_path + "/" + comm.substring(0, comm.length() - 2));
        OutputStream out = s.getOutputStream();
        int j;
        while ((j = f.read()) != -1) {
            out.write(j);
        }
        out.flush();
        reconnect();
    }

    public static void directory_rollback() {
        int tmp = 0;
        for (int i = ctlg_path.length() - 1; i > 0; i--) {
            if (ctlg_path.charAt(i) == '/') {
                tmp = i;
                break;
            } else if (i == 1) {
                tmp = 0;
                break;
            }
        }
        if (tmp != 0) {
            String str = "";
            for (int i = tmp, b = 0; i > 0; i--, b++) {
                str += ctlg_path.charAt(b);
            }
            ctlg_path = str;
        }
    }

    public static String directory() {
        File f = new File(ctlg_path);
        String str = "\nПуть: " + ctlg_path + "\nКаталог файлов:";
        int j = 1;
        try {
            for (File i : f.listFiles()) {
                str += "\n" + j + ") ";
                if (i.getName().contains(".")) {
                    str += "(Файл)" + i.getName();
                    str+=" размер "+(double)i.length()/(1024*1024)+" mb";;
                } else {
                    str += "(Папка)" + i.getName();
                }
                
                j++;
            }
        } catch (Exception e) {
            directory_rollback();
            str = "Извинте этот элемент недоступен!\n" + directory();
        }
        return str;
    }

    public static void sendMsg(String msg) throws IOException {
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.print(msg);
        pr.flush();
        if (pr.checkError()) {
            System.out.println("ОШИБКА ОТПРАВКИ");
        }
        pr.close();

    }
    
   

    public static String getMsg() throws IOException, IOException {
        InputStreamReader in = new InputStreamReader(s.getInputStream(), "UTF-8");
        String str = "";
        char x;
        while (true) {
            x = (char) in.read();
            if (x == '￿') {
                break;
            }
            str += x;
        }
        in.close();
        return str;
    }
}
