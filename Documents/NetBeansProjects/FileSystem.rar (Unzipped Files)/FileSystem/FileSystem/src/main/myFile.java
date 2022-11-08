/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.text.*;
import java.util.*;

/**
 *
 * @author vinicius
 */
public class myFile {
    int u, g, a;
    String name;
    String content;
    String creationDate;
    
    myFile (String name, String content)
    {
        // Nome e conteudo do arquivo
        this.name = name;
        this.content = content;
        this.creationDate =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        
        // Permissoes do arquivo
        u = 7;
        g = 7;
        a = 7;
    }

    myFile(String atxts) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public String getName() {
        return name;
    }
    
    public String getContent() {
        return content;
    }
    
    private String reSet(String str, char c, int pos)
    {
        char array[] = str.toCharArray();
        array[pos] = c;
        return new String(array);
    }
    
    public String getDate() {
        return this.creationDate;
    }
    
    public int getU() {
        return this.u;
    }
    
    public int getG() {
        return this.g;
    }
    
    public int getA() {
        return this.a;
    }
    
    public void setU(int u) {
        this.u = u;
    }
    
    public void setG(int g) {
        this.g = g;
    }
    
    public void setA(int a) {
        this.a = a;
    }
    
    public String getPermissions() {
        String chmodU = String.format("%3s", Integer.toBinaryString(u)).replace(' ', '0');
        String chmodG = String.format("%3s", Integer.toBinaryString(g)).replace(' ', '0');
        String chmodA = String.format("%3s", Integer.toBinaryString(a)).replace(' ', '0');;
        
        chmodU = chmodU.replace('0', '-');
        chmodG = chmodG.replace('0', '-');
        chmodA = chmodA.replace('0', '-');
        
        for (int i = 0; i < 3; i++)
        {
            if (i == 0)
            {
                if (chmodU.charAt(i) == '1') chmodU = reSet(chmodU, 'r', i);
                if (chmodG.charAt(i) == '1') chmodG = reSet(chmodG, 'r', i); 
                if (chmodA.charAt(i) == '1') chmodA = reSet(chmodA, 'r', i); 
            }
            else if (i == 1)
            {
                if (chmodU.charAt(i) == '1') chmodU = reSet(chmodU, 'w', i);
                if (chmodG.charAt(i) == '1') chmodG = reSet(chmodG, 'w', i); 
                if (chmodA.charAt(i) == '1') chmodA = reSet(chmodA, 'w', i); 
            }
            else if (i == 2)
            {
                if (chmodU.charAt(i) == '1') chmodU = reSet(chmodU, 'x', i);
                if (chmodG.charAt(i) == '1') chmodG = reSet(chmodG, 'x', i); 
                if (chmodA.charAt(i) == '1') chmodA = reSet(chmodA, 'x', i); 
            }
        }
        
        return "-" + chmodU + chmodG + chmodA;
    }
    
    public myFile copyFile(myFile file)
    {
        return new myFile(file.name, file.content);
    }
}
