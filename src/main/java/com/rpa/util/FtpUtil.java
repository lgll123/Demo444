package com.rpa.util;

import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FtpUtil {

    public static String downloadFileAsString(String server, int port, String user, String pass, String remoteDir) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        // 获取目录下的文件名
        String[] files = ftpClient.listNames(remoteDir);
        if (files == null || files.length == 0) {
            throw new FileNotFoundException("FTP目录下没有文件: " + remoteDir);
        }

        // 这里假设目录中只有一个文件
        String remoteFilePath = files[0];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ftpClient.retrieveFile(remoteFilePath, outputStream);

        ftpClient.logout();
        ftpClient.disconnect();

        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
