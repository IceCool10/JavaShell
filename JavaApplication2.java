
package javaapplication2;
import java.awt.*;
import java.io.*;
import java.lang.*;
import java.nio.file.Files;
import java.util.Scanner;


public class JavaApplication2 {

    public static void showAllFiles(File target) {
        for(File file : target.listFiles()) {
            if(file.isDirectory()) // for recursion 
            {
                System.out.println("\033[32m" + file.getName() + "/" + (char)27 + "[0m"); // folderele vor aparea cu culoarea
                                                                                          // verde si cu / la final
                showAllFiles(file);
            }
            else
                System.out.println(file.getName());
        }
    }
    
    public static void showDirectories(File target,int value) { //afisam doar folderele cu lsdir
        for(File file : target.listFiles()) {
            if(file.isDirectory())
            {
                if(value == 0) {
                    System.out.println("\033[32m" + file.getName() + "/" + (char)27 + "[0m");
                    showDirectories(file, 1);
                }
                else {
                    System.out.print("\033[32m" + target.getName() + "/" + (char)27 + "[0m");
                    System.out.println("\033[32m" + file.getName() + "/" + (char)27 + "[0m");
                    showDirectories(file,1);
                }
                                                                                            // folderele vor aparea cu culoarea
                                                                                            // verde si cu / la final    
            }
        }
    }
    
    public static void showFilesExtension(File target,String pattern) { //afisam fisierele care contin "pattern"
        for(File file : target.listFiles()) {
            if(file.isDirectory()) // for recursion
                showFilesExtension(file,pattern);
            else {
                if(file.getName().contains(pattern))
                    System.out.println(file.getName());
            }
            
        }
    }
    
    public static String changeDirectory(String path,String command) {
        String anotherDirectory = command.substring(command.indexOf(" ")+1, command.length());
        String directory = path;
        
        directory += Character.toString((char)92);
        directory += command.substring(command.indexOf(" ")+1, command.length());
        
        File verifyDirectory = new File(directory);
        File anotherVerifyDirectory = new File(anotherDirectory);
        
        if(command.substring(command.lastIndexOf(" ")+1, command.length()).equals("..")) {
            path = path.substring(0,path.lastIndexOf(Character.toString((char)92)));
        }
        
        else if(verifyDirectory.isDirectory()) {
            path = directory;
        }
        
        else if(anotherVerifyDirectory.isDirectory()) { // daca userul scrie fullPath-ul ( cd D:\blabla\blabla2 )
            path = anotherDirectory;
        }
        //System.out.println(path);
        return path;
    }
    //
    
    public static void createFile(String path, String command) throws IOException {
        String fileName = command.substring(command.indexOf(' ')+1,command.length());
       
        path += Character.toString((char)92);
        path += fileName;
        
        File file = new File(path);
        
        if(file.exists() == true) {
            System.out.println("The file " + fileName + " already exists!");
        }
        
        else {
          file.createNewFile();
        }
    }
    
    public static void removeFile(String path, String command) {
        String fileName = command.substring(command.indexOf(' ')+1,command.length());
        
        path += Character.toString((char)92);
        path += fileName;
        
        File removedFile = new File(path);
        
        if(removedFile.isFile() && removedFile.delete() == true) {
            System.out.println("The file " + fileName + " has been deleted");
        }
        else {
            System.out.println("The file " + fileName + " does not exists");
        }
    }
    //"\033[32m" + file.getName() + "/" + (char)27 + "[0m"
    public static void createDirectory(String path, String command) {
        String directoryName = command.substring(command.indexOf(' ')+1,command.length());
        
        path += Character.toString((char)92);
        path += directoryName;
        
        File newDirectory = new File(path);
        
        if(newDirectory.exists() == true) {
            System.out.println("Directory already exists!");
        }
        
        else {
            if(newDirectory.mkdir() == true) {
                System.out.println("Directory \033[32m" + directoryName + (char)27 + "[0m has been created");
            }
            
            else {
                System.out.println("Directory \033[32m" + directoryName + (char)27 + "[0m has \033[31mNOT[0m been created");
            }
        }
    }
    
    public static void writeToFile(String path, String command) throws IOException {
        String commandArray[];
        commandArray = command.split(" ");
        String content = command.substring(command.indexOf(commandArray[1])+commandArray[1].length()+1,command.length());
        
        //System.out.println(commandArray[0] + " " + commandArray[1]);
        
        String fileName = path + Character.toString((char)92) + commandArray[1];
        File destinationFile = new File(fileName);
        
        if(!destinationFile.exists()) {
            destinationFile.createNewFile();
        }
        
        FileWriter fw = new FileWriter(destinationFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
    }
    
    public static void displayContentFile(String path, String command) throws FileNotFoundException, IOException {
        String fileName = command.substring(command.indexOf(" ")+1,command.length());
        
        path += Character.toString((char)92);
        path += fileName;
        
        File displayedFile = new File(path);
        
        if(displayedFile.exists() == true) {
            FileReader fr = new FileReader(displayedFile.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);
            
            String s; 
            while((s = br.readLine()) != null) { 
            System.out.println(s); 
            } 
            fr.close();
        }
        else {
            System.err.println("The file " + fileName + " does NOT exists");
        }
    }
    
    public static void main(String[] args) throws IOException {
        String path = "D:";
        String pattern;  
        String command = null;
        Scanner scanIn = new Scanner(System.in);
        
        do {        
            File folder = new File(path);
            System.out.print("\033[36mroot@" + path + ":");
            command = scanIn.nextLine();
            
            if(command.equals("ls")) {
                showAllFiles(folder);
            }
            else if(command.equals("lsdir")) { // afisam doar folderele din folderul actual
                showDirectories(folder,0);
            }
            else if(command.startsWith("ls ")) { // daca nu e doar comanda ls
                pattern = command.substring(command.lastIndexOf(' ')+1, command.length()).toLowerCase();
                showFilesExtension(folder, pattern);
            }
            else if(command.startsWith("cd ") && !command.equals("cd")) {
                path = changeDirectory(path,command);
            }
            else if(command.equals("cd")) {
                path = "D:";
            }
            else if(command.startsWith("mkfile ")) { //create file
                createFile(path,command);
            }
            else if(command.startsWith("rm ")) {    //remove file
                removeFile(path,command);
            }
            else if(command.startsWith("mkdir ")) {
                createDirectory(path,command);
            }
            else if(command.startsWith("write ")) {
                writeToFile(path,command);
            }
            else if(command.startsWith("cat ")) {
                displayContentFile(path,command);
            }
            else {
                if(!command.equals("exit"))
                System.err.println("\033[31m" + "ERROR!! Command not found! Try again" + (char)27 + "[0m");
            }
            
        } while (!command.equals("exit"));
        scanIn.close();
    }
    
}
