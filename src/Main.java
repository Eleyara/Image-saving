import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        String siteLink = "https://lenta.ru";
        try {
            Document doc = Jsoup.connect(siteLink).get(); //Получаем html код сайта в Document
            Elements images = doc.select("img"); //Получаем набор элементов по тегу
            BufferedImage image = null;
            for (Element element : images) {
                String imageLink = element.attr("abs:src");//Получаем абсолютную ссылку на картинку
                if(imageLink.substring(imageLink.length() - 4).equals(".jpg")) { //Проверяем, что ссылка относится к файлу в формате jpg
                    image = ImageIO.read(new URL(imageLink));
                    writeImageIfNotNull(image, getFileNameFromLink("images", element.attr("abs:src"), "/"));//Если файла с именем картинки не существует, создаем и записываем его
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getFileNameFromLink(String folder, String link, String separator) {
        String filename = folder + "/" + link.substring(link.lastIndexOf(separator) + 1);
        System.out.println(filename);
        return  filename;
    }

    public static void writeImageIfNotNull(BufferedImage image, String path){
        if(image != null && !Files.exists(Paths.get(path))){
            try {
                Path filePath = Files.createFile(Paths.get(path));
                ImageIO.write(image, "jpg", filePath.toFile());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
}