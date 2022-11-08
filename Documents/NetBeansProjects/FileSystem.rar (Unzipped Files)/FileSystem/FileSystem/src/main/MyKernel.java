package main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import operatingSystem.Kernel;

/**
 * Kernel desenvolvido pelo aluno. Outras classes criadas pelo aluno podem ser
 * utilizadas, como por exemplo: - Arvores; - Filas; - Pilhas; - etc...
 *
 * @author Vinicius Mari Marrafon
 */

import main.Exceptions.PathNotFoundException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.*;
import java.io.*;


public class MyKernel implements Kernel {

    // Variáveis Auxiliares
    Node pwd;   // Diretório atual da navegação
    Node home;  // Diretório home (%HOMEDIR% || /)
    
    public MyKernel() {
        // OK!
        // Criação do root ('/' com pai = null)
        home = new Node("/", null);
        pwd = home;
    }
    
    // Métodos Auxiliares para a implementação
    
    // Retorna um Array de String que possui o nome de todos os caminhos
    // especificados em str utilizando '/' para efetuar tal divisão!
    private String[] countChar(String str, char target, boolean ignoreLast) {
        if (str.equals("")) {
            return null;
        }
        
        int count = 0;
        
        char[] arr = str.toCharArray();
        int n = arr.length;
        
        // Ignorar o ultimo '/'
        if (arr[n - 1] == target && ignoreLast)
            --n;
        
        // System.out.println("Array.lenght = " + arr.length);

        // Contar a quantida de barra
        for (int i = 0; i < n; i++)
            if (arr[i] == target)
                count++;

        String paths[] = str.split(target + "", count + 1);
        for (int i = 0, m = paths.length; i < m; i++)
            paths[i] = paths[i].replace(target + "", "");
    
        return paths;
    }
    
