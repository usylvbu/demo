package com.demo.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @Author: LinZhaoKang.
 * @Description:
 * @Date Created in 2023/1/4
 * @Modified By:
 */
public class ReadFile {
    private final static String PATH = "D:\\Desktop\\test\\lib";
    private final static String PATH_PRELIMINARY = "D:\\Desktop\\test\\lib\\preliminary";
    private final static String PATH_SCREENING = "D:\\Desktop\\test\\lib\\screening";
    public static void main(String[] args) throws IOException{
        String[] fileNameLists = getFilesNameLists(PATH);
        File[] filePathLists = getFilesPathLists(PATH);
        moveFileToPreliminary(fileNameLists,filePathLists);
        fileNameLists = getFilesNameLists(PATH_PRELIMINARY);
        filePathLists = getFilesPathLists(PATH_PRELIMINARY);
        for (int i = 0; i < fileNameLists.length; i++) {
            boolean isMove = moveFileToScreening(fileNameLists[i],filePathLists[i]);
            if (isMove){
                System.out.println("成功");
            }else{
                System.out.println("失败");
            }
        }
    }
    /**
    *@Description 获取文件夹内所有文件的文件名
    *@Param String
    *@Return java.lang.String[]
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/9 10:28
    */
    public static String[] getFilesNameLists(String filePath) throws IOException{
        File file = new File(filePath);
        //存储文件名的String数组
        String[] fileNameLists = file.list();
        return fileNameLists;

    }
    public static File[] getFilesPathLists(String filePath) throws IOException{
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
    public static void createCatalogue(String path) throws IOException {
        File outFile = new File(path+"test.txt");
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
    public static boolean moveFileToPreliminary(String[] fileNameLists, File[] filePathLists) throws IOException {
        if (filePathLists!=null&&fileNameLists!=null){
            //第一次处理前缀
            String newPath = PATH+"\\preliminary"+"\\";
            createCatalogue(newPath);
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
                        filePathLists[i].renameTo(new File(fileNameLists[i]));
                    }
                }
            }
        }
        return true;
    }
    /**
    *@Description 将废弃的jar包移动到screening目录
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/9 13:48
    */
    public static boolean moveFileToScreening(String fileName, File file) throws IOException {
        try {
            URL url=new URL("jar:file:"+PATH+"\\preliminary\\"+fileName+"!/Test.class");
            InputStream is=url.openStream();
            byte[] b =new byte[1000];
            is.read(b);
            is.close();
            createCatalogue(PATH_SCREENING+"\\");
            Path sDir = Paths.get(PATH_PRELIMINARY, file.getName());
            Path tDir = Paths.get(PATH_SCREENING, fileName);
            Files.move(sDir,tDir,StandardCopyOption.REPLACE_EXISTING);
            return true;
        }catch (Exception e){
            System.out.println(file.exists());
            file.renameTo(new File(PATH,fileName));
            System.out.println("这是正在使用的jar包");
        }
        return false;
    }
    /**
    *@Description 判断文件是否存在，存在就删除，不存在就可以移动
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/21 15:32
    */
    public static boolean isFileExists(File file){
        if (file.exists()){
            file.delete();
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
    public static long  getFileByte(File file) throws IOException {
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
