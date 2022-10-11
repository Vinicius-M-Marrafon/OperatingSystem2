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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    
    // Função utilizada para "setar" um caractere em uma posição especifica na string
    private String reSet(String str, char c, int pos)
    {
        char array[] = str.toCharArray();
        array[pos] = c;
        return new String(array);
    }
    
    private void setPermission(Node start, String u, String g, String a, boolean recursive)
    {
        for (int i = 0; i < 3; i++)
        {
            switch (i) {
                case 0:
                    // System.out.println(chmodU.charAt(i));
                    if (u.charAt(i) == '1') start.u = reSet(start.u, 'r', i);
                    else  start.u = reSet(start.u, '-', i);
                    if (g.charAt(i) == '1') start.g = reSet(start.g, 'r', i);
                    else  start.g = reSet(start.g, '-', i);
                    if (a.charAt(i) == '1') start.a = reSet(start.a, 'r', i);
                    else  start.a = reSet(start.a, '-', i);
                    break;
                case 1:
                    // System.out.println(chmodU.charAt(i));
                    if (u.charAt(i) == '1') start.u = reSet(start.u, 'w', i);
                    else  start.u = reSet(start.u, '-', i);
                    if (g.charAt(i) == '1') start.g = reSet(start.g, 'w', i);
                    else  start.g = reSet(start.g, '-', i);
                    if (a.charAt(i) == '1') start.a = reSet(start.a, 'w', i);
                    else  start.a = reSet(start.a, '-', i);
                    break;
                case 2:
                    //System.out.println(chmodU.charAt(i));
                    if (u.charAt(i) == '1') start.u = reSet(start.u, 'x', i);
                    else  start.u = reSet(start.u, '-', i);
                    if (g.charAt(i) == '1') start.g = reSet(start.g, 'x', i);
                    else  start.g = reSet(start.g, '-', i);
                    if (a.charAt(i) == '1') start.a = reSet(start.a, 'x', i);
                    else  start.a = reSet(start.a, '-', i);
                    break;
                default:
                    break;
            }
        }
         
         if (recursive) {
            for (Map.Entry<String, Node> set : start.dirs.entrySet()) {
                if (!(set.getKey().equals(".") || set.getKey().equals("..")))
                    this.setPermission(set.getValue(), u, g, a, recursive);
            }
         }
    }
    
    private void setPermission(Node start, String u, String g, String a, String fileName)
    {
        System.out.println("Changing: " + fileName);
        File target = start.files.get(fileName);
        for (int i = 0; i < 3; i++)
        {
            switch (i) {
                case 0:
                    // System.out.println(chmodU.charAt(i));
                    if (u.charAt(i) == '1') target.u = reSet(target.u, 'r', i);
                    else  target.u = reSet(target.u, '-', i);
                    if (g.charAt(i) == '1') target.g = reSet(target.g, 'r', i);
                    else  target.g = reSet(target.g, '-', i);
                    if (a.charAt(i) == '1') target.a = reSet(target.a, 'r', i);
                    else  target.a = reSet(target.a, '-', i);
                    break;
                case 1:
                    // System.out.println(chmodU.charAt(i));
                    if (u.charAt(i) == '1') target.u = reSet(target.u, 'w', i);
                    else  target.u = reSet(target.u, '-', i);
                    if (g.charAt(i) == '1') target.g = reSet(target.g, 'w', i);
                    else  target.g = reSet(target.g, '-', i);
                    if (a.charAt(i) == '1') target.a = reSet(target.a, 'w', i);
                    else  target.a = reSet(target.a, '-', i);
                    break;
                case 2:
                    //System.out.println(chmodU.charAt(i));
                    if (u.charAt(i) == '1') target.u = reSet(target.u, 'x', i);
                    else  target.u = reSet(target.u, '-', i);
                    if (g.charAt(i) == '1') target.g = reSet(target.g, 'x', i);
                    else  target.g = reSet(target.g, '-', i);
                    if (a.charAt(i) == '1') target.a = reSet(target.a, 'x', i);
                    else  target.a = reSet(target.a, '-', i);
                    break;
                default:
                    break;
            }
        }
    }
            
    // Retorna o nome do diretório que o usuário esta!
    @Override public String whereami() {
        return "Where am I: " + pwd.path;
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
                    String path[] = this.countChar(args[0], '/', true);
                    int steps = path.length;
                    int s = 0;

                    // Caminho absoluto?
                    if (path[0].equals("")) {
                        walker = home;
                        s++;
                    }
                    else {
                        walker = pwd;
                    }

                    for (int i = s; i < steps; i++)
                    {
                        if (walker.dirs.get(path[i]) == null) {
                            return "Cannot find path specified!";
                        }

                        // Go ahead then
                        walker = walker.dirs.get(path[i]);
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

                String path[] = this.countChar(args[1], '/', true);
                int steps = path.length;
                int s = 0;

                // Caminho absoluto?
                if (path[0].equals("")) {
                    walker = home;
                    s++;
                }
                else {
                    walker = pwd;
                }

                for (int i = s; i < steps; i++)
                {
                    if (walker.dirs.get(path[i]) == null) {
                        return "Cannot find path specified!";
                    }

                    // Go ahead then
                    walker = walker.dirs.get(path[i]);
                }
            }
            else {
                return "ls: The syntax of command is incorrect!";
            }
        } 
        else {
            walker = pwd;
        }
        
        if (!walker.u.contains("r")) {
            return "Access denied!";
        }
        
        result += "ls: directory " + walker.path + "\n";
        for (Map.Entry<String, Node> set : walker.dirs.entrySet()) {
            result += "\t"+ (per? set.getValue().getPermissions() + "\t\t": " ") + set.getKey() + "\n";
        }
        
        for (Map.Entry<String, File> set : walker.files.entrySet()) {
            result += "\t"+ (per? set.getValue().getPermissions() + "\t\t": " ") + set.getKey() + "\n";
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
            if (token[0].equals("")) {
                walker = home;
                start++;
            }
            else {
                walker = pwd;
            }
            
            for (int i = start; i < num - 1; i++)
            {
                if (walker.dirs.get(token[i]) == null) {
                    return "The system cannot find the path specified.";
                }

                // Go ahead then
                walker = walker.dirs.get(token[i]);
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
        // System.out.println("Chamada de Sistema: cd");
        // System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        
        String[] token = this.countChar(parameters, '/', true);
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
        
        for (int i = start; i < num; i++)
        {
            if (walker.dirs.get(token[i]) == null) {
                return "The system cannot find the path specified.";
            }

            // Go ahead then
            walker = walker.dirs.get(token[i]);
        }

        pwd = walker;
        
        //System.out.println("Your Dir now: " + pwd.name);

        //setando parte gráfica do diretorio atual
        operatingSystem.fileSystem.FileSytemSimulator.currentDir = pwd.path;
    

        //fim da implementacao do aluno
        return result;
    }

    // rmdir = Remove Diretórios VAZIOS!
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

            // Ignorar todos os /
            // System.out.println("Token: " + token[0]);
            
            // Considerar que o caminho é absoluto por padrão
            boolean isAbs = true;
            
            // Checar se o caminho é relativo
            if (pwd.dirs.get(token[0]) != null || num == 1) {
                isAbs = false;
            }
            
            //System.out.println("Creating directory: " + token[num]);

            Node walker = isAbs? home : pwd;
            for (int i = 0; i < num; i++)
            {
                if (walker.dirs.get(token[i]) == null) {
                    return "The system cannot find the file specified.";
                }

                // Prosseguir
                walker = walker.dirs.get(token[i]);
            }
            
            // System.out.println("Walker info\nSize Dir: " + walker.dirs.size() + "\nSize Files: " + walker.files.size());
            // Checar se walker esta vazio
            if (walker.name.equals(pwd.name)) {
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
        
        // Checar origem
        if (arg1[0].equals("")) {
            source = home;
            s1++;
            start++;
        }
        else {
            source = pwd;
        }
        
        // Checar destino
        if (arg2[0].equals("")) {
            destiny = home;
            s2++;
            start++;
        }
        else {
            destiny = pwd;
        }
        
        // Podemos ter arquivos misturado com diretorios (Tratar)
        for (int i = s1; i < n1; i++)
        {
            if (!dirCp && arg1[i].contains(".txt")) {
                f1 = true;
            }
            else if (source.dirs.get(arg1[i]) == null) {
                return arg1[i] + ": The system cannot find the path specified.";
            }

            // Go ahead then
            if (!f1) source = source.dirs.get(arg1[i]);
        }
        
        for (int i = s2; i < n2; i++)
        {
            if (!dirCp && arg2[i].contains(".txt")) {
                f2 = true;
            }
            else if (destiny.dirs.get(arg2[i]) == null) {
                return arg2[i] + ": The system cannot find the path specified.";
            }

            // Go ahead then
            if (!f2) destiny = destiny.dirs.get(arg2[i]);
        }
        
        if (dirCp && (source.name.equals(".") || source.name.equals("..") || source.name.equals("/"))) {
            return "Can't copy \'.\' and \'..\' directory!";
        }
        
        // Copiar os arquivos/ diretorios (nome destino pode ser alterado)
        if (dirCp) {
            // System.out.println("Destiny's name " + destiny.name);
            Node cpDir = source.copyNode(destiny);
            destiny.dirs.put(arg1[n1 - 1], cpDir);
        }
        
        // copia de arquivo
        else {
            if (source.files.get(arg1[n1-1]) != null) {
                System.out.println("File to copy: " + arg1[n1-1]);
                File target = source.files.get(arg1[n1-1]);
                File cpFile = target.copyFile(target);
                
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

    // Pode ser um arquivo ou um diretorio
    // Nao ha especificacao de diretorio com a flag -r
    @Override public String mv(String parameters){
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        // System.out.println("Chamada de Sistema: mv");
        // System.out.println("\tParametros: " + parameters);)
        //inicio da implementacao do aluno
        // Ir até o arquivo de origem e destino
        
        String[] args = this.countChar(parameters, ' ', true);
        int argc = args.length;
        int s1 = 0, s2 = 0;
        boolean f1 = false, f2 = false;
        
        if (argc != 2) {
            return "The syntax of the command is incorrect.";
        }
        
        String[] arg1 = this.countChar(args[0], '/', true);
        String[] arg2 = this.countChar(args[1], '/', true);
        int n1 = arg1.length;
        int n2 = arg2.length;
        
        Node source, destiny;
        
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
        
        for (int i = s2; i < n2; i++)
        {
            if (arg2[i].equals(source.name)) {
                return arg2[i] + ": Can't move it!";
            }
            
            if (arg2[i].contains(".txt")) {
                f2 = true;
            }
            else if (destiny.dirs.get(arg2[i]) == null) {
                return arg2[i] + ": The system cannot find the path specified.";
            }

            // Go ahead then
            if (!f2) destiny = destiny.dirs.get(arg2[i]);
        }
        
        if (!f1 && (source.name.equals(".") || source.name.equals("..") || source.name.equals("/"))) {
            return "Can't move \'.\' or \'..\' directory!";
        }
        
        // Mudar a referencia
        // Verificar se o usuario quer mover um diretorio
        // Tera que alterar todos os paths dos filhos do diretorio copiado
        if (!f1) {
            // Simplesmente mude o pai
            System.out.println("Source Father's name: " + source.father.name);
            
            source.father.dirs.remove(arg1[n1-1]);
            source.dirs.put("..", destiny);
            source.dirs.put(".", source);
            source.path = source.path.replaceFirst("/", "");
            source.path = destiny.path + source.path;
            destiny.dirs.put(arg1[n1-1], source);
        }
        // Verificar se o usuario quer mover um arquivo
        else { // OK!
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

    // Precisa especificar a flag -R para especificar a exclusão de um diretorio
    // Caso o usuário queira remover um diretório sem especificar -R, o comando
    // NÃO DEVERÁ REMOVE-LO!
    @Override public String rm(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        // System.out.println("Chamada de Sistema: rm");
        // System.out.println("\tParametros: " + parameters);

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
            // Caminho absoluto?
            if (path[0].equals("")) {
                walker = home;
                s++;
            }
            else {
                walker = pwd;
            }

            for (int i = s; i < steps - 1; i++)
            {
                if (walker.dirs.get(path[i]) == null) {
                    return "Cannot find path specified!";
                }

                // Go ahead then
                walker = walker.dirs.get(path[i]);
            }
            
            System.out.println("args: " + path[steps-1]);
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
                // Caminho absoluto?
                if (path[0].equals("")) {
                    walker = home;
                    s++;
                }
                else {
                    walker = pwd;
                }

                for (int i = s; i < steps - 1; i++)
                {
                    if (walker.dirs.get(path[i]) == null) {
                        return "Cannot find path specified!";
                    }

                    // Go ahead then
                    walker = walker.dirs.get(path[i]);
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
            
            // Get the binary representation of it
            String chmodU = String.format("%3s", Integer.toBinaryString(x)).replace(' ', '0');
            String chmodG = String.format("%3s", Integer.toBinaryString(y)).replace(' ', '0');
            String chmodA = String.format("%3s", Integer.toBinaryString(z)).replace(' ', '0');

//            System.out.println(chmodU);
//            System.out.println(chmodG);
//            System.out.println(chmodA);

            // Mudar a permissao
            if (fileChmod) {
                System.out.println("FileChmod");
                this.setPermission(walker, chmodU, chmodG, chmodA, token[num - 1]);
            }
            else
                this.setPermission(walker, chmodU, chmodG, chmodA, recursive);
        }
        
        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }
    
    // Todos os arquivos gerados serao do tipo .txt
    @Override public String createfile(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("\tParametros: " + parameters);

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
        if (token[0].equals("")) {
            walker = home;
            s++;
        } else {
            walker = pwd;
        }
        
        for (int i = s; i < num - 1; i++)
        {
            if (walker.dirs.get(token[i]) == null) {
                return "Cannot find path specified!";
            }

            // Go ahead then
            walker = walker.dirs.get(token[i]);
        }
        
        if (ok("[ <>\"\'?:~|\\/!@#$%¨&*]", token[num-1])) {
            return "A file name can't contains any of the following chars:\n<>\'\"?*:|\\/";
        }
        // Checar se o nome esta dentro das especificacoes!
        
        if (!token[num -1].contains(".txt")) {
            token[num - 1] += ".txt";
        }
        
        File newFile;
        newFile = new File(token[num - 1], str.length == 2? str[1] : "");
        
        
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
        if (token[0].equals("")) {
            walker = home;
            start++;
        }
        else {
            walker = pwd;
        }

        for (int i = start; i < num - 1; i++)
        {
            if (walker.dirs.get(token[i]) == null) {
                return "The system cannot find the path specified.";
            }

            // Go ahead then
            walker = walker.dirs.get(token[i]);
        }
        //fim da implementacao do aluno
        if (walker.files.get(token[num-1]) == null) {
            result += "The system cannot find the file specified.";
        }
        else {
            result += walker.files.get(token[num-1]).getContent();
        }
        return result;
    }

    @Override public String batch(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: batch");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    @Override public String dump(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: dump");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
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
        String version = "1.0";

        result += "Nome do Aluno:        " + name;
        result += "\nMatricula do Aluno:   " + registration;
        result += "\nVersao do Kernel:     " + version;

        return result;
    }

}
