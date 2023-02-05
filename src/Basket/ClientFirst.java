package Basket;
/*Код программы клиента*/

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientFirst {
    private static final String[] firstScreenCommand = new String[]{"Osnovnaia programma (1)", "Redactirovanie dannih (2)", "Viiti (q)"};
    private static final String[] secondScreenCommand = new String[]{"Users (1)", "Materials (2)", "V nachalo (3)", "Viiti (q)"};
    private static final String[] thirdScreenCommand = new String[]{"Dobavit' (1)", "Udalit' (2)", "V nachalo' (3)", "Viiti (q)"};

    public static void main(String[] arg) throws IOException,
            ClassNotFoundException {
        try (Socket clientSocket = new Socket("127.0.0.1", 2525)) {
            /*с ресурсами - в скобках все ресурсы которые можно закрыть которые реализуют closeable или autocloseable*/
            ObjectOutputStream coos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream cois = new ObjectInputStream(clientSocket.getInputStream());

            firstScreen(clientSocket, coos, cois);
        }
    }

    private static void firstScreen(Socket clientSocket, ObjectOutputStream coos, ObjectInputStream cois) throws IOException, ClassNotFoundException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        coos = (coos != null) ? coos : new ObjectOutputStream(clientSocket.getOutputStream());
        cois = (cois != null) ? cois : new ObjectInputStream(clientSocket.getInputStream());

        SysOut.notice("Soedinenie ustanovleno....");
        SysOut.Title("Pridumat' nazvanie");
        SysOut.Description("Opisanie proecta i/ili raboti");

        SysOut.command(firstScreenCommand);

        String choose = stdin.readLine();

        switch (choose) {
            case "1" -> runMainProgramm(clientSocket, coos, cois);
            case "2" -> runEditDataProgramm(clientSocket, coos, cois);
            case "q" -> SysOut.notice("zavershenie programmi...");
            default -> {
            }
        }
    }

    private static void runEditDataProgramm(Socket clientSocket,
                                            ObjectOutputStream coos, ObjectInputStream cois) throws IOException, ClassNotFoundException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        coos = (coos != null) ? coos : new ObjectOutputStream(clientSocket.getOutputStream());
        cois = (cois != null) ? cois : new ObjectInputStream(clientSocket.getInputStream());

        SysOut.Title("Vibor sushnosti");
        SysOut.Description("Vibirite kakuiu tablicu vi hotite redactirovat'");
        SysOut.command(secondScreenCommand);

        String choose = stdin.readLine();

        switch (choose) {
            case "1" -> runEditDataSecondProgramm("Users", clientSocket, coos, cois);
            case "2" -> runEditDataSecondProgramm("Materials", clientSocket, coos, cois);
            case "3" -> firstScreen(clientSocket, null, null);
            default -> {
            }
        }
    }

    private static void runEditDataSecondProgramm(String entity, Socket clientSocket,
                                                  ObjectOutputStream coos, ObjectInputStream cois) throws IOException, ClassNotFoundException {
        entity = entity.toLowerCase(Locale.ROOT);
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        coos = (coos != null) ? coos : new ObjectOutputStream(clientSocket.getOutputStream());
        cois = (cois != null) ? cois : new ObjectInputStream(clientSocket.getInputStream());

        SysOut.Title("Vibor deistvia");
        SysOut.Description("Vibirite udalit' ili dobavit' zapis'");
        SysOut.command(thirdScreenCommand);

        String choose = stdin.readLine();

        switch (choose) {
            case "1" -> AddField(entity, clientSocket, coos, cois);
            case "2" -> DeleteField(entity, clientSocket, coos, cois);
            case "3" -> firstScreen(clientSocket, null, null);
            default -> {
            }
        }
    }

    private static void DeleteField(String entity, Socket clientSocket,
                                    ObjectOutputStream coos, ObjectInputStream cois) throws IOException, ClassNotFoundException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        coos = (coos != null) ? coos : new ObjectOutputStream(clientSocket.getOutputStream());
        cois = (cois != null) ? cois : new ObjectInputStream(clientSocket.getInputStream());

        SysOut.Title("Vibirite ID polia");
        String fieldId = stdin.readLine();

        HashMap<String, HashMap<String, String>> clientData = new HashMap<>() {{
            put("delete", new HashMap<>() {{
                put("entity", entity);
                put("fieldId", fieldId);
            }});
        }};
        coos.writeObject(clientData);/*отправляем данные для удаления записи сущности по id */
        HashMap serverData = (HashMap) cois.readObject(); /* принимаем данные об удалении записи */
        if (Objects.equals((String) serverData.get("status"), "success")) {
            SysOut.notice((String) serverData.get("message"));
        } else if (Objects.equals((String) serverData.get("status"), "error")) {
            SysOut.error((String) serverData.get("message"));
        }
        runEditDataProgramm(clientSocket, coos, cois);
    }

    private static void AddField(String entity, Socket clientSocket,
                                 ObjectOutputStream coos, ObjectInputStream cois) throws IOException, ClassNotFoundException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        coos = (coos != null) ? coos : new ObjectOutputStream(clientSocket.getOutputStream());
        cois = (cois != null) ? cois : new ObjectInputStream(clientSocket.getInputStream());

        if(Objects.equals(entity, "users")) {
            SysOut.CommandText("Vibirite ID polia");
            String id = stdin.readLine();

            SysOut.CommandText("Vibirite user name");
            String name = stdin.readLine();

            SysOut.CommandText("Vibirite user phone");
            String phone = stdin.readLine();

            HashMap<String, HashMap<String, String>> clientData = new HashMap<>() {{
                put("add", new HashMap<>() {{
                    put("entity", entity);
                    put("name", name);
                    put("id", id);
                    put("phone", phone);
                }});
            }};
            coos.writeObject(clientData);/*отправляем данные для добавления записи сущности */
        } else if (Objects.equals(entity, "materials")) {
            SysOut.CommandText("Vibirite ID polia");
            String fieldId = stdin.readLine();

            SysOut.CommandText("Vibirite nazvanie materiala");
            String materialName = stdin.readLine();

            SysOut.CommandText("Vibirite tcenu materiala");
            String materialPrice = stdin.readLine();

            SysOut.CommandText("Vibirite datu materiala");
            String materialDate = stdin.readLine();

            HashMap<String, HashMap<String, String>> clientData = new HashMap<>() {{
                put("add", new HashMap<>() {{
                    put("entity", entity);
                    put("fieldId", fieldId);
                    put("materialName", materialName);
                    put("materialPrice", materialPrice);
                    put("materialDate", materialDate);
                }});
            }};
            coos.writeObject(clientData);/*отправляем данные для добавления записи сущности */
        }

        HashMap serverData = (HashMap) cois.readObject(); /* принимаем данные о добавлении записи */

        if (Objects.equals((String) serverData.get("status"), "success")) {
            SysOut.notice((String) serverData.get("message"));
        } else if (Objects.equals((String) serverData.get("status"), "error")) {
            SysOut.error((String) serverData.get("message"));
        }
        runEditDataProgramm(clientSocket, coos, cois);
    }

    private static void runMainProgramm(Socket clientSocket,
                                        ObjectOutputStream coos,
                                        ObjectInputStream cois) throws IOException, ClassNotFoundException {
        /*отправляет запрос создания соединения*/
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        coos = (coos != null) ? coos : new ObjectOutputStream(clientSocket.getOutputStream());
        cois = (cois != null) ? cois : new ObjectInputStream(clientSocket.getInputStream());

        SysOut.CommandText("Vvedite ID Clienta");
        String idClienta = stdin.readLine();/*считываем id клиента на сервер*/
        HashMap<String, String> clientData = new HashMap<>() {{
            put("main", idClienta);
        }};
        coos.writeObject(clientData);/*отправляем данные содержащие id клиента на сервер*/

        HashMap serverData = (HashMap) cois.readObject();/*принимаем данные о клиенте или об ошибке */
        System.out.println(serverData);
        //и перезагружаем программу если клиента с указанным ID не существует
        if (Objects.equals((String) serverData.get("status"), "success")) {
            SysOut.Title("Klient:");
            System.out.print(SysOut.Color.CYAN_BOLD_BRIGHT);
            System.out.println(serverData.get("value"));
            //и перезагружаем программу если клиента с указанным ID не существует
            System.out.print(SysOut.Color.RESET);

            SysOut.Title("Materiali:");
            System.out.print(SysOut.Color.CYAN_BOLD_BRIGHT);
            System.out.println(cois.readObject());/*принимаем список товаров с сервера*/
            System.out.print(SysOut.Color.RESET);
            SysOut.CommandText("Select the item what you want to buy from the list\n\t('q' ? to complete the selection or item number)\n");
            String clientMessage = stdin.readLine();/*счтитываем что клиент ввел в консоль*/
            SysOut.CommandText("You choose: " + clientMessage);/*сообщаем какой выбор сделал клиент*/
            while (!clientMessage.equals("q")) {
                coos.writeObject(clientMessage);/*отправляем выбор клиента на сервер*/
                SysOut.Description("The cost of the goods is:  " + cois.readObject() + " $");
                //SysOut.notice("Esli vi hotite viiti, vvedite 'q', esli vi hotite povtorit', vvedite chto ugodno");
                SysOut.Description("---------------------------");
                //clientMessage = stdin.readLine();
                //SysOut.Description("Vash vibor: " + clientMessage);
                // if(!clie1ntMessage.equals("q")) {

                //runMainProgramm(clientSocket, stdin, coos, cois);
                //}
                clientMessage = "q";
            }
        } else if (Objects.equals((String) serverData.get("status"), "error")) {
            SysOut.error((String) serverData.get("message"));
            // runMainProgramm(clientSocket, stdin, coos, cois);
        }
    }
}