    // Checar string
    private boolean ok(String reGex, String str)
    {
        Pattern pattern = Pattern.compile(reGex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
   
    
    private boolean isFather(String dirName, String chil) {
        System.out.println(dirName + " -> " + chil + " ?");
        return chil.contains(dirName);
    }
    
    private String setPermission(Node source, int u, int g, int a, String fileName)
    {
        // OK!
        myFile target = source.files.get(fileName);
        
        try {
            target.a = a;
            target.u = u;
            target.g = g;
        }
        catch (NullPointerException ex) {
            return "File do not exist!";
        }
        
        return "chmod: Success";
    }
    
    private void setPermission(Node source, int u, int g, int a, boolean recursive)
    {
        source.a = a;
        source.u = u;
        source.g = g;
        
        if (recursive) {
            for (Map.Entry<String, Node> set : source.dirs.entrySet()) {
                if (!(set.getKey().equals(".") || set.getKey().equals(".."))) {
                    this.setPermission(set.getValue(), u, g, a, recursive);
                }
            }
            
            for (Map.Entry<String, myFile> set : source.files.entrySet()) {
                this.setPermission(source, u, g, a, set.getValue().getName());
            }
        }
    }
    
    // Retorna o ponteiro para o nó da árvore de diretorios
    // Ou retorna o diretorio de um arquivo especificado
    private Node getPathByName(String path, int until) throws PathNotFoundException
    {
        String s[] = this.countChar(path, '/', true);
        int steps = s.length;
        int start = 0;
        
        Node destiny;
        if (s[0].equals("")) {
            destiny = home;
            start++;
        }
        else {
            destiny = pwd;
        }
        
        for (int i = start; i < steps - until; i++)
        {
            if (destiny.dirs.get(s[i]) == null)
                throw new PathNotFoundException("cd : Cannot find path \'"+path+"\' because it does not exist.");
            
            destiny = destiny.dirs.get(s[i]);
            // System.out.println("New step: " + destiny.path);
        }
        return destiny;
    }
    
    private void updatePath(Node source) {
        // Para cada filho de source
        
        for (Map.Entry<String, Node> n : source.dirs.entrySet()) {
            if (!(n.getKey().equals(".") || n.getKey().equals(".."))) {
                n.getValue().path = source.path + "/" + n.getValue().name;
                this.updatePath(n.getValue());
            }
        }
    }
    
    private String dfsStore(String f, Node u) {
        // Começa pelo root
        String store = "";
        String currentPath = u.path;
        if (u != home) store = "mkdir " + u.path + "\n";
        if (!u.getPermissions().equals("drwxrwxrwx")) {
            String value = u.getU()+""+u.getG()+""+u.getA();
            store += "chmod " + value + " " + currentPath;
        }
        
        for (Map.Entry<String, Node> set : u.dirs.entrySet())
        {
            if (!(set.getKey().equals("..") || set.getKey().equals(".")))
            {
                // System.out.println("Key: " + set.getValue().path);
                store += this.dfsStore(f, set.getValue());
            }
        }
        
        for (Map.Entry<String, myFile> set : u.files.entrySet())
        {
            if (!(set.getKey().equals("..") || set.getKey().equals(".")))
            {
                // System.out.println("Key: " + set.getValue().path);
                store += "createfile " + currentPath + "/" + u.files.get(set.getKey()).getName() + " " + u.files.get(set.getKey()).getContent()+ "\n";
            }
        }
        
        return store;
    }
            
    // Retorna o nome do diretório que o usuário esta!
    @Override public String whereami() {
        return "Where am I: " + pwd.path;
    }
    
    @Override public String sudo() {
        return null;
    }
    // OK!
    @Override public String ls(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        
        String args[] = this.countChar(parameters, ' ', true);
        
        Node walker;
        boolean per = false;
        
        if (args != null) {
            int argc = args.length;
            if (argc == 1) {
               if (args[0].toLowerCase().equals("-l")) {
                   walker = pwd;
                   per = true;
               }
               else {
                    try {
                    walker = this.getPathByName(args[0], 0);
                }
                catch (PathNotFoundException ex) {
                    return ex.getMessage();
                }
                }
            }
            else if (argc == 2) {
                if (args[0].toLowerCase().equals("-l")) {
                    per = true;
                }
                else {
                    return "ls: Invalid flag for ls " + args[0] + ", try \"-l\"";
                }
                
                try {
                    walker = this.getPathByName(args[1], 0);
                }
                catch (PathNotFoundException ex) {
                    return ex.getMessage();
                }
            }
            else {
                return "ls: The syntax of command is incorrect!";
            }
        } 
        else {
            walker = pwd;
        }
        
        // Tem perimissao?
        if (walker.getU() < 4 || walker.getA() < 4 || walker.getG() < 4) {
            return "ls: Can't list! Access Denied. Are you root?";
        }
        
        result += "ls: directory " + walker.path + "\n";
        for (Map.Entry<String, Node> set : walker.dirs.entrySet()) {
            result += (per? set.getValue().getPermissions()+'\t'+set.getValue().getDate() + "\t": " ") + set.getKey() + "\n";
        }
        
        for (Map.Entry<String, myFile> set : walker.files.entrySet()) {
            result += (per? set.getValue().getPermissions()+'\t'+set.getValue().getDate() + "\t": " ") + set.getKey() + "\n";
        }
        
        //fim da implementacao do aluno
        return result;
    }

    @Override public String mkdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        // System.out.println("Chamada de Sistema: mkdir");
        // System.out.println("\tParametros: " + parameters);
        
        if (parameters.equals("")) {
            result =  "Usage: mkdir [dir_name]";
        }
        else
        {
            //inicio da implementacao do aluno
            String[] token = this.countChar(parameters, '/', true);
            int num = token.length;
            int start = 0;

            // Consider that the path is absolute by default
            Node walker;
            try {
                walker = this.getPathByName(parameters, 1);
            }
            catch (PathNotFoundException ex) {
                return ex.getMessage();
            }
            
            String chmodU = String.format("%3s", Integer.toBinaryString(walker.getU())).replace(' ', '0');
            String chmodG = String.format("%3s", Integer.toBinaryString(walker.getG())).replace(' ', '0');
            String chmodA = String.format("%3s", Integer.toBinaryString(walker.getA())).replace(' ', '0');
        
            // Tem perimissao?
            if (chmodU.charAt(1) == '0' || chmodG.charAt(1) == '0' || chmodA.charAt(1) == '0') {
                return "mkdir: Can't write in this directory! Access Denied. Are you root?";
            }
        
            Node newDir;
            // restricoes para criar um diretorio
            if (ok("[ <>\"\'?:~|\\/!@#$%¨&*]", token[num-1]) || walker.dirs.get(token[num-1]) != null)
                // newDir = new Node(walker.name + token[num - 1], walker);
                return "Invalid Directory";
            else
                // checar se o nome do diretorio eh valido
                // newDir = new Node(walker.name + "/" + token[num - 1], walker);
                newDir = new Node(token[num - 1], walker);
            
            // newDir = new Node(token[num - 1], walker);
            walker.dirs.put(token[num - 1], newDir);
        }
        
        //fim da implementacao do aluno
        return result;
    }

    // Need to be sharped!
    @Override public String cd(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        if (parameters.equals("")) {
            return "";
        }
        
        String result = "";
        try {
            pwd = this.getPathByName(parameters, 0);
        }
        catch (PathNotFoundException ex) {
            return ex.getMessage();
        }
        
        operatingSystem.fileSystem.FileSytemSimulator.currentDir = pwd.path;
        return result;
    }

    // Em manutenção
    @Override public String rmdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        
        // Validações: Se o diretório deve estar vazio, então ele não pode ser parente
        // de pwd!
        if (parameters.equals("")) {
            result = "The syntax of command is incorrect";
        }
        else if (parameters.equals("..") || parameters.equals(".")) {
            result = "The directory can't be removed";
        }
        else
        {
            String[] token = this.countChar(parameters, '/', true);
            int num = token.length;
            
            Node walker;
            try {
                walker = this.getPathByName(parameters, 0);
            }
            catch (PathNotFoundException ex) {
                return ex.getMessage();
            }
            
            // System.out.println("Walker info\nSize Dir: " + walker.dirs.size() + "\nSize Files: " + walker.files.size());
            // Checar se walker esta vazio
            if (walker == pwd) {
                result = "Your current directory cannot be removed!";
            }
            else if (walker.dirs.size() == 2 && walker.files.isEmpty()) {
                walker.father.dirs.remove(token[num - 1]);
            }
            else {
                result = "The directory " + walker.path + " is not empty!";
            }
        }
        
        //fim da implementacao do aluno
        return result;
    }

