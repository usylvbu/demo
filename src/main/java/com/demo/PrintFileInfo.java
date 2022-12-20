package com.demo;

import com.entity.Loopholes;


import java.io.*;

import java.util.*;


public class PrintFileInfo {
    public static void main(String[] args) throws Exception {
        String path = "D:\\Desktop\\fileinfo.txt";
        PrintFileInfo printFileInfo = new PrintFileInfo();
        List<Loopholes> loopholes = printFileInfo.readFileInfo(path);

        // 封装Map
        Map<String,List<Loopholes>> cacheMap = getMapType(loopholes);
        Set<Map.Entry<String, List<Loopholes>>> entries = cacheMap.entrySet();
        for (Map.Entry<String, List<Loopholes>> entry : entries) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        printFileInfo.outFileInfo(cacheMap);
        //System.out.println(cacheMap);
    }

    private static Map<String, List<Loopholes>> getMapType(List<Loopholes> loopholes) {
        Map<String, List<Loopholes>> cacheMap = new HashMap<>();
        for (Loopholes loophole : loopholes) {
            String type1 = loophole.getType();
            List<Loopholes> loopholesList1 = cacheMap.get(type1);
            if (loopholesList1 == null) {
                loopholesList1 = new ArrayList<>();
                cacheMap.put(type1,loopholesList1);
            }
            loopholesList1.add(loophole);
        }


        return cacheMap;
    }

    public List<Loopholes> readFileInfo(String path){
        List<Loopholes> loopholesList = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path));) {

            String s = bufferedReader.readLine().trim();
            while (s != null && !s.equals("")) {
                Loopholes loopholes = new Loopholes();
                String[] split = s.split(";");
                loopholes.setType(split[0]);
                loopholes.setPath(split[1]);
                String num = split[2].replace("行","");
                loopholes.setLineNum(Integer.valueOf(num));
                loopholesList.add(loopholes);
                s = bufferedReader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loopholesList;
    }

    public void outFileInfo(Map<String,List<Loopholes>> cacheMap) throws Exception {

        int linenumber = 0;
        Set<Map.Entry<String, List<Loopholes>>> entries = cacheMap.entrySet();
        for (Map.Entry<String, List<Loopholes>> entry : entries) {
            List<Loopholes> loopholesList = entry.getValue();
            String path = "D:\\Desktop\\";
            File outFile = new File("D:\\Desktop\\out\\"+entry.getKey()+".txt");
            if (!outFile.exists()){
                outFile.getParentFile().mkdir();
                outFile.createNewFile();
            }else {
                outFile.delete();
            }
            FileWriter fw = new FileWriter("D:\\Desktop\\out\\"+entry.getKey()+".txt", true);
            PrintWriter pw = new PrintWriter(fw);
            for (Loopholes loophole:loopholesList) {
                path = "D:\\Desktop\\";
                path += loophole.getPath();
                linenumber = loophole.getLineNum();
                int nowLineNum = 1;
                pw.println("Type = "+loophole.getType()+"    Path = "+loophole.getPath()+"    LineNum = "+loophole.getLineNum());
                try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path));) {
                    String s = bufferedReader.readLine().trim();
                    while (s != null) {
                        if (linenumber - nowLineNum <= 10&&linenumber - nowLineNum >= -10){
                            if(nowLineNum == linenumber){
                                pw.println("this is "+linenumber+"行"+":");
                            }
                            pw.println(s);
                            pw.flush();
                        }else if (linenumber - nowLineNum < -10){
                            break;
                        }
                        s = bufferedReader.readLine();
                        nowLineNum++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
}
