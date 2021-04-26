package client;

import java.util.List;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    private static Socket s;
    private static String ctlg_dest = "C:/Users/abbos/Desktop/";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException, IOException {
        
        while (true) {
            try {
                s = new Socket("localhost", 3000);
                System.out.println(getMsg() + "\nВ какую папку перейти? Что-то скачать?");// получение директории
                check_connect();
                move_ctlg();// перемещение между каталогамии

                System.out.println("end");
                System.in.read();
            } catch (Exception e) {
                System.out.println("Ошибка " + e.toString());
            }

        }
    }

    
    public static void move_ctlg() throws IOException {
        while (true) {
            String str = sc.nextLine();
            if (str == "Выйти" || str == "выйти") {
                s.close();
                break;
            }
            sendMsg(str);
            check_connect();
            if (str.contains("cd.")) {
                System.out.println(getMsg());
            } else if (str.contains(".")) { // если нету cd то это файл с каким-то расширением .txt к примеру
                downloads(str);
                System.out.println(getMsg());
            } else {
                System.out.println(getMsg());
            }
            check_connect();
        }
    }

    public static void downloads(String str) throws IOException {
        FileOutputStream f = new FileOutputStream(ctlg_dest + str);
        f.write(getMsgB());// запись файла в путь ctlg_dest с названием str
        check_connect();
        f.close();
        System.out.println("Скачано!\nПуть:" + ctlg_dest + str);
    }

    public static void check_connect() throws IOException {// проверка соединения
        if (s.isClosed()) {
            s = new Socket("localhost", 3000);
        }
    }

    public static void sendMsg(String msg) throws IOException {// отправка сообщения на сервер
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(msg);
        pr.flush();
        if (pr.checkError()) {
            System.out.println("ОШИБКА ОТПРАВКИ");
            check_connect();
            sendMsg(msg);
        }
        pr.close();
    }

    public static String getMsg() throws IOException, IOException {// передаются текстовые файлы
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

    public static byte[] getMsgB() throws IOException, IOException {// передаются не текстовые файлы
        InputStream in = s.getInputStream();
        List<Integer> data = new ArrayList<Integer>();
        int x;
        while (true) {
            x = in.read();
            if (x == -1) {
                break;
            }
            data.add(x);
        }
        in.close();

        byte[] result = new byte[data.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = data.get(i).byteValue();
        }
        return result;
    }

}
