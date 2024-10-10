
/**
 * Write a description of class Wordvalidator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import java.util.ArrayList; //
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
public class Wordvalidator
{
    // instance variables - replace the example below with your own
    public File Wordset;
    public Scanner Reader;
    public ArrayList<String> Words;
    public String holder;
    public void Wordvalidator(){
        try{
            Wordset = new File("scrabble.txt");
            Reader = new Scanner(Wordset);
            Words = new ArrayList<String> ();
            while(Reader.hasNextLine()){
                Words.add(Reader.nextLine());
            }
            System.out.println(Words);
            Reader.close();
        }catch(FileNotFoundException e){
            System.out.println("an Error Occured");
            e.printStackTrace();
        }
    }
    public boolean ItsAWord(String word){
        for (String i :Words){
            if (word == i){
                return true;
            }
        }
        return false;
    }

    

