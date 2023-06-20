package com.demo.module;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.JarURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @Author: LinZhaoKang.
 * @Description:
 * @Date Created in 2023/1/4
 * @Modified By: 2023/06/15 16:31
 */
public class ReadFile {
    private final static String PATH = "D:/Desktop/test/lib";
    private final static String PATH_PRELIMINARY = "D:/Desktop/test/lib/preliminary";
    private final static String PATH_SCREENING = "D:/Desktop/test/lib/screening";
    private final static String[] WHITE_PATH = {"keys"};
    private String path;
    private String path_preliminary;
    private String path_screening;
    private String path_contain_version_inf;
    //不需要处理的文件白名单
    private List<String> unMoveFile = null;
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
                System.out.println("移动成功");
            }else{
                System.out.println("移动失败");
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
    *@Description 初始化 path,path_preliminary,path_screening,path_contain_version_inf
    *@Param String
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
                System.out.println(filePath.getPath()+" 这是大小小于1kb的未废弃文件，请手动确认");
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
        if (!outFile.getParentFile().exists()){
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
                if (!fileNameLists[i].endsWith(".jar")){
                    continue;
                }
                //文件路径拼接
                fileNameLists[i] = newPath.concat(fileNameLists[i]);
                //获取文件大小
                long fileByte = getFileByte(filePathLists[i]);
                //文件小于1kb则有可能是废弃的jar包，可以处理
                if(fileByte<1024){
                    //判断文件是否存在preliminary目录，存在则不动
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
    /**
    *@Description 移除jar包内的版本信息(v1)
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
                boolean isFileExists = isFileExists(new File(this.path_contain_version_inf + "/" +
                        filePathLists[i].getName()));
                if (isFileExists) {
                    filePathLists[i].delete();
                }
                Files.copy(filePathLists[i].toPath(), Paths.get(this.path_contain_version_inf + "/" +
                        filePathLists[i].getName()));
                readerJarVersion(filePathLists[i].getPath());
                deleteJarVersionInf(filePathLists[i]);
            }
        }

        return true;
    }
    /**
    *@Description 读取jar包的版本信息
    *@Param String
    *@Return java.lang.String
    *@Author LinZhaoKang.
    *@Date Created in 2023/6/20 20:28
    *@Modified By: LinZhaoKang.
    *@ModifiedDate: Update in
    */
    public String readerJarVersion(String jarFilePath) throws IOException {
        JarFile jarFile = new JarFile(new File(jarFilePath));
        String info = "";
        try {
            String versionMF = "";
            String versionPom = "";
            versionMF = getMFVersion(jarFilePath);
            versionPom = getPomVersion(jarFilePath);
            System.out.println("JarFilePath="+jarFilePath+" versionMF: " + versionMF+" versionPom"+versionPom);
            info = "JarFilePath="+jarFilePath+" versionMF: " + versionMF+" versionPom"+versionPom;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return info;
    }
    /**
    *@Description 获取MF文件中的版本信息
    *@Param String
    *@Return java.lang.String
    *@Author LinZhaoKang.
    *@Date Created in 2023/6/20 20:37
    *@Modified By: LinZhaoKang.
    *@ModifiedDate: Update in
    */
    private String getMFVersion(String jarFilePath) throws IOException {
        JarFile jarFile = new JarFile(new File(jarFilePath));
        String version = "";
        Manifest manifest = jarFile.getManifest();
        if (manifest == null){
            System.out.println("JarFilePath="+jarFilePath+" 此JAR包无版本信息");
            version = "JarFilePath="+jarFilePath+" 此JAR包无版本信息";
            return version;
        }
        Attributes attrs = manifest.getMainAttributes();
        version = attrs.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
        return version;
    }

    /**
    *@Description 读取pom中的版本信息
    *@Param String
    *@Return java.lang.String
    *@Author LinZhaoKang.
    *@Date Created in 2023/6/20 20:29
    *@Modified By: LinZhaoKang.
    *@ModifiedDate: Update in
    */
    private String getPomVersion(String jarFilePath){
        String versionStr = "";
        //项目路径
        String pathStr = System.getProperty("user.dir");
        MavenXpp3Reader mx3Reader = new MavenXpp3Reader();
        String pomPath = jarFilePath + File.separator+"pom.xml";
        pomPath = decodeStr(pomPath);//编码为utf-8
        try{
            Model model = mx3Reader.read(new FileReader(pomPath));
            versionStr = model.getVersion();
        }catch(Exception e){
            e.printStackTrace();
        }
        return versionStr;
    }
    public String decodeStr(String text){
        try{
            text = java.net.URLDecoder.decode(text,"UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        return text;
    }
    /**
    *@Description 删除版本信息文件
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/1/13 15:23
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public boolean deleteJarVersionInf(File file)throws ZipException {
        //比如目录：111/22，所以加了个/
        String delFolder = file.getParent();
        delFolder += "/";
        String zipFile = file.getPath();
        ZipFile zip = new ZipFile(zipFile);
        zip.setFileNameCharset("UTF-8");
//        checkZipFile(zip);
        //先判断要删除的文件夹是否存在
         /*FileHeader dirHeader = zip.getFileHeader(delFolder);
         if (dirHeader==null) {
         System.err.println("没有找到你要删除的文件夹,路径：" + zipFile);
         return;
         }*/
        List<FileHeader> list = zip.getFileHeaders();
        String dname = "";
        boolean isSucess = false;
        for (int i = 0; i < list.size(); i++) {
            // 遍历压缩包中文件目录
            dname = list.get(i).getFileName();
            //System.out.println(dname);

            if (true) {
                if ((dname.startsWith("META-INF/") && dname.contains("maven")) || dname.equals("META-INF/MANIFEST.MF")) {
                    // System.out.println(dname);
                    //if (dname.startsWith(delFolder)) {
                    zip.removeFile(dname);
                    isSucess = true;
                    //解决移位问题
                    i--;
                }
            } else {
             /*if (dname.equals("META-INF/MANIFEST.MF")) {
             zip.removeFile(dname);
             isSucess = true;
             //解决移位问题
             i--;
             }*/
             /*if (dname.startsWith(delFolder)) {
             zip.removeFile(dname);
             isSucess = true;
             //解决移位问题
             i--;
             }*/
            }

        }
        if (!isSucess) {
            System.err.println("未找到需要删除的maven或者META-INF/MANIFEST.MF文件，路径：" + zipFile);
        }
        isSucess = false;
        return true;
    }
    /**
    *@Description 是否含有版本信息文件
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
                // 读取jar文件
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jarfile = conn.getJarFile();
                jarfile.close();
                return true;
            }else {
                return false;
            }
        }catch (IOException e){
            System.out.println(file.getName()+"此jar包已废弃或已修复过了");
            return false;
        }
    }
    /**
    *@Description 删除文件
    *@Param boolean
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2023/4/27 16:25
    *@Modified By: LinZhaoKang.
    *@ModifiedDate:
    */
    public boolean deleteFile(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            file.delete();
            return true;
        }else {
            return false;
        }
    }
    /**
    *@Description 递归获取所有jar文件
    *@Param void
    *@Return void
    *@Author LinZhaoKang.
    *@Date Created in 2023/6/15 16:25
    *@Modified By: LinZhaoKang.
    *@ModifiedDate: 2023/06/15 16:31
    */
    public void listFiles(List<String> files,String dirName){
        listFiles(files,dirName,".jar");
    }
    /**
    *@Description 递归获取指定后缀文件
    *@Param void
    *@Return void
    *@Author LinZhaoKang.
    *@Date Created in 2023/6/15 16:26
    *@Modified By: LinZhaoKang.
    *@ModifiedDate: 2023/06/15 16:31
    */
    public void listFiles(List<String> files,String dirName, String extname){
        File dirFile = new File(dirName);
        if(!dirFile.exists() || (!dirFile.isDirectory())){
        }else{
            File[] tmpfiles = dirFile.listFiles();
            for(int i=0;i<tmpfiles.length;i++){
                File f = tmpfiles[i];
                if(f.isFile()){
                    if(extname==null || f.getName().toLowerCase().endsWith(extname)){
                        files.add(f.getAbsolutePath().replaceAll("\\\\", "/"));
                    }
                }else if(f.isDirectory()){
                    listFiles(files,f.getAbsolutePath().replaceAll("\\\\", "/"),extname);
                }
            }
        }
    }

}
