import java.util.Random;

public class NeuralNetwork {

    //making the hyper-parameters constants will suffice for our project, in theory t
// these could be specified by the user, allowing them the ability to create their
// own ANN, all functions should be created in a way that would allow this
    public int inputLayer = 5;
    public int hiddenLayer = 7;
    public int outputLayer = 8;
    public int numOfIn;

    //defining the structure of the network
    public double[][] x;   //the input matrix
    public double[][] w1;  //the first set of weights
    public double[][] z2;  //the hidden layer neurons, values after the inputs are effected by the weights
    public double[][] a2;  //z2 after the activation function have been applied
    public double[][] w2;  //the second matrix of weights
    public double[][] z3;  //the final layer of neurons
    //the x-axis of z3 lists the different emotions, in this order,
    //according to Robert Plutchiks theory:
    //Fear, Anger, Sadness, Joy, Disgust, Surprise, Trust, Anticipation

    // y = f(z3)
    // y = f(w2 * a2)
    // y = f(w2 * f(z2))
    // y = f(w2 * f(x * w1))
    public double[][] a3;  //z3 after the activation function has been applied
    public double[][] fPz2;//sigmoid prime of z2
    public double[][] fPz3;//sigmoid prime of z3
    public double[] Csm; //the total cost of al outputs,
    public double[] Csig;//the cost at each node of all
    public double[][] dCdZ2;//rate of change of the cost with respect to Z, an average of all inputs
    public double[][] dCdW2;//the rate of change of the cost with respect to the w2
    public double[][] dCdW1;//the rate if change of the cost with respect to w1

    //default constructor, for inputting 5 vector words at once
    //this was primarily used for testing and has less practical use as its less efficient
    public NeuralNetwork(boolean create) {

        numOfIn = 5;
        x = new double[inputLayer][numOfIn];
        w1 = new double[hiddenLayer][inputLayer];
        z2 = new double[hiddenLayer][numOfIn];
        a2 = new double[hiddenLayer][numOfIn];
        w2 = new double[outputLayer][hiddenLayer];
        z3 = new double[outputLayer][numOfIn];
        a3 = new double[outputLayer][numOfIn];
        fPz2 = new double[hiddenLayer][numOfIn];
        fPz3 = new double[outputLayer][numOfIn];
        Csm = new double[outputLayer];
        dCdZ2 = new double[outputLayer][numOfIn];
        dCdW2 = new double[outputLayer][hiddenLayer];
        dCdW1 = new double[hiddenLayer][inputLayer];

        if (create == true) {
            randomWeight(w1);
            randomWeight(w2);
        }else if (create == false) {
            int w1X = w1.length;
            int w1Y = w1[0].length;
            int w2X = w2.length;
            int w2Y = w2[0].length;

            for (int y = 0; y < w1Y; y++) {
                String lineOfVec = EmotionANN.readNextLine(EmotionANN.saveNameANN, y);
                String[] tokens = lineOfVec.split(",");
                for (int x = 0; x < w1X; x++) {
                    w1[x][y] = Double.parseDouble(tokens[x]);

                }
            }
            for (int y = 0; y < w2Y; y++) {
                String lineOfVec = EmotionANN.readNextLine(EmotionANN.saveNameANN, w1Y + y);
                String[] tokens = lineOfVec.split(",");
                for (int x = 0; x < w2X; x++) {
                    w2[x][y] = Double.parseDouble(tokens[x]);
                }
            }
        }
    }

    // constructor allowing for a specification of the number of word vectors to be passed
    public NeuralNetwork(int num, boolean create) {

        numOfIn = num;
        x = new double[inputLayer][numOfIn];
        w1 = new double[hiddenLayer][inputLayer];
        z2 = new double[hiddenLayer][numOfIn];
        a2 = new double[hiddenLayer][numOfIn];
        w2 = new double[outputLayer][hiddenLayer];
        z3 = new double[outputLayer][numOfIn];
        a3 = new double[outputLayer][numOfIn];
        fPz2 = new double[hiddenLayer][numOfIn];
        fPz3 = new double[outputLayer][numOfIn];
        Csm = new double[outputLayer];
        dCdZ2 = new double[outputLayer][numOfIn];
        dCdW2 = new double[outputLayer][hiddenLayer];
        dCdW1 = new double[hiddenLayer][inputLayer];
        if (create == true) {
            randomWeight(w1);
            randomWeight(w2);
        } else if (create == false) {
            int w1X = w1.length;
            int w1Y = w1[0].length;
            int w2X = w2.length;
            int w2Y = w2[0].length;

            for (int y = 0; y < w1Y; y++) {
                String lineOfVec = EmotionANN.readNextLine(EmotionANN.saveNameANN, y);
                String[] tokens = lineOfVec.split(",");
                for (int x = 0; x < w1X; x++) {
                    w1[x][y] = Double.parseDouble(tokens[x]);

                }
            }
            //w1 is pulled from a text file, each vector is parsed and placed in the w1 array
            for (int y = 0; y < w2Y; y++) {
                String lineOfVec = EmotionANN.readNextLine(EmotionANN.saveNameANN, w1Y + y);
                String[] tokens = lineOfVec.split(",");
                for (int x = 0; x < w2X; x++) {
                    w2[x][y] = Double.parseDouble(tokens[x]);
                }
            }
            //w2 is pulled from a text file, each vector is parsed and placed in the w2 array

        }
    }

