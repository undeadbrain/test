package Basket;
/*код программы сервера*/

import java.io.*;
import java.lang.reflect.Field;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.System.setIn;

class ServerFirst {
    protected static HashMap<String, ArrayList<String>> _docs = new HashMap<>();
    protected static Boolean needToLogging = false;

    protected static ArrayList<User> _users = new ArrayList<User>();

    protected static ArrayList<Material> _materials = new ArrayList<Material>();

    protected static ArrayList<String> _entities = new ArrayList<>(Arrays.asList("users", "materials"));

    protected static char newline = '\n';

    public static void main(String[] arg) throws IOException, IllegalAccessException {
        ServerSocket serverSocket = null;
        Socket clientAccepted = null;
        ObjectInputStream sois = null;
        ObjectOutputStream soos = null;

        readDocs(_entities);

        try {
            serverSocket = new ServerSocket(2525);

            System.out.println("server starting....");
            System.out.println("at IP=" + InetAddress.getLocalHost().getHostAddress());
            System.out.println("at port=" + serverSocket.getLocalPort());

            /*clientAccepted = serverSocket.accept();*/
            clientAccepted = serverSocket.accept();/*сервер ждет*/

            System.out.println("connection established....");
            System.out.println("with IP=" + clientAccepted.getInetAddress());
            System.out.println("at port=" + clientAccepted.getPort());

            sois = new ObjectInputStream(clientAccepted.getInputStream());
            soos = new ObjectOutputStream(clientAccepted.getOutputStream());

            HashMap clientData = (HashMap) sois.readObject(); /*сервер получает данные с клиента*/

            if (clientData.containsKey("main")) {
                System.out.println("Client's number\n: " + clientData.get("main")); /*выводим id клиента*/
                Boolean existing = false;
                for (User user : _users) {
                    if (user.getID().equals(clientData.get("main"))) {
                        User userIn = new User(user.getName(), user.getID(), user.getPhone());
                        String userString = userIn.toString();
                        existing = true;

                        soos.writeObject(new HashMap<>() {{
                            put("status", "success");
                            put("value", userString);
                        }});

                        System.out.println("ID Client\n?: " + user.getID());
                        System.out.println("Full name client: " + user.getName());
                        System.out.println("Phone number client:" + user.getPhone());
                        System.out.println("--------------------------------------------");
                    }
                }

                if (existing) {
                    soos.writeObject(_materials.toString());

                    String clientMessageRecieved = (String) sois.readObject();/*принимаем выбор клиента*/
                    while (!clientMessageRecieved.equals("q")) {
                        System.out.println("Client chose: " + clientMessageRecieved);/*пишем в консоль выбор клиента выбор клиента*/
                        for (Material material : _materials) {
                            if (material.getNameMaterial().equals(clientMessageRecieved)) {
                                soos.writeObject(material.getPrice());
                            }
                        }
                        clientMessageRecieved = (String) sois.readObject();
                    }
                } else {
                    soos.writeObject(new HashMap<>() {{
                        put("status", "error");
                        put("message", "Clienta s ID: " + clientData.get("main") + " ne sushestvuet");
                    }});
                }
            } else if (clientData.containsKey("delete")) {
                HashMap deleteData = (HashMap) clientData.get("delete");
                if (deleteData.containsKey("entity") && deleteData.containsKey("fieldId")) {
                    Boolean userExist = false;
                    int counter = 0;

                    if (Objects.equals(deleteData.get("entity"), "users")) {
                        for (User user : _users) {
                            if (user.getID().equals(deleteData.get("fieldId"))) {
                                userExist = true;
                                _users.remove(counter);
                                prepareFileDataAndWrite((String) deleteData.get("entity"), _users.toArray());
                                break;
                            }
                            counter++;
                        }
                    } else if (Objects.equals(deleteData.get("entity"), "materials")) {
                        for (Material material : _materials) {
                            int fieldId = Integer.parseInt((String) deleteData.get("fieldId"));
                            if (material.getIdMaterial() == fieldId) {
                                userExist = true;
                                _materials.remove(counter);
                                prepareFileDataAndWrite((String) deleteData.get("entity"), _materials.toArray());
                                break;
                            }
                            counter++;
                        }
                    }

                    if (!userExist) {
                        soos.writeObject(new HashMap<>() {{
                            put("status", "error");
                            put("message", "Zapisi s ID (" + deleteData.get("fieldId") + ") v tablice " + deleteData.get("entity") + " ne sushestvuet");
                        }});
                        System.out.println("Zapisi s takim ID net");
                    } else {
                        soos.writeObject(new HashMap<>() {{
                            put("status", "success");
                            put("message", "Zapisi s ID (" + deleteData.get("fieldId") + ") v tablice " + deleteData.get("entity") + " udalena");
                        }});
                    }

                } else {
                    System.out.println("Nelorrectnii dannii");
                }
            } else if (clientData.containsKey("add")) {
                HashMap addData = (HashMap) clientData.get("add");

                if (Objects.equals(addData.get("entity"), "users")) {
                    String name = (String) addData.get("name");
                    String id = (String) addData.get("id");
                    String phone = (String) addData.get("phone");
                    _users.add(new User(name, id, phone));
                    prepareFileDataAndWrite((String) addData.get("entity"), _users.toArray());
                    soos.writeObject(new HashMap<>() {{
                        put("status", "success");
                        put("message", "Zapisi (id: " + id + ", name: " + name + ", phone: " + phone + ") dobavlena v tablicu " + (String) addData.get("entity"));
                    }});
                    System.out.println("Zapisi s takim ID net");
                } else if (Objects.equals(addData.get("entity"), "materials")) {
                    int idMaterial = Integer.parseInt((String) addData.get("fieldId"));
                    String materialName = (String) addData.get("materialName");
                    int price = Integer.parseInt((String) addData.get("materialPrice"));
                    int dateOfDelivery = Integer.parseInt((String) addData.get("materialDate"));
                    _materials.add(new Material(idMaterial, materialName, price, dateOfDelivery));
                    prepareFileDataAndWrite((String) addData.get("entity"), _materials.toArray());
                    soos.writeObject(new HashMap<>() {{
                        put("status", "success");
                        put("message", "Zapisi (id: " + idMaterial + ", materialName: " + materialName + ", price: " + price + ", dateOfDelivery:" + dateOfDelivery + ") dobavlena v tablicu " + (String) addData.get("entity"));

                    }});
                    System.out.println("Zapisi s takim ID net");
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error");
        } finally {
            try {
                if (sois != null) sois.close();
                if (soos != null) soos.close();
                if (clientAccepted !=
                        null) clientAccepted.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                System.out.println("Resources are not closed!!!");
            }
        }
    }

    private static void readDocs(ArrayList<String> fileNames) throws IOException, IllegalAccessException {
        for (String fileName : fileNames) {
            ArrayList<String> fields = getFileDataFromCsv(fileName + ".csv");
            //_docs.put(fileName, fields);
            if (Objects.equals(fileName, "users")) {
                for (String field : fields) {
                    //name.add
                    String[] parts = field.split(";");
                    _users.add(new User(parts));
                }
            }
            if (Objects.equals(fileName, "materials")) {
                for (String field : fields) {
                    //name.add
                    String[] parts = field.split(";");
                    _materials.add(new Material(parts));
                }
            }
            if (needToLogging) {
                System.out.println("Full name client: " + fileName);
                System.out.println("ID Client: " + fields.size());
                System.out.println("Phone number client: " + fields);
            }
        }
    }

    static void writeDocs(String entity, String data) throws IOException {
        String filePath = "./res/" + entity + ".csv";
        File file = new File(filePath);

        if (!file.exists() && !file.isDirectory()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        if (file.exists() && !file.isDirectory()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(data);

            writer.close();
        }
    }

    private static ArrayList<String> getFileDataFromCsv(String filePath) throws IOException {
        ArrayList<String> fileLines = new ArrayList<>();
        filePath = "./res/" + filePath;
        File file = new File(filePath);

        if (file.exists() && !file.isDirectory()) {
            int counter = 0;

            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                setIn(fileInputStream);
                byte[] fileData = System.in.readAllBytes();
                String str = new String(fileData);

                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == newline) {
                        fileLines.add(str.substring(counter, i));
                        counter = i + 1;
                    } else if (i == str.length() - 1) {
                        fileLines.add(str.substring(counter, i));
                    }
                }
            }
        }

        return fileLines;
    }

    private static void prepareFileDataAndWrite(String entity, Object[] fields) throws IOException, IllegalAccessException {
        ArrayList<String> strFields = new ArrayList<>();
        for (Object field : fields) {
            strFields.add(String.join(";", objToArrayStrings(field)));
        }

        writeDocs(entity, String.join("\n", strFields));
    }

    public static String[] objToArrayStrings(Object obj) throws IllegalAccessException {

        ArrayList<String> arrayString = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null) {
                arrayString.add("" + value);
            }
        }
        String[] array = new String[arrayString.size()];
        return arrayString.toArray(array);

    }

}

