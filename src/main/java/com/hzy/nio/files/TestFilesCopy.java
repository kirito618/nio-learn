package com.hzy.nio.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 使用Files类实现文件拷贝
 * 如果目标路径下已经存在对应的文件 那么会抛异常
 * */
public class TestFilesCopy {

    public static void main(String[] args) throws IOException {
        //源文件夹路径
        String resource = "F:\\java\\nioTestDirectory\\source";
        String target = "F:\\java\\nioTestDirectory\\target";
        Files.walk(Paths.get(resource)).forEach(path -> {
            String targetName = path.toString().replace(resource,target);
            try {
            if (Files.isDirectory(Paths.get(path.toString()))){
                //说明当前文件是一个文件夹
                Files.createDirectories(Paths.get(targetName));
            }else if (Files.isRegularFile(Paths.get(path.toString()))){
                //说明当前是个文件，那么就拷贝到新的目录下面去
                //第一个参数是来源的Path对象，第二个是目标文件夹的Path对象
                    Files.copy(path,Paths.get(targetName));
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
