package assignment;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


/*
 * CS 314H Assignment 2 - Random Writing
 *
 * Your task is to implement this RandomWriter class
 */
public class RandomWriter implements TextProcessor {

private HashMap<String,ArrayList<Character>> hash;
private int level;
private String currentSeed;
private StringBuilder input = new StringBuilder();



//The main method of the class serves 3 important functions.
// First, it checks the inputs from the args array
// Then, it instantiates the Random Writer with the inputted level of analysis.
// Finally, it calls the readText and writeText methods to generate the text to satisfy the purpose of the program.
    public static void main(String[] args) {
        //check args to make sure the things work

        if(args.length!=4) {
            System.err.println("Input Array is incorrect");
            return;
        }
        if((args[0]==null)||(args[1]==null)||(args[2]==null)||(args[3]==null)){
            System.err.println("Input Array is incorrect");
            return;
        }
        if((args[0]=="")||(args[1]=="")||(args[2]=="")||(args[3]=="")){
            System.err.println("Input Array contains empty Strings");
            return;
        }
        String inputFilename  = args[0];
        String outputFilename = args[1];
        try {
            int k = Integer.parseInt(args[2]);
            int length = Integer.parseInt(args[3]);
            if((k<0)||(length<0)){
                System.err.println("Invalid Array Args");
                return;
            }
            TextProcessor randomwriter = createProcessor(k);
            randomwriter.readText(inputFilename);
            randomwriter.writeText(outputFilename,length);
        }
        catch(NumberFormatException e){
            System.err.println("Invalid Args Array");
        }
        catch(IOException e){
            System.err.println("File Not Found or Not Able to Read/Write");

        }

    }


    public static TextProcessor createProcessor(int level) {
      return new RandomWriter(level);
    }

    private RandomWriter(int level) {
      // Do whatever you want here
        this.level = level;
        this.hash = new HashMap<String, ArrayList<Character>>();
    }

//This method attempts to instantiate a buffered reader
//It then reads the input file into a stringbuilder
// Then it calls the createHash method and generates the initial seed.
    public void readText(String inputFilename) throws IOException {
        input = new StringBuilder();
        String currentline;
        File inputFile = new File(inputFilename);
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String ls = System.getProperty("line.separator");
            while((currentline = br.readLine())!= null) {
                input.append(currentline);
                input.append(ls);
            }


            if(input.length()<=level){
                return;
            }

            if(input.length()==0)
                return;

            input = new StringBuilder(input.substring(0, input.length()-1));
        }

        createHash(level,input);


        int randomInt= (int) (Math.random()*(input.length()-level));
        currentSeed = input.substring(randomInt,randomInt+level);
    }

    //This method fills the HashMap with the key value pairs
    // such that all possible strings of length k (the level) found in the file are stored as keys
    // and the values are an arraylist containing all the characters that directly follow the key in the input file.

    private void createHash(int level, StringBuilder input){
        String key;

        for(int i = 0; i<input.length()-level;i++){
            key = input.substring(i,i+level);
            if(hash.containsKey(key))
                (hash.get(key)).add(input.charAt(i+level));
            else {
                ArrayList<Character> a = new ArrayList<Character>();
                a.add(input.charAt(i+level));
                hash.put(key, a);
            }

        }

    }
//This method attempts to create a BufferedReader, and creates an output file if it doesn’t already exist.
// Then it loops through the following process until it creates a file of the desired length:
// check that the seed has characters associated with it, if it does print one at random.
// Then construct the next seed.
// If the seed doesn’t have any characters associated with it, randomly pick another seed, print it and continue the process.
    public void writeText(String outputFilename, int length) throws IOException {

        try(BufferedWriter outWriter = new BufferedWriter(new FileWriter(outputFilename))){
            if(input.length()<=level){
                System.err.println("Level is not less than length of File");
                return;
            }
            if (input.length()==0)
                return;
            for(int i=0;i<length;i++){
                char toOutput;
                ArrayList<Character> chooseFrom = hash.get(currentSeed);
                if(chooseFrom== null){
                    int randomInt= (int) (Math.random()*(input.length()-level));
                    currentSeed = input.substring(randomInt,randomInt+level);
                    outWriter.write(currentSeed);
                    i=i+level;
                    i--;
                    continue;
                }
               toOutput = chooseFrom.get((int) (chooseFrom.size()*Math.random()));
                outWriter.write(toOutput);

               if(level==0)
                   continue;
               currentSeed = currentSeed.substring(1) + toOutput;

            }


        }

    }
}
