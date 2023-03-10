package com.demo.module;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.JarURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
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
    private String path_contain_version_inf;

    public String getPath_contain_version_inf() {
        return path_contain_version_inf;
    }

    public void setPath_contain_version_inf(String path_contain_version_inf) {
        this.path_contain_version_inf = path_contain_version_inf;
    }

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
                System.out.println("????????????");
            }else{
                System.out.println("????????????");
            }
        }
    }
 */
//    public static void main(String[] args) throws IOException, ZipException {
//        ReadFile readFile = new ReadFile();
//        readFile.init(PATH);
//        File[] filesPathLists = readFile.getFilesPathLists(PATH);
//        readFile.removeJarVersionInf(filesPathLists);
//
//    }
    /**
    *@Description ?????????
    *@Param void
    *@Return void
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/12 10:12
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public void init(String path) throws IOException {
        this.path = path;
        this.path_preliminary = path+"/preliminary";
        this.path_screening = path+"/screening";
        this.path_contain_version_inf = path+"/contain_version_inf";
        createCatalogue(this.path_preliminary);
        createCatalogue(this.path_screening);
        createCatalogue(this.path_contain_version_inf);
    }
    /**
    *@Description ????????????jar????????????screening?????????
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
                // ??????????????????jar????????????????????????URL
                URL url = new URL("jar:file:/"+filePath.getPath()+"!/Test.class");
                // ??????jar??????
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jarfile = conn.getJarFile();
                // ???????????????????????????????????????URL??????
                String entryName = conn.getEntryName();
                // ??????jar???????????????
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
                System.out.println("????????????1kb??????????????????");
            } else if (e.getMessage().contains("????????????")) {
                System.out.printf("?????????",e.getMessage());
            }else{
                System.out.println(e);
            }
        }
        return false;
    }

    /**
    *@Description ??????????????????????????????????????????
    *@Param String
    *@Return java.lang.String[]
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/9 10:28
    */
    public  String[] getFilesNameLists(String filePath) throws IOException{
        File file = new File(filePath);
        //??????????????????String??????
        String[] fileNameLists = file.list();
        return fileNameLists;

    }
    /**
    *@Description ???????????????????????????????????????
    *@Param File
    *@Return java.io.File[]
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/11 15:27
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public  File[] getFilesPathLists(String filePath) throws IOException{
        File file = new File(filePath);
        //?????????????????????String??????
        File[] filePathLists = file.listFiles();
        return filePathLists;
    }
    /**
    *@Description ????????????
    *@Param void
    *@Return void
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/21 10:15
    */
    public  void createCatalogue(String path) throws IOException {
        File outFile = new File(path+"/test.txt");
        //????????????????????????????????????
        if (!outFile.getParentFile().exists()){
            outFile.getParentFile().mkdirs();
        }
//        else{
//            boolean isDeleteDir = deleteDir(outFile.getParentFile());
//            if (isDeleteDir) {
//                System.out.println("???????????????"+path);
//            }else{
//                System.out.println("???????????????");
//            }
//            createCatalogue(path);
//        }
    }
    /*
    private static boolean deleteDir(File dir) {

        if (dir.isDirectory()) {
            String[] children = dir.list();
            //?????????????????????
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // ?????????????????????????????????
        return dir.delete();
    }
     */
    /**
    *@Description ?????????????????????1kb??????????????????preliminary??????
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/21 15:03
    */
    public  boolean moveFileToPreliminary(String[] fileNameLists, File[] filePathLists) throws IOException {
        if (filePathLists!=null&&fileNameLists!=null){
            //???????????????
            String newPath = this.getPath_preliminary()+"\\";
            for (int i = 0; i < fileNameLists.length; i++) {
                //??????????????????
                fileNameLists[i] = newPath.concat(fileNameLists[i]);
                //??????????????????
                long fileByte = getFileByte(filePathLists[i]);
                //????????????1kb????????????????????????jar??????????????????
                if(fileByte<1024){
                    //??????????????????????????????????????????
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
    *@Description ?????????????????????????????????????????????????????????????????????
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/21 15:32
    */
    public  boolean isFileExists(File file){
        if (file.exists()){
            return true;
        }
        return false;
    }
    /**
    *@Description ?????????????????????
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
//                System.out.println("??????"+fileName+"???????????????"+fis.available()+"\n");
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
    /**
    *@Description ??????jar?????????????????????(v1)
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/13 15:22
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public boolean removeJarVersionInf(File[] filePathLists) throws IOException, ZipException {
        for (int i = 0; i < filePathLists.length; i++) {
            boolean isContain = isContainVersionInf(filePathLists[i]);
            if (isContain) {
                boolean isFileExists = isFileExists(new File(this.path_contain_version_inf + "/" + filePathLists[i].getName()));
                if (isFileExists) {
                    filePathLists[i].delete();
                }
                Files.copy(filePathLists[i].toPath(), Paths.get(this.path_contain_version_inf + "/" + filePathLists[i].getName()));
                deleteJarVersionInf(filePathLists[i]);
            }
        }

        return true;
    }
    /**
    *@Description ????????????????????????
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/13 15:23
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public boolean deleteJarVersionInf(File file)throws ZipException {
        //???????????????111/22??????????????????/
        String delFolder = file.getParent();
        delFolder += "/";
        String zipFile = file.getPath();
        ZipFile zip = new ZipFile(zipFile);
        zip.setFileNameCharset("UTF-8");
//        checkZipFile(zip);
        //??????????????????????????????????????????
         /*FileHeader dirHeader = zip.getFileHeader(delFolder);
         if (dirHeader==null) {
         System.err.println("????????????????????????????????????,?????????" + zipFile);
         return;
         }*/
        List<FileHeader> list = zip.getFileHeaders();
        String dname = "";
        boolean isSucess = false;
        for (int i = 0; i < list.size(); i++) {
            // ??????????????????????????????
            dname = list.get(i).getFileName();
            //System.out.println(dname);

            if (true) {
                if ((dname.startsWith("META-INF/") && dname.contains("maven")) || dname.equals("META-INF/MANIFEST.MF")) {
                    // System.out.println(dname);
                    //if (dname.startsWith(delFolder)) {
                    zip.removeFile(dname);
                    isSucess = true;
                    //??????????????????
                    i--;
                }
            } else {
             /*if (dname.equals("META-INF/MANIFEST.MF")) {
             zip.removeFile(dname);
             isSucess = true;
             //??????????????????
             i--;
             }*/
             /*if (dname.startsWith(delFolder)) {
             zip.removeFile(dname);
             isSucess = true;
             //??????????????????
             i--;
             }*/
            }

        }
        if (!isSucess) {
            System.err.println("????????????????????????maven??????META-INF/MANIFEST.MF??????????????????" + zipFile);
        }
        isSucess = false;
        return true;
    }
    /**
    *@Description ??????????????????????????????
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/13 15:23
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    private boolean isContainVersionInf(File file) {
        try {
            if (file.exists()&&!file.isDirectory()){
                URL url = new URL("jar:file:/"+file.getPath()+"!/META-INF/MANIFEST.MF");
                // ??????jar??????
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jarfile = conn.getJarFile();
                jarfile.close();
                return true;
            }else {
                return false;
            }
        }catch (IOException e){
            System.out.println(file.getName()+"???jar??????????????????????????????");
            return false;
        }
    }
}