    // Pode receber 3 parametros, mas não menos que 2!
    // Origem e destino
    // Flag -R para especificar a copia de um diretorio
    
    // Nao copia arquivo
    @Override public String cp(String parameters) {
        
        if (parameters.equals("")) {
            return "The syntax of the command is incorrect.";
        }
        
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";

        //inicio da implementacao do aluno
        
        // Checar quantos argumentos foram passados
        String[] args = this.countChar(parameters, ' ', true);
        int argc = args.length;
        int start = 0;
        int s1 = 0, s2 = 0;
        boolean dirCp = false;
        boolean f1 = false, f2 = false;
        
        // Verificar se a copia eh de um diretorio!
        if (argc == 3 && args[0].toLowerCase().equals("-r")) {
            start++;
            dirCp = true;
        }
        
        String[] arg1 = this.countChar(args[start], '/', true);
        String[] arg2 = this.countChar(args[start + 1], '/', true);
        int n1 = arg1.length;
        int n2 = arg2.length;
        
        Node source, destiny;
        
        try {
            source = this.getPathByName(args[start], 0);
            destiny = this.getPathByName(args[start + 1], 0);
        }
        catch (PathNotFoundException ex) {
            return ex.getMessage();
        }
        
        if (dirCp && (source == destiny.father)) {
            return "Can't copy";
        }
        
        if (dirCp) {
            // System.out.println("Destiny's name " + destiny.name);
            Node cpDir = source.copyNode(destiny);
            destiny.dirs.put(arg1[n1 - 1], cpDir);
            destiny.dirs.put(".", destiny);
        }
        else {
            if (source.files.get(arg1[n1-1]) != null) {
                System.out.println("File to copy: " + arg1[n1-1]);
                myFile target = source.files.get(arg1[n1-1]);
                myFile cpFile = target.copyFile(target);
                
                String destinyFileName;
                if (f2) {
                    if (!arg2[n2-1].contains(".txt")) {
                        arg2[n2-1] += ".txt";
                    }
                    destinyFileName = arg2[n2-1];
                } else {
                    destinyFileName = arg1[n1-1];
                }
                destiny.files.put(destinyFileName, cpFile);
            }
            else {
                return "The system cannot find the path specified.";
            }
        }
        
        //fim da implementacao do aluno
        return result;
    }

