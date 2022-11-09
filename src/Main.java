import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args){
        try (Scanner sc = new Scanner(new FileReader(args[0]))) {
            while (sc.hasNextLine()) {  //按行读取字符串
                String line = sc.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
