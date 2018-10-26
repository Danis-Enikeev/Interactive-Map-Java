package Controller;

import java.io.*;
import java.util.HashMap;

public class Config {
    static private HashMap<String, String> settings;
    private File file;
    private BufferedReader br;
    private BufferedWriter bw;

    public Config(String path) {
        this.file = new File(path);
        settings = new HashMap<String, String>();
        this.Read();
    }

    public Config() {
        settings = new HashMap<String, String>();
        settings.put("Developer mode", "0");
        settings.put("Login", "Ivan Ivanov");
        settings.put("Password", "12345");
        settings.put("Rank", "user");
        settings.put("Autotests", "0");
        // settings.put
        this.file = new File("config");
        try {
            this.bw = new BufferedWriter(new FileWriter(this.file));
            try {
                for (HashMap.Entry<String, String> pair : settings.entrySet()) {
                    bw.write(pair.getKey() + ":" + pair.getValue() + "\r\n");
                }
            } catch (Exception e) {
                System.err.println("Error writing to config file");
            }
        } catch (IOException e) {
            System.err.println("Error opening config file for writing");
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException ex) {
                System.err.println("Error closing the config file!");
            }
        }
    }

    static public HashMap<String, String> getSettings() {
        return settings;
    }

    public void Read() {
        try {
            this.br = new BufferedReader(new FileReader(this.file));
            String thisLine;
            try {
                while ((thisLine = br.readLine()) != null) {
                    String[] stringArray = thisLine.split(":", 2);
                    settings.put(stringArray[0], stringArray[1]);
                }
            } catch (Exception e) {
                System.err.println("Error reading from the config file");
            }
        } catch (FileNotFoundException e) {
            Config configBuff = new Config();
            this.settings = configBuff.getSettings();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                System.err.println("Error closing the config file");
            }
        }
    }

    public void Write(String key, String value) {
        settings.put(key, value);
        try {
            this.bw = new BufferedWriter(new FileWriter(this.file));
            try {
                for (HashMap.Entry<String, String> pair : settings.entrySet()) {
                    bw.write(pair.getKey() + ":" + pair.getValue() + "\r\n");
                }
            } catch (Exception e) {
                System.err.println("Error writing to the config file");
            }
        } catch (IOException e) {
            System.err.println("Error opening the config file for writing");
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException ex) {
                System.err.println("Error closing the config file");
            }
        }
    }
}