    @Override public String mv(String parameters)
    {
        String result = "";
        //inicio da implementacao do aluno
        // Ir até o arquivo de origem e destino
        
        String[] args = this.countChar(parameters, ' ', true);
        int argc = args.length;
        int s1 = 0, s2 = 0;
        boolean f1 = false, f2 = false, rename = false;
        
        if (argc != 2) {
            return "The syntax of the command is incorrect.";
        }
        
        String[] arg1 = this.countChar(args[0], '/', true);
        String[] arg2 = this.countChar(args[1], '/', true);
        int n1 = arg1.length;
        int n2 = arg2.length;
        
        Node source, destiny;
        String renameDir = "";
        
        // Checar origem
        if (arg1[0].equals("")) {
            source = home;
            s1++;
        }
        else {
            source = pwd;
        }
        
        // Checar destino
        if (arg2[0].equals("")) {
            destiny = home;
            s2++;
        }
        else {
            destiny = pwd;
        }
        
        // Podemos ter arquivos misturado com diretorios (Tratar)
        for (int i = s1; i < n1; i++)
        {
            if (arg1[i].contains(".txt")) {
                f1 = true;
            }
            else if (source.dirs.get(arg1[i]) == null) {
                return arg1[i] + ": The system cannot find the path specified.";
            }

            // Go ahead then
            if (!f1) source = source.dirs.get(arg1[i]);
        }
        
        // O nome do diretorio pode ser alterado no move
        for (int i = s2; i < n2; i++)
        {
            renameDir = arg2[i];
            if (arg2[i].equals(source.name)) {
                return arg2[i] + ": Can't move it!";
            }
            
            if (arg2[i].contains(".txt")) {
                f2 = true;
            }
            else if (destiny.dirs.get(arg2[i]) == null && i == n2 - 1 && !f1) {
                renameDir = arg2[i];
                rename = true;
            }
            else if (destiny.dirs.get(arg2[i]) == null) {
                return arg2[i] + ": The system cannot find the path specified.";
            }

            // Go ahead then
            if (!f2 && !rename) destiny = destiny.dirs.get(arg2[i]);
        }
        
        System.out.println("new dir name: " + renameDir);
        // Arrumar (. ..) | (.. .)
        if (!f1 && (source == destiny.father || source == pwd)) {
            return "Can't move";
        }
        
        if (!f1) // Mover um diretorio
        {
            source.father.dirs.remove(arg1[n1-1]);
            source.dirs.put("..", destiny);
            source.dirs.put(".", source);
            source.path = source.path.replaceFirst("/", "");
            source.path = destiny.path + "/" + (rename? renameDir : source.name);
            this.updatePath(source);
            destiny.dirs.put((rename? renameDir : arg1[n1-1]), source);
            
        }
        else // Mover um arquivo
        {
            String destinyFileName;
            if (f2) {
                if (!arg2[n2-1].contains(".txt")) {
                    arg2[n2 - 1] += ".txt";
                }
                destinyFileName = arg2[n2-1];
            }
            else {
                destinyFileName = arg1[n1-1];
            }
            
            destiny.files.put(destinyFileName, source.files.get(arg1[n1-1]));
            source.files.remove(arg1[n1-1]);
        }
        
        return result;
    }

