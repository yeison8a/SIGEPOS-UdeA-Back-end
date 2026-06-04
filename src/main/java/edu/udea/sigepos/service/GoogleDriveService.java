package edu.udea.sigepos.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class GoogleDriveService {

    public File descargarPlantilla() throws IOException {

        String fileId =
                "1ebJCqznM4wW3DC7DjBPkkF2AW1tLj-lo";

        String url =
                "https://docs.google.com/document/d/"
                        + fileId
                        + "/export?format=docx";

        File destino = File.createTempFile(
                "plantilla",
                ".docx"
        );

        try (
                InputStream in = new URL(url).openStream();
                FileOutputStream out =
                        new FileOutputStream(destino)
        ) {

            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }

        System.out.println("Archivo descargado: " + destino.getAbsolutePath());
        System.out.println("Tamaño archivo: " + destino.length());

        return destino;
    }
}