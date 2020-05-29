package hp.inc.jsg.qa.stf.framework.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/************************************************************
 *  © Copyright 2019 HP Development Company, L.P.
 *  SPDX-License-Identifier: MIT
 *
 *  Smart Test Framework
 ************************************************************/
public class InputOutputHelper {

    /***
     * Helper to write a string content into a file.
     * It will close the file when finishing to write it.
     * @param content : The content itself.
     * @param fullFilename : The filename and its path (localization)
     * @throws IOException
     */
    public static void writeFile(String content, String fullFilename) throws IOException {
        List<String> tempList = new ArrayList<>();
        tempList.add(content);
        writeFile(tempList, fullFilename);
    }

    /***
     * Helper to write a string list into a file where each entry will be written per line.
     * It will close the file when finishing to write it.
     * @param content : The content itself.
     * @param fullFilename : The filename and its path (localization)
     * @throws IOException
     */
    public static void writeFile(List<String> content, String fullFilename) throws IOException {
        File myNewFile = new File(fullFilename);
        FileOutputStream outputStream = new FileOutputStream(myNewFile);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        for (int i = 0; i < content.size(); i++) {
            bufferedWriter.write(content.get(i));
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    /***
     * Helper that creates a directory.
     * @param absoluteNewDirectoryPath: The full path.
     * @return
     * @throws IOException
     */
    public static String createDirectory(String absoluteNewDirectoryPath) throws IOException {
        Path path = Paths.get(absoluteNewDirectoryPath);
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        return absoluteNewDirectoryPath;
    }

    /***
     * This helper write a string into an already created file, leaving it open for further new entries.
     * @param bufferedWriter: Buffered writer.
     * @param content : The content itself.
     * @throws IOException
     */
    public static void writeFileOnTheFly(BufferedWriter bufferedWriter, String content) throws IOException {
        List<String> tempList = new ArrayList<>();
        tempList.add(content);
        writeFileOnTheFly(bufferedWriter, tempList);
    }

    /***
     * This helper write a list into an already created file, leaving it open for further new entries.
     * @param bufferedWriter: Buffered writer.
     * @param content : The content itself.
     * @throws IOException
     */
    public static void writeFileOnTheFly(BufferedWriter bufferedWriter, List<String> content) throws IOException {
        for (int i = 0; i < content.size(); i++) {
            bufferedWriter.write(content.get(i));
            bufferedWriter.newLine();
        }
    }

    /***
     * This helper will append content into an existent file - in a new line.
     * @param content : The content itself.
     * @param fullFilename : The filename and its path (localization)
     * @throws IOException
     */
    public static void appendContentToFile(String content, String fullFilename) throws IOException {
        List<String> tempList = new ArrayList<>();
        tempList.add(content);
        appendContentToFile(tempList, fullFilename);
    }

    /**
     * This helper will append content into an existent file - in a new line.
     *
     * @param content      : The content itself.
     * @param fullFilename : The filename and its path (localization)
     * @throws IOException
     */
    public static void appendContentToFile(List<String> content, String fullFilename) throws IOException {
        BufferedWriter bufferedWriter;
        FileWriter fileWriter;
        File existentFile = new File(fullFilename);
        fileWriter = new FileWriter(existentFile.getAbsoluteFile(), true);
        bufferedWriter = new BufferedWriter(fileWriter);

        for (int i = 0; i < content.size(); i++) {
            bufferedWriter.write(content.get(i));
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        fileWriter.close();
    }

    /***
     * Simple helper that reads the content from a text file.
     * @param fullFilename: The filename and its path (localization)
     * @return
     * @throws IOException
     */
    public static String readContentFromFile(String fullFilename) throws IOException {
        return readContentFromFile(fullFilename, Charset.defaultCharset());
    }

    /***
     * Helper that will read a txt file and return its content as string.
     * @param fullFilename : The filename and its path (localization).
     * @param charset : The encoding that should be used during the conversion.
     * @return
     * @throws IOException
     */
    public static String readContentFromFile(String fullFilename, Charset charset) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(fullFilename));
        return new String(encoded, charset);
    }

    /**
     * Convert a input stream into string.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    //region To Be Deprecate


    //endregion

}