    // rm -r ../.
    @Override public String rm(String parameters) {
        String result = "";

        //inicio da implementacao do aluno
        // Hah algum parametro?
        if (parameters.length() == 0) {
            return "The syntax of the command is incorrect.";
        }
        
        String args[] = this.countChar(parameters, ' ', true);
        int argc = args.length;
        
        // Quantos parametros?
        if (argc == 1) {
            // Remocao de arquivo => estamos procurando um .txt
            String path[] = this.countChar(args[0], '/', true);
            int steps = path.length;
            int s = 0;

            Node walker;
            
            try {
                walker = this.getPathByName(args[0], 1);
            }
            catch (PathNotFoundException ex) {
                return ex.getMessage();
            }
            
            if (walker.files.get(path[steps-1]) != null) {
                walker.files.remove(path[steps-1]);
            }
            else {
                return "File \"" + path[steps-1] + "\" not found in directory: " + walker.path;
            }
        }
        else if (argc == 2) {
            // Remocao de um diretorio?
            if (args[0].toLowerCase().equals("-r")) {
                // Bora remover um dir?
                String path[] = this.countChar(args[1], '/', true);
                int steps = path.length;
                int s = 0;

                Node walker;
                try {
                    walker = this.getPathByName(args[1], 1);
                }
                catch (PathNotFoundException ex) {
                    return ex.getMessage();
                }
                
                // Verificar se target eh um pai de pwd
                
                String target = path[steps-1];
                if (this.isFather(target, pwd.path) || target.equals(".") || target.equals("..")){
                    return "rm: refusing to remove \'.\' or \'..\' directory: skipping " + args[1];
                }
                // Remover o diretorio
                if (walker.dirs.get(path[steps-1]) != null) {
                    walker.dirs.remove(path[steps-1]);
                }
                else {
                    return "Diretory \"" + path[steps-1] + "\" not found in directory: " + walker.path;
                }
            }
            else {
                return args[0] + ": Invalid flag to rm! try \"-r\"";
            }
        }
        else {
            return "The syntax of the command is incorrect.";
        }
        
        //fim da implementacao do aluno
        return result;
    }

    // chmod –R 744 ./myFolder (-R = recursivo -> todos os filhos recebem a mesma perimissao)
    @Override public String chmod(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";

        System.out.println("parametro chmod => " + parameters);
        if (parameters.equals("")) {
            return "The syntax of the command is incorrect.";
        }
        
        // Recebeu algum parametro
        else {
            // Verificar parametros -> quantos foram recebidos ...
            String[] parameter = this.countChar(parameters, ' ', true);
            int n = parameter.length; // Calcula a quantidade de parametros recebidos
            int startPos = 1, permissions = 0;
            boolean recursive = false;
            boolean fileChmod = false;
            
            if (n < 2) {
                return "The syntax of the command is incorrect.";
            }
            else if (n == 3) {
                if (!parameter[0].toLowerCase().equals("-r")) {
                    return "chmod: illegal option " + parameter[0];
                }
                // O destino se encontra no terceiro parametro
                startPos++;
                
                // As permissoes estao na posicao 2
                permissions++;
                
                recursive = true;
            }
            
            String[] token = this.countChar(parameter[startPos], '/', true);
            int num = token.length;
            int start = 0;
            // System.out.println("Token: " + token[0] + "(" + num + ")");

            // Checar se o caminho eh absoluto
            Node walker;
            if (token[0].equals("")) {
                walker = home;
                start++;
            }
            else {
                walker = pwd;
            }
            
            // Ir ate o diretorio || procurar o arquivo
            for (int i = start; i < num; i++)
            {
                if (token[i].contains(".txt")) {
                    fileChmod = true;
                }
                else if (walker.dirs.get(token[i]) == null) {
                    return "The system cannot find the path specified.";
                }
                
                // Go ahead then
                if (!fileChmod) walker = walker.dirs.get(token[i]);
            }
            
            System.out.println("Changing permission in directory: " + walker.name);
            System.out.println("Token: " + token[num - 1]);
            // Calcular o chmod
            int value;
            try {
                value = Integer.parseInt(parameter[permissions]);
            }
            catch (NumberFormatException e) {
                return "The syntax of the command is incorrect.";
            }
            
            int x, y, z;
            x = value/ 100;
            value %= 100;
       
            y = value/ 10;
            value %= 10;

            z = value;
            if (x < 0 || x > 7 || y < 0 || y > 7 || z < 0 || z > 7) {
                return "The syntax of the command is incorrect.";
            }
            
            // Mudar a permissao
            if (fileChmod) {
                result = this.setPermission(walker, x, y, z, token[num - 1]);
            }
            else
                this.setPermission(walker, x, y, z, recursive);
        }
        
        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }
    
