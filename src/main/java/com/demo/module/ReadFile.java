package com.demo.module;

import com.demo.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.JarURLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: LinZhaoKang.
 * @Description:
 * @Date Created in 2023/1/4
 * @Modified By:
 */
public class ReadFile {
    private final static String PATH = "D:/Desktop/test/lib";
    private final static String PATH_PRELIMINARY = "D:/Desktop/test/lib/preliminary";
    private final static String PATH_SCREENING = "D:/Desktop/test/lib/screening";
    private String path;
    private String path_preliminary;
    private String path_screening;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath_preliminary() {
        return path_preliminary;
    }

    public void setPath_preliminary(String path_preliminary) {
        this.path_preliminary = path_preliminary;
    }

    public String getPath_screening() {
        return path_screening;
    }

    public void setPath_screening(String path_screening) {
        this.path_screening = path_screening;
    }
/*
    public static void main(String[] args) throws IOException{
        ReadFile readFile = new ReadFile();
        readFile.createCatalogue(PATH_PRELIMINARY);
        readFile.createCatalogue(PATH_SCREENING);
        String[] fileNameLists = readFile.getFilesNameLists(PATH);
        File[] filePathLists = readFile.getFilesPathLists(PATH);
        readFile.moveFileToPreliminary(fileNameLists,filePathLists);
        fileNameLists = readFile.getFilesNameLists(PATH_PRELIMINARY);
        filePathLists = readFile.getFilesPathLists(PATH_PRELIMINARY);
        for (int i = 0; i < fileNameLists.length; i++) {
            boolean isMove = readFile.moveJarToScreening(fileNameLists[i],filePathLists[i]);
            if (isMove){
                System.out.println("移动成功");
            }else{
                System.out.println("移动失败");
            }
        }
    }
 */
    public void init(String path) throws IOException {
        this.path = path;
        this.path_preliminary = path+"/preliminary";
        this.path_screening = path+"/screening";
        createCatalogue(this.path_preliminary);
        createCatalogue(this.path_screening);
    }
    /**
    *@Description 将废弃的jar包移动到screening文件夹
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/11 15:29
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public boolean moveJarToScreening(String fileName, File filePath) throws IOException {
        try {
            if (filePath.exists()) {
                // 创建一个指向jar文件里一个入口的URL
                URL url = new URL("jar:file:/"+filePath.getPath()+"!/Test.class");
                // 读取jar文件
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jarfile = conn.getJarFile();
                // 此时的入口名字应该和指定的URL相同
                String entryName = conn.getEntryName();
                // 得到jar文件的入口
                JarEntry jarEntry = conn.getJarEntry();
                jarfile.close();
                filePath.renameTo(new File(this.getPath_screening(),fileName));
                return true;
            }else{
                return false;
            }

        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException e) {
            if (e.getMessage().contains("not found")){
                System.out.println("大小小于1kb的未废弃文件");
            } else if (e.getMessage().contains("拒绝访问")) {
                System.out.printf("文件夹",e.getMessage());
            }else{
                System.out.println(e);
            }
        }
        return false;
    }

    /**
    *@Description 获取文件夹内所有文件的文件名
    *@Param String
    *@Return java.lang.String[]
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/9 10:28
    */
    public  String[] getFilesNameLists(String filePath) throws IOException{
        File file = new File(filePath);
        //存储文件名的String数组
        String[] fileNameLists = file.list();
        return fileNameLists;

    }
    /**
    *@Description 获取文件夹内所有文件的路径
    *@Param File
    *@Return java.io.File[]
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/11 15:27
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public  File[] getFilesPathLists(String filePath) throws IOException{
        File file = new File(filePath);
        //存储文件路径的String数组
        File[] filePathLists = file.listFiles();
        return filePathLists;
    }
    /**
    *@Description 创建目录
    *@Param void
    *@Return void
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/21 10:15
    */
    public  void createCatalogue(String path) throws IOException {
        File outFile = new File(path+"/test.txt");
        //如果目录不存在则创建目录
        if (!outFile.exists()){
            outFile.getParentFile().mkdirs();
        }
    }
    /**
    *@Description 将文件大小小于1kb的文件移动到preliminary目录
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/21 15:03
    */
    public  boolean moveFileToPreliminary(String[] fileNameLists, File[] filePathLists) throws IOException {
        if (filePathLists!=null&&fileNameLists!=null){
            //第一次处理
            String newPath = this.getPath_preliminary()+"\\";
            for (int i = 0; i < fileNameLists.length; i++) {
                //文件路径拼接
                fileNameLists[i] = newPath.concat(fileNameLists[i]);
                //获取文件大小
                long fileByte = getFileByte(filePathLists[i]);
                //文件小于1kb则有可能是废弃的jar包，可以处理
                if(fileByte<1024){
                    //判断文件是否存在，存在则删除
                    boolean isFileExists = isFileExists(filePathLists[i]);
                    if (isFileExists) {
                        if(!filePathLists[i].isDirectory()) {
                            filePathLists[i].renameTo(new File(fileNameLists[i]));
                        }
                    }
                }
            }
        }
        return true;
    }
    /**
    *@Description 判断文件是否存在，存在就删除，不存在就可以移动
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/21 15:32
    */
    public  boolean isFileExists(File file){
        if (file.exists()){
//            file.delete();
            return true;
        }
        return false;
    }
    /**
    *@Description 获取文件的大小
    *@Param long
    *@Return long
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/13 9:44
    */
    public  long  getFileByte(File file) throws IOException {
        FileInputStream fis  = null;
        long fileByte = 0;
        try {
            if(file.exists() && file.isFile()){
                String fileName = file.getName();
                fis = new FileInputStream(file);
//                System.out.println("文件"+fileName+"的大小是："+fis.available()+"\n");
                fileByte = fis.available();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(null!=fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileByte;
    }
}
