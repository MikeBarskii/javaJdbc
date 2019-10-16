package com.github.mbarskiy.third_lesson;

import com.github.mbarskiy.ConnectionProperties;
import com.github.mbarskiy.second_lesson.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class BlobExample {
    public static void main(String[] args) throws IOException, SQLException {
        Properties properties = ConnectionProperties.getConnectionProperties();
        String url = properties.getProperty("url");

        try (Connection conn = DriverManager.getConnection(url, properties);
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("CREATE TABLE Images (name VARCHAR(20), d DATE, image BLOB)");

            try (PreparedStatement prepStat =
                         conn.prepareStatement("INSERT INTO Images (name, d, image) VALUES (?, {d ?}, ?)")) {
                String imageName = "smile.jpg";
                BufferedImage image = ImageIO.read(getFileFromResource(imageName));

                Blob smile = conn.createBlob();
                try (OutputStream outputStream = smile.setBinaryStream(1)) {
                    ImageIO.write(image, "jpg", outputStream);
                }

                prepStat.setString(1, "smile");
                prepStat.setDate(2, Date.valueOf("2019-10-16"));
                prepStat.setBlob(3, smile);
                prepStat.execute();

                try (ResultSet resultSet = prepStat.executeQuery("SELECT * FROM Images");) {
                    while (resultSet.next()) {
                        Blob newSmile = resultSet.getBlob("image");
                        BufferedImage newImage = ImageIO.read(newSmile.getBinaryStream());
                        File outputFile = new File("newSmile.jpg");
                        ImageIO.write(newImage, "jpg", outputFile);
                    }
                }
            }
        }
    }

    private static File getFileFromResource(String filename) {
        ClassLoader classLoader = Main.class.getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(filename)).getFile());
    }
}
