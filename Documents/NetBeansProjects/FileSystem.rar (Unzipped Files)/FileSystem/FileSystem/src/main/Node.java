/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.*;

/**
 *
 * @author vinic
 * 
 * Descricão da Classe Node
 *      Classe que representa a estrutura dos diretórios:
 *      Cada diretório (Node da Árvore) terá:
 *          1) Uma Tabela Hash Para os diretórios filhos
 *          2) Uma Tabela Hash Para os arquivos que o mesmo contêm
 *          3) Um Node que referencia o Diretório Pai
 *          4) Um nome!
 */
public class Node
{
    // Permissões do diretorio / Arquivo
    int u, g, a;
    
    String name, path, creationDate;
    Node father;
    HashMap<String, Node> dirs; // Mapeia o nome para o diretório. Ex: (.., Pai)
    HashMap<String, myFile> files; // Mpeia nome => conteudo

    // Node => Representa os diretorios!
    public Node(String name, Node father)
    {
        this.name = name;           // Nome do dir
        this.father = father;       // Pai do dir
        this.creationDate =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        dirs  = new HashMap<>();    // Diretorios dentro do dir
        files = new HashMap<>();    // Arquivos dentro do dir
        
        // Permissoes padrão
        u = 7;
        g = 7;
        a = 7;
    
        // Referencia a si mesmo
        dirs.put(".", this);
        
        // Referencia o pai
        if (father != null) {
            dirs.put("..", father);
            this.path = father.path + "/" + this.name;
        }
        else {
            this.father = this;
            dirs.put("..", this);
            this.path = "";
        }
    }
   
    
    public Node copyNode(Node father)
    {
        // OK!
        Node copy = new Node(this.name, father);
        
        copy.dirs  = new HashMap<>(this.dirs);
        copy.files = new HashMap<>(this.files);
        
        copy.dirs.put(".", copy);
        copy.dirs.put("..", father);
        
        copy.path = father.path + this.name;
        
        // Recursao
        for (Map.Entry<String, Node> set : copy.dirs.entrySet())
        {
            if (!(set.getKey().equals("..") || set.getKey().equals(".")))
            {
                copy.path += "/";
                
                Node tmp = set.getValue().copyNode(copy); // Ok!
                copy.dirs.put(set.getKey(), tmp);
            }
        }
        
        return copy;
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
    
    public String getPermissions()
    {
        String chmodU = String.format("%3s", Integer.toBinaryString(u)).replace(' ', '0');
        String chmodG = String.format("%3s", Integer.toBinaryString(g)).replace(' ', '0');
        String chmodA = String.format("%3s", Integer.toBinaryString(a)).replace(' ', '0');
        
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
        
        return "d" + chmodU + chmodG + chmodA;
    }
}
