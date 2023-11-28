package uk.ac.ed.inf;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class directory {


        public static void main(){
            Path resultfiles = Paths.get("resultFiles/");
            try {
                Files.createDirectory(resultfiles);
                System.out.println("Directory created");
            } catch (IOException ignored) {
                System.out.println("Directory found");
            }
        }

}
