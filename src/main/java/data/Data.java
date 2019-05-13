package data;

import javafx.util.Pair;
import model.Document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Data {

    private static final String headerFile = "/data/header.txt";
    private static final String organizationsFile = "/data/organizations.txt";
    private static final String unitsFile = "/data/units.txt";
    private static final String postsFile = "/data/posts.txt";

    private ArrayList <String> getArrayList(String path) throws IOException {
        FileInputStream stream = new FileInputStream(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        ArrayList <String> arrayList = new ArrayList <>();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            arrayList.add(line);
        }

        bufferedReader.close();

        return arrayList;
    }

    public Pair <ArrayList <String>, ArrayList <String>> getHeader(Document document) {
        ArrayList <String> organizations = new ArrayList <>();
        ArrayList <String> units = new ArrayList <>();

        try {
            FileInputStream stream = new FileInputStream(this.getClass().getResource(headerFile).getPath());
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(stream));
            document.setNumber(bufferedReader.readLine());
            document.setOCUD(bufferedReader.readLine());
            document.setOCPO(bufferedReader.readLine());

            organizations = getArrayList(this.getClass().getResource(organizationsFile).getPath());
            units = getArrayList(this.getClass().getResource(unitsFile).getPath());
        }

        catch (Exception exception){
            exception.printStackTrace();
        }

        return new Pair <> (organizations, units);
    }

    public ArrayList <String> getFooter() {
        ArrayList <String> posts = new ArrayList <>();

        try {
            posts = getArrayList(this.getClass().getResource(postsFile).getPath());
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }

        return posts;
    }

}
