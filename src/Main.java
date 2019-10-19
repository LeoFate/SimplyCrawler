import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        //this means the file named "crawler_result.txt" will be saved in C:\Users\$your_user_name
        String fileName = "C:\\Users\\" + System.getProperty("user.name") + "\\crawler_result.txt";
        try {
            for (CrawlerFactory.Detail detail : CrawlerFactory.fetchDetailLink()) {
                CrawlerFactory.writeDetail(detail, fileName);
            }
        } catch (IOException e) {
            //Here we just let the exception be printed in console
            e.printStackTrace();
        }
    }
}
