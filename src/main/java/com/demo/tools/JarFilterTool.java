package com.demo.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.module.ReadFile;
import net.lingala.zip4j.exception.ZipException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LinZhaoKang.
 * @Description:
 * @Date Created in 2023-01-11 16:17
 * @Modified By: LinZhaoKang.
 */
public class JarFilterTool {
    private TextArea ta = new TextArea();
    public static void main(String[] args) throws IOException, ZipException {

//    System.out.println("欢迎使jar包扫描工具");
//    System.out.println("*****preliminary目录为仍保存版本信息的jar包备份*****");
//    System.out.println("*****screening为被废弃的jar包备份*****");
//    System.out.println("*****contain_version_inf是仍保存版本信息的jar包备份*****");
//    try{
//        JarFilterTool jarFilterTool = new JarFilterTool();
//        ReadFile readFile = new ReadFile();
//        String directoriesPath = jarFilterTool.getDirectoriesPath();
//        readFile.init(directoriesPath);
//        jarFilterTool.outObsoleteJarsFilter(readFile);
//        readFile.removeJarVersionInf(readFile.getFilesPathLists(directoriesPath));
//    }catch (Exception e){
//        System.out.println(e);
//        System.exit(1);
//    }
//    System.exit(1);
    JarFilterTool jarFilterTool = new JarFilterTool();
    jarFilterTool.createJFrame();
}

