package com.demo;

import com.entity.Loopholes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class OutDeleteFileBat {

    public static void main(String[] args) throws IOException {
        String path = "D:\\Desktop\\fileinfo.txt";
        PrintFileInfo printFileInfo = new PrintFileInfo();
        List<Loopholes> loopholes = printFileInfo.readFileInfo(path);
        OutDeleteFileBat outDeleteFileBat = new OutDeleteFileBat();
        outDeleteFileBat.deleteFileBat(loopholes);
        outDeleteFileBat.deleteFileSh(loopholes);
        outDeleteFileBat.outDeleteFilePath(loopholes);
    }

    public void deleteFileBat(List<Loopholes> loopholes) throws IOException {
//        File outFile = new File("D:\\Desktop\\delete.bat");
//        String path = "";
//        if (!outFile.exists()){
//            outFile.getParentFile().mkdir();
//            outFile.createNewFile();
//        }else {
//            outFile.delete();
//        }
        String path = "D:\\Desktop\\delete.bat";
        createFile(path);
        FileWriter fw = new FileWriter(path, true);
        PrintWriter pw = new PrintWriter(fw);
        for (Loopholes loophole:loopholes) {
            path = "D:/e9/";
            path = loophole.getPath();
            path = path.replace("/","\\");
            pw.println("del " +"\"" + path + "\"" );
//            pw.println("pause");
            pw.flush();
        }
        pw.println("pause");
    }

    public void deleteFileSh(List<Loopholes> loopholes) throws IOException {
//        File outFile = new File("D:\\Desktop\\delete.sh");
//        String path = "";
//        if (!outFile.exists()){
//            outFile.getParentFile().mkdir();
//            outFile.createNewFile();
//        }else {
//            outFile.delete();
//        }
        String path = "D:\\Desktop\\delete.sh";
        createFile(path);
        FileWriter fw = new FileWriter(path, true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println("dir=$(pwd)");
        for (Loopholes loophole:loopholes) {
            path = loophole.getPath();
//            path = path.replace("/","\\");
            pw.println("path=\"${dir}/"+path+"\"");
            pw.println("echo \"delete path=$path\" ");
            pw.println("rm -rf $path");
            pw.flush();
        }
    }
    public void outDeleteFilePath(List<Loopholes> loopholes) throws IOException {
        String path = "D:\\Desktop\\filepath.txt";
        createFile(path);
        FileWriter fw = new FileWriter(path, true);
        PrintWriter pw = new PrintWriter(fw);
        for (Loopholes loophole:loopholes) {
            path = loophole.getPath();
            pw.println(path);
            pw.flush();
        }
    }
    public void createFile(String path) throws IOException {
        File outFile = new File(path);
        if (!outFile.exists()){
            outFile.getParentFile().mkdir();
            outFile.createNewFile();
        }else {
            outFile.delete();
        }
    }
}
