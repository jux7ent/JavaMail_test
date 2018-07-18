package mail.smtp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class ParsePattern {
    private String title, body;
    private String patternFile;
    private Properties properties;

    ParsePattern(String patternFile, Properties properties) throws IOException {
        this.patternFile = patternFile;
        this.properties = properties;

        parsePattern();
    }

    String getTitle() {
        return title;
    }

    String getBody() {
        return body;
    }

    private void parsePattern() throws IOException {
        try (FileInputStream pattern = new FileInputStream(patternFile)) { //можно сделать эффективней
            //конструктор StringBuilder не принимает byte[]
            StringBuilder allFile = new StringBuilder(new String(pattern.readAllBytes()));

            //ищем все вхождения #{fullname}, чтобы их все заменить на нужное значение
            String fullnamePattern = "#{fullname}";
            for (int index = allFile.indexOf(fullnamePattern); index != -1;
                 index = allFile.indexOf(fullnamePattern, index)) {
                allFile.replace(index, index + fullnamePattern.length(),
                        properties.getProperty("fullname"));
            }

            //ищем все вхождения #{phone}, чтобы их все заменить на нужное значение
            String phonePattern = "#{phone}";
            for (int index = allFile.indexOf(phonePattern); index != -1;
                 index = allFile.indexOf(phonePattern, index)) {
                allFile.replace(index, index + phonePattern.length(),
                        properties.getProperty("phone"));
            }

            //title = <title>...</title>  <title>.length = 7
            title = allFile.substring(allFile.indexOf("<title>") + 7,
                    allFile.indexOf("</title>"));

            //body = <body>...</body>
            body = allFile.substring(allFile.indexOf("<body>") + 6,
                    allFile.indexOf("</body>"));
        }
    }
}
