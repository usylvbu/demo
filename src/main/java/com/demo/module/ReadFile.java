package com.demo.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ReadFile {
    private final static String PATH = "D:\\Desktop\\test\\lib";
    public static void main(String[] args) throws IOException{
//        Map<String, String> map = getFilesDatas("D:\\Desktop\\test\\lib");
//        for(String key : map.keySet()){
//            String value = map.get(key);
//            System.out.println("文件名："+key+"   内容："+value);
//        }
        getFiles("D:\\Desktop\\test\\lib");
    }

    public static void getFiles(String filePath) throws IOException{
        File file = new File(filePath);
        String[] fileNameLists = file.list(); //存储文件名的String数组
        File[] filePathLists = file.listFiles(); //存储文件路径的String数组
//        for (int i = 0; i < filePathLists.length; i++) {
//            getFileByte(filePathLists[i]);
//        }
        moveFile(fileNameLists,filePathLists);
    }
    /**
    *@Description 创建目录
    *@Param void
    *@Return boolean
    *@Author LinZhaoKang.
    *@Date Created in 2022/12/21 10:15
    */
    public static boolean createCatalogue(String path) throws IOException {
        File outFile = new File(path);
        if (!outFile.exists()){
            outFile.getParentFile().mkdir();
            return true;
        }else {
            return false;
        }
    }
    public static boolean moveFile(String[] fileNameLists, File[] filePathLists) throws IOException {
        if (filePathLists!=null&&fileNameLists!=null){
            String newPath = PATH+"\\preliminary"+"\\"+fileNameLists[0];
            createCatalogue(newPath);
            for (int i = 0; i < fileNameLists.length; i++) {
                fileNameLists[i] = newPath.concat(fileNameLists[i]);
                long fileByte = getFileByte(filePathLists[i]);
                if(fileByte<1024){
                    filePathLists[i].renameTo(new File(fileNameLists[i]));
                }
            }

        }
        return true;
    }
    /**
     * 获取某文件夹下的文件名和文件内容,存入map集合中
     * @param filePath 需要获取的文件的 路径
     * @return 返回存储文件名和文件内容的map集合
     */
    public static Map<String, String> getFilesDatas(String filePath){
        Map<String, String> files = new HashMap<>();
        File file = new File(filePath); //需要获取的文件的路径
        String[] fileNameLists = file.list(); //存储文件名的String数组
        File[] filePathLists = file.listFiles(); //存储文件路径的String数组
        for(int i=0;i<filePathLists.length;i++){
            if(filePathLists[i].isFile()){
                try {//读取指定文件路径下的文件内容
                    String fileDatas = readFile(filePathLists[i]);
                    //把文件名作为key,文件内容为value 存储在map中
                    files.put(fileNameLists[i], fileDatas);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return files;
    }
    /**
     * 读取指定目录下的文件
     * @param path 文件的路径
     * @return 文件内容
     * @throws IOException
     */
    public static String readFile(File path) throws IOException {
        //创建一个输入流对象
        InputStream is = new FileInputStream(path);
        //定义一个缓冲区
        byte[] bytes = new byte[1024];// 1kb
        //通过输入流使用read方法读取数据
        int len = is.read(bytes);
        //System.out.println("字节数:"+len);
        String str = null;
        while(len!=-1){
            //把数据转换为字符串
            str = new String(bytes, 0, len);
            //System.out.println(str);
            //继续进行读取
            len = is.read(bytes);
        }
        //释放资源
        is.close();
        return str;
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
                System.out.println("文件"+fileName+"的大小是："+fis.available()+"\n");
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
