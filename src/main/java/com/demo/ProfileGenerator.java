package com.demo;

import com.entity.Loopholes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProfileGenerator {
    public static void main(String[] args) {
        ProfileGenerator ProfileGenerator = new ProfileGenerator();
        ProfileGenerator.UpLoadFile("上传文件");
    }

    public void UpLoadFile(String title) {
        JFrame jframe = new JFrame(title);// 实例化一个JFrame
        JPanel jPanel = new JPanel(); // 创建一个轻量级容器
        JToolBar jToolBar = new JToolBar(); // 提供了一个用来显示常用的 Action 或控件的组件
        jframe.setVisible(true);// 可见
        jframe.setSize(500, 500);// 窗体大小
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// close的方式
        jframe.setContentPane(jPanel); // 设置 contentPane 属性。
        JLabel jl = new JLabel("请选择：");// 创建一个Label标签
        jl.setHorizontalAlignment(SwingConstants.LEFT);// 样式，让文字居中
        jPanel.add("North", jl);// 将标签添加到容器中
        jPanel.add("North", jToolBar);
        JButton developer = new JButton("上传文件");
        developer.setHorizontalAlignment(SwingConstants.CENTER);
        jToolBar.add(developer);// 上传文件按钮添加到容器
        jPanel.add("North", jToolBar);
        developer.addMouseListener(new MouseAdapter() { // 添加鼠标点击事件
            public void mouseClicked(MouseEvent event) {
                eventOnImport(new JButton());
            }
        }); // 文件上传功能
    }

    /**
     * 文件上传功能
     *
     * @param developer
     *            按钮控件名称
     */
    public static void eventOnImport(JButton developer) {
        JFileChooser chooser = new JFileChooser();
        Random random = new Random();
        chooser.setMultiSelectionEnabled(true);
        /** 过滤文件类型 * */
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "xml", "txt", "doc", "docx");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(developer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            /** 得到选择的文件* */
            File[] arrfiles = chooser.getSelectedFiles();
            if (arrfiles == null || arrfiles.length == 0) {
                return;
            }
            FileInputStream input = null;
            FileOutputStream out = null;
            try {
                for (File f : arrfiles) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
                    String s = bufferedReader.readLine().trim();
                    String path = "./out/"+random.toString()+".xml";
                    createFile(path);
                    FileWriter fw = new FileWriter(path, true);
                    PrintWriter pw = new PrintWriter(fw);
                    while (s != null && !s.equals("")) {
                        String[] split = s.split(";");
                        writeFile(pw,split);
                        s = bufferedReader.readLine();
                    }
                }
                JOptionPane.showMessageDialog(null, "上传成功！", "提示",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 文件生成
     */
    public static void  createFile(String path) throws IOException {
        File outFile = new File(path);
        if (!outFile.exists()){
            outFile.getParentFile().mkdir();
            outFile.createNewFile();
        }else {
            outFile.delete();
        }
    }
    /**
     * 写入xml文件
     */
    public static void writeFile(PrintWriter pw,String[] split){
        pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        pw.println("<root>");
        pw.println("<host-list>");
        for (int i = 0; i < split.length; i++) {
            pw.println("<host>");
            pw.println(split[i]);
            pw.println("</host>");
        }
        pw.println("</host-list>");
        pw.println("</root>");

    }
}
