package compile.il;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nachogarrone on 7/9/16.
 */
public class Logger {

    public static void log(String key, String message) {
        Writer writer = null;
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        System.out.println(simpleDateFormat.format(date) + ": " + key + " : " + message);

        try {

            File file = new File("log.txt");

            //if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file, true);
            writer = new BufferedWriter(fileWriter);
            writer.write(simpleDateFormat.format(date) + ": " + key + " : " + message + System.lineSeparator());
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }
}
