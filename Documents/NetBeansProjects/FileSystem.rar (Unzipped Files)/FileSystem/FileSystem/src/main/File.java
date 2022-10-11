/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author vinicius
 */
public class File {
    String u, g, a;
    String name;
    String content;
    
    File (String name, String content)
    {
        // Nome e conteudo do arquivo
        this.name = name;
        this.content = content;
        
        // Permissoes do arquivo
        u = "rwx";
        g = "rwx";
        a = "rwx";
    }
    
    public String getName() {
        return name;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getPermissions() {
        return "-" + u + g + a;
    }
    
    public File copyFile(File file)
    {
        return new File(file.name, file.content);
    }
}
