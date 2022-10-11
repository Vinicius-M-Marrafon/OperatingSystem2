/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.HashMap;
import java.util.Map;

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
    String u, g, a;
    
    String name, path;
    Node father;
    HashMap<String, Node> dirs; // Mapeia o nome para o diretório. Ex: (.., Pai)
    HashMap<String, File> files; // Mpeia nome => conteudo

    // Node => Representa os diretorios!
    public Node(String name, Node father)
    {
        this.name = name;           // Nome do dir
        this.father = father;       // Pai do dir
        dirs  = new HashMap<>();    // Diretorios dentro do dir
        files = new HashMap<>();    // Arquivos dentro do dir
        
        // Permissoes padrão
        u = "rwx";
        g = "rwx";
        a = "rwx";
    
        // Reference itself
        dirs.put(".", this);        // Adiciona um diretorio '.' que ref. a si mesmo

        // Reference the father
        if (father != null) {
            dirs.put("..", father);     // Adiciona um diretorio '..' que ref. o pai
            this.path = father.path + this.name + "/";
        }
        else {
            dirs.put("..", this);
            this.path = this.name;
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
        
        // Alterar o path do dirs e do file
        // Como?
        
        copy.path = father.path + this.name;
        
        // Recursao
        for (Map.Entry<String, Node> set : copy.dirs.entrySet())
        {
            if (!(set.getKey().equals("..") || set.getKey().equals("."))) {
                // getValue = main.Node@5c1c5f03
                // getKey = b
                //System.out.println("set.GetValue() name: " + set.getValue().name);
                //System.out.println("set.GetValue() path: " + set.getValue().path);
                copy.path += "/";
                
                //System.out.println("copy path: " + copy.path);
                
                Node tmp = set.getValue().copyNode(copy); // Ok!
                copy.dirs.put(set.getKey(), tmp);
            }
        }
        
        return copy;
    }
    
    public String getPermissions() {
        return "d" + u + g + a;
    }
}