    /**
    *@Description 扫描jar包功能
    *@Param void
    *@Return void
    *@Author LinZhaoKang.
    *@Date Created in 2023/2/10 14:13
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public void scanJarFile(){
        System.out.println("*****欢迎使jar包扫描工具****");
        System.out.println("*****preliminary目录为仍保存版本信息的大小为1kb的jar包备份*****");
        System.out.println("*****screening为被废弃的jar包备份*****");
        System.out.println("*****contain_version_inf是仍保存版本信息的jar包备份*****");
        try{
            JarFilterTool jarFilterTool = new JarFilterTool();
            ReadFile readFile = new ReadFile();
            String directoriesPath = jarFilterTool.getDirectoriesPath();
            //初始化路径，生成目录
            readFile.init(directoriesPath);
            //筛选废弃jar包
            jarFilterTool.outObsoleteJarsFilter(readFile);
            //移除版本信息
            readFile.removeJarVersionInf(readFile.getFilesPathLists(directoriesPath));
        }catch (Exception e){
            System.out.println(e);
            //如果报错，强行停止程序
//            System.exit(1);
        }
        System.exit(0);
    }
    /**
    *@Description 创建面板
    *@Param void
    *@Return void
    *@Author LinZhaoKang.
    *@Date Created in 2023/2/10 14:13
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public void createJFrame(){
        Frame fr = new Frame("Hello");
        //卡片局部
        //显示的面板
        fr.setSize(800,800);
        fr.setBackground(Color.white);
        fr.setLayout(new FlowLayout());
        fr.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        ta.setEnabled(false);
        Button button = new Button();
        button.setLabel("One Click Processing");
        button.setName("One Click Processing");
        button.setSize(200,200);
//        button.setBounds(pan.getWidth()/2, pan.getHeight()/2,200,50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scanJarFile();
            }
        });
        Button buttonByScanJarVersion = new Button();
        buttonByScanJarVersion.setLabel("Scan JAR Version");
        buttonByScanJarVersion.setName("Scan JAR Version");
        buttonByScanJarVersion.setSize(200,200);
//        buttonByScanJarVersion.setBounds(button.getX(),button.getY()+60,200,50);
        buttonByScanJarVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("*****欢迎使jar包扫描工具****");
                ta.append("**********欢迎使jar包扫描工具*********\n");
                ta.append("**********以下为jar包版本信息*********\n");
                System.out.println("**********以下为jar包版本信息*********");
                scanJarVersion();
                System.out.println("********************************************************************************");
                ta.append("********************************************************************************\n");
            }
        });
        Button buttonByClear = new Button();
        buttonByClear.setLabel("Clear");
        buttonByClear.setName("Clear");
        buttonByClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.setText("");
            }
        });
        fr.add(button);
        fr.add(buttonByScanJarVersion);
        fr.add(buttonByClear);
        fr.add(ta);
//        fr.pack();
//        fr.add(pan);

        fr.setVisible(true);
    }
    public void scanJarVersion(){
        try {
            String  filePath = getDirectoriesPath();
            ReadFile readFile = new ReadFile();
            List<String> fileList = new ArrayList<>();
            readFile.listFiles(fileList,filePath);
            for (String path:fileList) {
                ta.append(readFile.readerJarVersion(path)+"\n");
            }

        }catch (Exception e){
            System.out.println("报错信息:"+e.getMessage());
        }
    }
    /**
    *@Description 可视化窗口读取jar包文件夹
    *@Param String
    *@Return java.lang.String
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/12 9:54
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
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
//        frame.setDefaultCloseOperation(2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return chooser.getSelectedFile().getPath();
    }
    /**
    *@Description 筛选废弃jar包
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/12 9:55
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public boolean outObsoleteJarsFilter(ReadFile readFile) throws IOException {
        String[] fileNameLists = readFile.getFilesNameLists(readFile.getPath());
        File[] filePathLists = readFile.getFilesPathLists(readFile.getPath());
        readFile.moveFileToPreliminary(fileNameLists,filePathLists);
        fileNameLists = readFile.getFilesNameLists(readFile.getPath_preliminary());
        filePathLists = readFile.getFilesPathLists(readFile.getPath_preliminary());
        if (filePathLists!=null&&fileNameLists!=null) {
            for (int i = 0; i < fileNameLists.length; i++) {
                if (!fileNameLists[i].endsWith(".jar")){
                    continue;
                }
                boolean isMove = readFile.moveJarToScreening(fileNameLists[i], filePathLists[i]);
                if (isMove) {
                    System.out.println(fileNameLists[i]+"移动成功");
                } else {
                    System.out.println(fileNameLists[i]+"移动失败");
                }
            }
            File file = new File(readFile.getPath_preliminary());
            if(file.isDirectory()){
                if(file.list().length>0){
                    fileNameLists = readFile.getFilesNameLists(readFile.getPath_preliminary());
                    filePathLists = readFile.getFilesPathLists(readFile.getPath_preliminary());
                    for (int i = 0; i < fileNameLists.length; i++) {
//                        filePathLists[i].renameTo(new File(readFile.getPath(),fileNameLists[i]));
                        System.out.println(filePathLists[i].toPath()+","+Paths.get(readFile.getPath()
                                + "/" + filePathLists[i].getName()));
                        if (readFile.deleteFile(Paths.get(readFile.getPath() + "/" +
                                filePathLists[i].getName()).toString())) {
                            Files.copy(filePathLists[i].toPath(), Paths.get(readFile.getPath() + "/"
                                    + filePathLists[i].getName()));
                        }else{
                            System.out.println("源目录中存在文件，请手动删除,文件路径:"+Paths.get(readFile.getPath()
                                    + "/" + filePathLists[i].getName()));
                        }

                    }
                }else{
                    System.out.println(file.getName()+"目录为空!");
                }
            }else{
                System.out.println(file.getName()+"这不是一个目录!");
            }
        }
        return true;
    }
    /**
    *@Description 判断目录是否为空
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/2/6 17:19
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public boolean isDirectoryEmpty(String filePath){
        File file = new File(filePath);
        if(file.isDirectory()){
            if(file.list().length>0){
                System.out.println("目录不为空!");
                return false;
            }else{
                System.out.println("目录为空!");
                return true;
            }
        }else{
            System.out.println("这不是一个目录!");
            return true;
        }
    }
}