    //used for the initial creation of a ANN, set the weights random initially, then train them
// after through back propagation
    public void randomWeight(double[][] weightArray) {

        Random rand = new Random();
        int arrayX = weightArray.length;
        int arrayY = weightArray[0].length;
        System.out.println("random Weight");

        for (int x = 0; x < arrayX; x++) {
            for (int y = 0; y < arrayY; y++) {
                int randNumber = rand.nextInt(400);
                weightArray[x][y] = randNumber / 100;
            }
        }


    }

    //applies the sigmoid function to every element in the array specified, this is effectively
// the activation function
    public void sigmoid(double[][] subjectArray, double[][] resultArray) {
        double e = 2.718281828459045;
        int arrayX = subjectArray.length;
        int arrayY = subjectArray[0].length;

        for (int x = 0; x < arrayX; x++) {
            for (int y = 0; y < arrayY; y++) {

                resultArray[x][y] = (1 / (1 + (Math.pow(e, -subjectArray[x][y]))));

            }
        }

    }

    //preforms a dot product function of a weight array and a value array and stores it in the next value array(z2,z3)
    public void dotProduct(double[][] valueArray, double[][] weightArray, double[][] resultArray) {
        int valueX = valueArray.length;
        //int valueY = valueArray[0].length;
        //int weightX = weightArray.length;
        //int weightY = weightArray[0].length;
        int resultX = resultArray.length;
        int resultY = resultArray[0].length;
        double tempTotal = 0;


        for (int rY = 0; rY < resultY; rY++) {        //loops through the different instance of input
            for (int rX = 0; rX < resultX; rX++) {    //loops through the different nodes on z, to create a value for each node on z for a given instance on input
                for (int vX = 0; vX < valueX; vX++) {  //looping through to produce the value for one node in z of one instance of input
                    tempTotal = tempTotal + (valueArray[vX][rY] * weightArray[rX][vX]);
                }
                resultArray[rX][rY] = tempTotal;
                tempTotal = 0;
            }
        }
    }

    //gets the overall cost of all inputs for a given target node
    public double overallCost(int emotionNumber, boolean targetNode) {
        double totalCost = 0;
        double tempCost = 0;
        double y = 0;                         //stores the objective value of the node relative to the input.
        double yEst;                      //stores the value estimated from the ANN

        if (targetNode = true) {
            y = 1;
        } else if (targetNode = false) {
            y = 0;
        }

        for (int i = 0; i < numOfIn; i++) {
            yEst = z3[emotionNumber - 1][i];
            tempCost = y - yEst;
            totalCost = totalCost + (Math.pow(tempCost, 2));
            tempCost = 0;
        }

        totalCost = totalCost / 2;
        return totalCost;
    }

    //creates an array of the total cost of all inputs for each node, this is the cost function
// best for the softmax function
    public double costArraySM(int targetnode, double[][] subjectArray, double[] resultArray) {
        double temp = 0;
        int arrayX = subjectArray.length;
        int arrayY = subjectArray[0].length;
        double t = 0;
        double average = 0;

        for (int x = 0; x < arrayX; x++) {

            for (int y = 0; y < arrayY; y++) {
                if (x == targetnode - 1) {
                    t = 1;
                }
                temp = temp + (t * Math.log(subjectArray[x][y]));
                t = 0;
            }
            resultArray[x] = -(temp / arrayY);
            temp = 0;
        }
        for (int x = 0; x < arrayX; x++) {
            average = average + resultArray[x];
        }
        average = average / arrayX;
        return average;
    }

    //the derivative of the sigmoid function
    public void sigmoidPrime(double[][] subjectArray, double[][] resultArray) {
        double e = 2.718281828459045;
        int arrayX = subjectArray.length;
        int arrayY = subjectArray[0].length;

        for (int x = 0; x < arrayX; x++) {
            for (int y = 0; y < arrayY; y++) {

                double equ = 1 + Math.pow(e, -subjectArray[x][y]);
                resultArray[x][y] = (Math.pow(e, -subjectArray[x][y])) / (Math.pow(equ, 2));

            }
        }
    }

