package com.greencross.lims.util;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PdfUtils {
    private static final String BASE_DIR = "D:\\0. 작업공간\\결과지 서명, 로고 업데이트 건\\패널\\지놈";
    public static PDDocument byteArrayToPDDocument(byte[] pdfBytes) throws IOException {
        try (var bais = new java.io.ByteArrayInputStream(pdfBytes)) {
            return PDDocument.load(bais);
        }
    }
    public static void saveAndOpenPdf(PDDocument doc, String testCode) throws IOException {
        String fileName = String.format("[%s].pdf", testCode);
        Path dir = Path.of(BASE_DIR);
        Files.createDirectories(dir);
        File pdfFile = dir.resolve(fileName).toFile();
        doc.save(pdfFile);

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(pdfFile);
        } else {
            openPdfInDefaultViewer(pdfFile);
        }
    }

    public static void openPdfInDefaultViewer(File file) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        if (System.getenv("OS") != null && System.getenv("OS").contains("Windows")) {
            runtime.exec("rundll32 url.dll,FileProtocolHandler " + file.getAbsolutePath());
        } else {
            runtime.exec("xdg-open " + file.getAbsolutePath());
        }
    }

    public static void saveShortFormText(String text, String testCode) throws IOException {
        String fileName = String.format("[%s] 서술형결과지_짧은 버전.txt", testCode);
        Path filePath = Path.of(BASE_DIR, fileName);
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, text);
    }
    public static void saveLongFormText(String text, String testCode) throws IOException {
        String fileName = String.format("[%s] 서술형결과지_긴 버전.txt", testCode);
        Path filePath = Path.of(BASE_DIR, fileName);
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, text);
    }
}
