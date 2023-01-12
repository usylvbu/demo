package com.demo.tools;

import com.demo.module.ReadFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * @Author: LinZhaoKang.
 * @Description:
 * @Date Created in 2023-01-11 16:17
 * @Modified By: LinZhaoKang.
 */
public class JarFilterTool {
    public static void main(String[] args) throws IOException {
        JarFilterTool jarFilterTool = new JarFilterTool();
        ReadFile readFile = new ReadFile();
        String directoriesPath = jarFilterTool.getDirectoriesPath();
        readFile.init(directoriesPath);
        jarFilterTool.outObsoleteJarsFilter(readFile);
        System.exit(1);
    }

    public String getDirectoriesPath(){
        // 创建一个JFrame组件为parent组件
        JFrame frame = new JFrame();
        // 创建一个默认打开用户文件夹的问价选择器
        JFileChooser chooser = new JFileChooser();
        //设置只能选择目录
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int flag = chooser.showOpenDialog(frame);
        //若选择了文件，则打印选择了什么文件
        if (flag == JFileChooser.APPROVE_OPTION) {
            System.out.println("用户选择了文件：" + chooser.getSelectedFile().getPath());
        }
        return chooser.getSelectedFile().getPath();
    }
    public boolean outObsoleteJarsFilter(ReadFile readFile) throws IOException {
        String[] fileNameLists = readFile.getFilesNameLists(readFile.getPath());
        File[] filePathLists = readFile.getFilesPathLists(readFile.getPath());
        readFile.moveFileToPreliminary(fileNameLists,filePathLists);
        fileNameLists = readFile.getFilesNameLists(readFile.getPath_preliminary());
        filePathLists = readFile.getFilesPathLists(readFile.getPath_preliminary());
        for (int i = 0; i < fileNameLists.length; i++) {
            boolean isMove = readFile.moveJarToScreening(fileNameLists[i],filePathLists[i]);
            if (isMove){
                System.out.println("移动成功");
            }else{
                System.out.println("移动失败");
            }
        }
        return true;
    }
}
