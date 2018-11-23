import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class EmotionANN { //this method handles the emotion ANN, the neural network is a data type and multiple neural networks can exist, this manges operations of one

//a list of public variables
    public static Scanner input = new Scanner(System.in);
    public static int totalAnalysed = 1;
    public static NeuralNetwork ANN;
    public static boolean newANN;
    public static boolean firstIteration = true;
    public static NeuralNetwork subjectANN;
    public static int iter = 0;
    public static ArrayList<Double> costs;
    public static String saveNameANN;
    public static double[][] weights1;
    public static double[][] weights2;
    public static double[] a3Average;


    public static void main(String[] args) { //the main method ws here for testing purposes


    }


    //this method is passed a vector file (generated  by the W2V ANN, and the line number it shall read to be analysed)
    public static String readNextLine(String fileName, int lineNum) {

        String line = null;

        // this tries to open the input file, uses the Stream API
        // the Stream API allow you to handle data in  declarative manor
        // in this specific example, we can read a particular line in a text file without a load of loops reading all lines before
        // , this is more efficient as only one line is read rather than all of the lines before the objective one.

        try (Stream<String> lines = Files.lines(Paths.get("/Users/tomasflynn/Word2Vecmaven/src/main/resources/" + fileName + ".txt"))) {
            line = lines.skip(lineNum).findFirst().get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //System.out.println(line);

        return line;
    }

    //mostly used for de bugging processes, allows any float array to be printed
    public static void printArray(double[][] array) {
        System.out.println("printing array");
        int arrayX = array.length;
        int arrayY = array[0].length;

        for (int y = 0; y < arrayY; y++) {
            for (int x = 0; x < arrayX; x++) {
                System.out.print(array[x][y] + " ");

            }
            System.out.println("");
        }
    }

    //same as the other print array, it just works for 1D arrays, this is used in the debugging process
    public static void printArray(double[] subjectArray) {
        int arrayX = subjectArray.length;

        for (int x = 0; x < arrayX; x++) {
            System.out.println(subjectArray[x]);
        }
    }

    //pull a line of inputs (5 vectors that represent a word in 5-dimensional space)
    public static void populateX(double[][] x, String fileName) {
        System.out.println("populating x");
        int xX = x.length;
        int xY = x[0].length;

        for (int numOFInputs = 0; numOFInputs < (xY - 1); numOFInputs++) { //this iterates vertically down a text file of vectors,
            // each time it iterates it reads a line from the text file

            String lineOfVec = readNextLine(fileName, numOFInputs + totalAnalysed);
            String[] tokens = lineOfVec.split(" ");
            //the 'numOfInputs' line is called from the text file and each vector is parsed into a element of the tokens array
            //each vector is spaced at a space or " "

            for (int numOfvec = 1; numOfvec <= xX; numOfvec++) {//this iterates horizontally through the text file
                x[numOfvec - 1][numOFInputs] = Double.parseDouble(tokens[numOfvec]);
                //each token is added to is correct x value, x is the input array of the emotion ANN
            }
        }
        totalAnalysed = totalAnalysed + xY;
    }

    //this sets global variables relative to the way the emotion ANN should be treated, these values are determined in the gui by the user
    public static void conditionANN(String nameANN, boolean New) {

        newANN = New;
        saveNameANN = nameANN;

    }

    //when a emotion ANN is to be trained, this method is called, the file name and objective emotion is passed
    public static void trainANN(String fileName, int emotionNum) {


        if (firstIteration == true) {//on the first iterations this code runs, only on the first iteration
                                    //this method is recursive, its until the cost is low enough.

            long numOfLines = 0;

            try (Stream<String> lines = Files.lines(Paths.get("/Users/tomasflynn/Word2Vecmaven/src/main/resources/" + fileName + ".txt"), Charset.defaultCharset())) {
                numOfLines = lines.count();
            } catch (IOException e) {
                System.out.println("file not found");
            }
            System.out.println(numOfLines);
            //the number of lines in the text file is pulled, this is used to dynamically create the size of the arrays in the neural network,
            // dynamically creating them this way allows any size of file to be used to train or analyse in a single round of training,
            //this is enables us to take advantage of matrice operations

            subjectANN = new NeuralNetwork(((int) numOfLines), newANN);
            //the emotion ANN is dynamically created and called 'subjectANN'

            firstIteration = false;
            //the program now knows that the first iterations has occurred and not to run this code again

            costs = new ArrayList<>();
            //the dynamic array list is created to store all cost relative to the iterations of this recursive algorithm


            populateX(subjectANN.x, fileName);
            //the array 'x' containing the input data is populated with our vectors from a text file chosen by the  user

        }

        subjectANN.dotProduct(subjectANN.x, subjectANN.w1, subjectANN.z2);
        //the dot porduct matrice operation is preformed on 'x' and 'w1' and stored in 'z2'
        subjectANN.sigmoid(subjectANN.z2, subjectANN.a2);
        //the sigmoid activation function is run of the 'z2' array and stored in the 'a2' array
        subjectANN.dotProduct(subjectANN.a2, subjectANN.w2, subjectANN.z3);
        //the dot product of 'a2' and 'w2' is preformed, the result is stored in 'z3'
        subjectANN.softmax(subjectANN.z3, subjectANN.a3);
        //the softmax activation function is applied to the 'z3' array and stored in 'a3'

        //all previous arrays are 2D array, all operations occurred on 2D arrays

        double cost = subjectANN.costArraySM(emotionNum, subjectANN.a3, subjectANN.Csm);
        //the cost of the specific iteration is calculated


        subjectANN.CostZ2(emotionNum, subjectANN.z2, subjectANN.dCdZ2);
        //the derivative of the cost relative to 'z2' is calculated, the change in cost relative to the change of 'z2'
        subjectANN.CostW2(subjectANN.a2, subjectANN.dCdZ2, subjectANN.dCdW2);
        //the derivative od the cost relative to 'w2' is calculated , the change in cost relative to the change in 'w2'
        subjectANN.sigmoidPrime(subjectANN.z2, subjectANN.fPz2);
        //the equation of sigmoid prime (the derivative of sigmoid), is applied to 'z2'
        subjectANN.CostW1(subjectANN.x, subjectANN.dCdZ2, subjectANN.w2, subjectANN.fPz2, subjectANN.dCdW1);
        //the derivatice of the cost relative to 'w1' is calculated, the change in cost relative ot the change in 'w1'

        subjectANN.adjustWeights(subjectANN.w1, subjectANN.w2, subjectANN.dCdW1, subjectANN.dCdW2);
        //the weights are then adjusted accordingly based on the previous derivatives calculated

        //printArray(subjectANN.dCdW2);
        System.out.println(cost);

        iter++;


        if (cost > 0.1) {
            trainANN(fileName, emotionNum);
        }
        //the program is recalled and is therefore recursiz until the cost is lower the 0.1

        costs.add(cost);

        weights1 = subjectANN.w1;
        weights2 = subjectANN.w2;
        //when cost is low enough the weights are stored to be saved

    }

    //when a emotion AN has been trained further, it can be saved
    public static void saveANN() throws IOException {

        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(saveNameANN));
        // afile is either opened or created to write the weights to

        for (int y = 0; y < weights1[0].length; y++) {
            for (int x = 0; x < weights1.length; x++) {
                outputWriter.write(Double.toString(weights1[x][y]) + ",");
            }
            outputWriter.newLine();
        }
        //w1 is writen here
        for (int y = 0; y < weights2[0].length; y++) {
            for (int x = 0; x < weights2.length; x++) {
                outputWriter.write(Double.toString(weights2[x][y]) + ",");
            }
            outputWriter.newLine();
        }
        outputWriter.close();
        //w2 is written here

        Files.move(Paths.get(saveNameANN), Paths.get("src/main/resources/" + saveNameANN + ".txt"), StandardCopyOption.REPLACE_EXISTING);
        //the file is moved to somewhere more appropriate


    }

    //when a vector file need sot be analysed by a emotion ANN this is called
    public static void analyseData(String vecFile, String nameANN) {

        long numOfLines = 0;
        double temp = 0;
        saveNameANN = nameANN;

        try (Stream<String> lines = Files.lines(Paths.get("/Users/tomasflynn/Word2Vecmaven/src/main/resources/" + vecFile + ".txt"), Charset.defaultCharset())) {
            numOfLines = lines.count();
        } catch (IOException e) {
            System.out.println("file not found");
        }

        subjectANN = new NeuralNetwork((int) numOfLines, false);

        populateX(subjectANN.x, vecFile);
        subjectANN.dotProduct(subjectANN.x, subjectANN.w1, subjectANN.z2);
        subjectANN.sigmoid(subjectANN.z2, subjectANN.a2);
        subjectANN.dotProduct(subjectANN.a2, subjectANN.w2, subjectANN.z3);
        subjectANN.softmax(subjectANN.z3, subjectANN.a3);

        printArray(subjectANN.a3);

        a3Average = new double[subjectANN.a3.length];
        //this method is called once, to pass the data through the network to arrive at percentage likelyhoods that its either emotion


        for (int x = 0; x < subjectANN.a3.length; x++) {
            for (int y = 0; y < subjectANN.a3[0].length; y++) {
                temp = temp + subjectANN.a3[x][y];
            }
            a3Average[x] = temp / subjectANN.a3[0].length;
            temp = 0;
        }
        //the estimations are then averaged over all word vectors input


    }


}