    //an activation function used on output neurons to yeild probabilities that the given node
// is the target node .
    public void softmax(double[][] subjectArray, double[][] resultArray) {
        double e = 2.718281828459045;
        int arrayX = subjectArray.length;
        int arrayY = subjectArray[0].length;
        double sumOfNeurons = 0;

        for (int y = 0; y < arrayY; y++) {
            for (int x = 0; x < arrayX; x++) {
                sumOfNeurons = sumOfNeurons + Math.pow(e, subjectArray[x][y]);
            }
            for (int x = 0; x < arrayX; x++) {

                resultArray[x][y] = Math.pow(e, subjectArray[x][y]) / sumOfNeurons;

            }
        }
    }

    //the derivative of the softmax function (change in the prediction relative to the z value)
    public void softmaxPrime(double[][] subjectArray, double[][] resultArray) {
        int arrayX = subjectArray.length;
        int arrayY = subjectArray[0].length;

        for (int y = 0; y < arrayY; y++) {
            for (int x = 0; x < arrayX; x++) {
                resultArray[x][y] = subjectArray[x][y] * (1 - subjectArray[x][y]);
            }
        }
    }

    //calculates the rate of change of the cost relative to z2, it populates an array with an average of all inputs
    public void CostZ2(int targetnode, double[][] subjectArray, double[][] resultArray) {
        int arrayX = subjectArray.length;
        int arrayY = subjectArray[0].length;
        double t = 0;

        for (int x = 0; x < arrayX; x++) {
            if (x == targetnode - 1) {
                t = 1;
            } else {
                t = 0;
            }
            for (int y = 0; y < arrayY; y++) {
                resultArray[x][y] = (subjectArray[x][y] - t);
            }
        }


    }

    //matrix multiplies a2 transpose and dCdZ2 and produces the rate of change of the cost relative to a given weight (it wil show for all input instances,
// however we will average these as input batches will have the same target node)
    public void CostW2(double[][] a2, double[][] dCdZ2, double[][] resultArray) {

        dotProduct(transpose(a2), dCdZ2, dCdW2);

    }

    public void CostW1(double[][] X, double[][] dCdZ2, double[][] w2, double[][] fPz2, double[][] resultArray) {
        double[][] temp1 = new double[hiddenLayer][inputLayer];
        double[] temp2 = new double[hiddenLayer];
        double[] temp3 = new double[outputLayer];
        double temp = 0;

        for (int x = 0; x < (dCdZ2.length); x++) {             //does the average of all inputs for the change in cost relative to the change in z2
            for (int y = 0; y < (dCdZ2[0].length); y++) {
                temp3[x] = temp3[x] + dCdZ2[x][y];
            }
            temp3[x] = temp3[x] / (dCdZ2[0].length);
        }
        sigmoidPrime(z2, fPz2);  //creates sigmoid prime

        dotProduct(transpose(X), fPz2, temp1);    //matrix multiplication of transpose x and sigmoid prime of z2

        for (int x = 0; x < hiddenLayer; x++) {   // matrix multiplication of transposed w2 and the averages of dCdZ2
            for (int y = 0; y < outputLayer; y++) {
                temp = temp + (transpose(w2)[x][y] * temp3[x]);
            }
            temp2[x] = temp;
            temp = 0;
        }

        for (int y = 0; y < inputLayer; y++) {
            for (int x = 0; x < hiddenLayer; x++) {
                resultArray[x][y] = temp1[x][y] * temp2[x];
            }
        }


    }

    //transposes any 2d array and returns the transposed array.
    public double[][] transpose(double[][] subjectArray) {
        int arrayX = subjectArray.length;
        int arrayY = subjectArray[0].length;
        double[][] returnArray = new double[arrayY][arrayX]; //creates an array to pass the transposed subject array into

        for (int x = 0; x < arrayY; x++) {
            for (int y = 0; y < arrayX; y++) {
                returnArray[x][y] = subjectArray[y][x];
            }
        }

        return returnArray;
    }

    public void adjustWeights(double[][] w1, double[][] w2, double[][] dCdW1, double[][] dCdW2) {
        int w1xLength = w1.length;
        int w1yLength = w1[0].length;
        int w2xLength = w2.length;
        int w2yLength = w2[0].length;

        for (int x = 0; x < w1xLength; x++) {
            for (int y = 0; y < w1yLength; y++) {
                w1[x][y] = w1[x][y] - dCdW1[x][y];
            }
        }
        for (int x = 0; x < w2xLength; x++) {
            for (int y = 0; y < w2yLength; y++) {

                w2[x][y] = w2[x][y] - dCdW2[x][y];
            }
        }


    }


}

