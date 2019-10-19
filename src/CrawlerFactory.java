import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerFactory {
    /**
     * @param url the url link that needs to crawl
     * @return the web page of the specified link
     * @throws IOException to be handled, not in this method
     */
    private static String crawl(String url) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    /**
     * @return the name&detail page links list of teachers
     * @throws IOException to be handled, not in this method
     */
    static List<Detail> fetchDetailLink() throws IOException {
        String source = crawl(Source.contentUrl);
        StringBuilder stringBuilder = new StringBuilder();
        Matcher matcher = Pattern.compile("(?<=class=\"ul_jiaoshou\")[\\S\\s]*?(?=</ul>)").matcher(source);
        while (matcher.find()) {
            stringBuilder.append(matcher.group());
        }
        source = stringBuilder.toString();
        matcher = Pattern.compile("href=\"(.*?)\".*?title=\"(.*?)\"[\\S\\s]*?").matcher(source);
        List<Detail> detailList = new ArrayList<>();
        while (matcher.find()) {
            String url = matcher.group(1);
            String name = matcher.group(2);
            if (url.contains("..")) {
                url = url.replace("..", "http://maths.tju.edu.cn");
                detailList.add(new Detail(name, url, true));
            } else {
                detailList.add(new Detail(name, url, false));
            }
        }
        return detailList;
    }

    /**
     * @param detail   detail message
     * @param fileName specify which file we need to write in
     * @throws IOException to be handled, not in this method
     */
    static void writeDetail(Detail detail, String fileName) throws IOException {
        if (detail.format && Source.isInList(detail.name)) {
            String source = crawl(detail.url);
            Matcher matcher = Pattern.compile("v_news_content[\\S\\s]*?div_vote_id").matcher(source);
            if (matcher.find())
                source = matcher.group();
            String regexTemp1 = ".*?<p>(.*?)</p>[\\S\\s]*?";
            String regex1 = "职称" + regexTemp1 + "电子邮箱" + regexTemp1 + "办公地点" + regexTemp1;
            String regexTemp2 = "</h3>[\\s\\S]*?>([\\s\\S]*?)<";
            String regex2 = "研究方向" + regexTemp2;
            matcher = Pattern.compile(regex1 + regex2).matcher(source);
            FileWriter fileWriter = new FileWriter(fileName, true);
            fileWriter.write(detail.name);
            fileWriter.write(System.lineSeparator());
            if (matcher.find()) {
                fileWriter.write("职称：" + matcher.group(1));
                fileWriter.write(System.lineSeparator());
                fileWriter.write("电子邮箱：" + matcher.group(2));
                fileWriter.write(System.lineSeparator());
                fileWriter.write("办公地点：" + matcher.group(3));
                fileWriter.write(System.lineSeparator());
                fileWriter.write("研究方向：" + matcher.group(4));
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.close();
        }
    }

    /**
     * struct that used to store name and correspond url
     */
    static class Detail {
        private String name;
        private String url;
        /**
         * true when the root url is http://maths.tju.edu.cn
         */
        private boolean format;

        Detail(String name, String url, boolean format) {
            this.name = name;
            this.url = url;
            this.format = format;
        }
    }
}