    // Todos os arquivos gerados serao do tipo .txt
    @Override public String createfile(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";

        //inicio da implementacao do aluno
      
        if (parameters.equals("")) {
            return "Usage: createfile [file_name] [file_content]";
        }
        
        //inicio da implementacao do aluno
        // Fazer um split baseado em espacos
        String[] str = parameters.split(" ", 2);
        String[] token = this.countChar(str[0], '/', true);
        int num = token.length;
        int s = 0;

        // Ignorar todos os /
        // System.out.println("Token: " + token[0]);

        // Consider that the path is absolute by default

        Node walker;
        try {
            walker = this.getPathByName(str[0], 1);
        }
        catch (PathNotFoundException ex) {
            return ex.getMessage();
        }
        
        if (ok("[ <>\"\'?:~|\\/!@#$%¨&*]", token[num-1])) {
            return "A file name can't contains any of the following chars:\n<>\'\"?*:|\\/";
        }
        // Checar se o nome esta dentro das especificacoes!
        
        if (!token[num -1].contains(".txt")) {
            token[num - 1] += ".txt";
        }
        
        myFile newFile;
        newFile = new myFile(token[num - 1], str.length == 2? str[1] : "");
        
        
        walker.files.put(newFile.getName(), newFile);
        
        
        //fim da implementacao do aluno
        return result;
    }

    @Override public String cat(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";

        //inicio da implementacao do aluno
        String[] token = this.countChar(parameters, '/', true);
        int num = token.length;
        int start = 0;

        // Consider that the path is absolute by default
        Node walker;
        
        try {
           walker = this.getPathByName(parameters, 1);
        }
        catch (PathNotFoundException ex) {
            return ex.getMessage();
        }
        
        //fim da implementacao do aluno
        if (walker.files.get(token[num-1]) == null) {
            result += "The system cannot find the file specified.";
        }
        else {
            String[] content = walker.files.get(token[num -1]).getContent().split("\\\\n");
            for (String line : content) {
                result += line + "\n";
            }
            // result += walker.files.get(token[num-1]).getContent();
        }
        return result;
    }

    @Override public String batch(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";

        
        //inicio da implementacao do aluno
        try {
            File load = new File(parameters);
            Scanner fileReader = new Scanner(load);
            
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String command = line.split(" ", 2)[0];
                String args = line.split(" ", 2)[1];
                
                if (command.equals("mkdir"))
                    mkdir(args);
                
                else if (command.equals("chmod"))
                    chmod(args);
                
                else if (command.equals("createfile"))
                    createfile(args);
                
                else if (command.equals("rmdir"))
                    rmdir(args);
                
                else if (command.equals("rm"))
                    rm(args);
                
                else if (command.equals("cd"))
                    cd(args);
                
                else if (command.equals("cp"))
                    cp(args);
                
                else if (command.equals("mv"))
                    mv(args);
            }
            
            fileReader.close();
        }
        catch (FileNotFoundException ex) {
            result = "batch: Couldn't load file";
        }
        //fim da implementacao do aluno
        return result;
    }

    @Override public String dump(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";

        //inicio da implementacao do aluno
        String content = "";
        FileWriter source;
        try {
            source = new FileWriter(parameters);
            content = dfsStore(content, home);
            System.out.println("Content: " + content);
            source.write(content);
            source.close();
        } catch (IOException ex) {
            result = "Error to open file";
        }
        //fim da implementacao do aluno
        return result;
    }

    @Override public String info() {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";

        //nome do aluno
        String name = "Vinicius Mari Marrafon (ViMM)";
        //numero de matricula
        String registration = "202011020023";
        //versao do sistema de arquivos
        String version = "2.0 (0 k[m])";

        result += "Nome do Aluno:        " + name;
        result += "\nMatricula do Aluno:   " + registration;
        result += "\nVersao do Kernel:     " + version;

        return result;
    }

}
