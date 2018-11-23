import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Main_Menu extends Application {  // 'extends Application' - this gets the functionality of javafx classes to make the gui

    Stage window;
    Scene main, w2v, emotion;
    boolean textFile = true;
    int emotionNum = 1;


    public static void main(String[] args) {

        launch(args); // when the project is launched, this method uses resources from 'Application' to declare the project with the properties of JavaFX


    }

    @Override
    public void start(Stage primaryStage) throws Exception { // 'primaryStage' - the initial

        window = primaryStage;

//objects - main menu
        Label title_main = new Label("Emotion Predictor - Machine Learning Project ");     //the title for the main menu
        title_main.setFont(new Font("Hallo", 20));                                  //the font and size for the title for the main menu
        Button openW2V = new Button("Word 2 Vector Conversion");                           //the button to change the scene to the word2Vector Artificial neural network
        Button openEmotion = new Button("Emotion Predictor");                              //the button to change the scene to the emotion predictor artificial neural network

//activity of objects - main menu
        openW2V.setOnAction(e -> window.setScene(w2v));                                         //the activity of the word2Vector button, changes to the scene to the 'w2v' scene
        openEmotion.setOnAction(e -> alertNeuralNetwork());                                     //the activity of the emotion predictor button, changes the scene to the 'emotion' scene

//layout - main menu

        GridPane layoutMenu = new GridPane();
        layoutMenu.setPadding(new Insets(20, 20, 20, 20));
        layoutMenu.setVgap(8);
        layoutMenu.setHgap(10);
        //a grid pane layout is created for the main menu, the gaping of objects and padding round the borders is set.

        GridPane.setConstraints(title_main, 2, 1);
        GridPane.setConstraints(openW2V, 2, 3);
        GridPane.setConstraints(openEmotion, 2, 5);
        layoutMenu.getChildren().addAll(title_main, openW2V, openEmotion);
        //setting the position of the objects onto the grid pane layout

        layoutMenu.setAlignment(Pos.CENTER);
        //aligns all objects and the layout itself to the center of the given window


        main = new Scene(layoutMenu, 600, 400);
        //the main scene has our previously designed layout and size constraints of the window applied to it

//objects - w2v
        Label title_w2v = new Label("Word 2 Vector - Artificial Neural Network");
        title_w2v.setFont(new Font("Impact", 20));
        //the title and its font and size, for the word2vector artificial neural network scene

        Label dropIN = new Label("Input: File or Raw Text:");
        Label dropEm = new Label("Objective Emotion:");
        //the two labels that shows the user the purpose of two drop down menus in the gui

        Button back_w2v = new Button("Back");
        Button confirmSettings = new Button("Confirm");
        //the two buttons present in the scene,
        // the back button reverts the scene back to main menu
        // the confirm settings button sets the selected drop down menus values as the selected settings

        ChoiceBox<String> inputType = new ChoiceBox<>();
        ChoiceBox<String> objectiveEmotion = new ChoiceBox<>();
        //creates the two drop down menus, drop down menus in javafx are called choice boxes

        TextArea inputData = new TextArea("");
        inputData.setPrefSize(400, 300);
        inputData.setWrapText(true);
        //creates a input box for text, whether that is a files name or raw text to be converted to vectors

        Button enterData = new Button("Enter");
        //button to confirm the data in the input box

        Label title_inputData = new Label("Input Data: ");
        //a label to tell you the purpose of the input box


//activity of Objects - w2v
        back_w2v.setOnAction(e -> window.setScene(main));
        //the code the changes the window to the main menu when the back button is pressed

        inputType.getItems().addAll("Input File", "Input Raw Text");
        inputType.setValue("Input File");

        objectiveEmotion.getItems().addAll("Fear", "Anger", "Sadness", "Joy", "Disgust", "Surprise", "Trust", "Anticipation");
        objectiveEmotion.setValue("Fear");
        // setting the options in the drop down boxes

        confirmSettings.setOnAction(e -> settingChoice(inputType, objectiveEmotion));
        //setting the activity to confirm the choices chosen in the drop down boxes

        enterData.setOnAction(e -> {
            if (textFile = false) {
                inDataToTextFile(inputData.getText());
            } else if (textFile = true) {
                try {
                    Files.move(Paths.get(inputData.getText() + ".txt"), Paths.get("src/main/resources/GUI_to_w2v.txt"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                alertSaveName();
            }
            window.setScene(main);
        });
        //this checks whether the data in the input field is a text file name or a raw text, dependant on this factor, it is handled in the relevant way


//layout - w2v
        GridPane leftMenu = new GridPane();
        HBox topMenu = new HBox();
        HBox bottomMenu = new HBox();
        VBox centerMenu = new VBox();
        //the word2vector gui is a border pane, allowing different layouts for different sections of the border pane
        //the previous lines are setting the layout types of all the different sections of the border pane

        topMenu.setAlignment(Pos.CENTER);
        //setting the positioning of the top menu

        leftMenu.setPadding(new Insets(10, 10, 10, 10));
        leftMenu.setVgap(8);
        leftMenu.setHgap(10);
        GridPane.setConstraints(dropIN, 1, 3);
        GridPane.setConstraints(inputType, 1, 4);
        GridPane.setConstraints(dropEm, 1, 5);
        GridPane.setConstraints(objectiveEmotion, 1, 6);
        GridPane.setConstraints(confirmSettings, 1, 8);
        //setting the positioning anf arrangement of the left menu,
        // and setting the position of all its objects

        bottomMenu.setAlignment(Pos.BASELINE_RIGHT);
        bottomMenu.setPadding(new Insets(10, 10, 10, 10));
        //setting the alignment of the bottom menu and the padding round the borders

        centerMenu.setPadding(new Insets(40, 150, 20, 20));
        centerMenu.setSpacing(5);
        //setting the spacing of objects and the padding of the objects


        BorderPane layoutw2v = new BorderPane();
        //setting the main overall layout to a border pane


        leftMenu.getChildren().addAll(dropIN, inputType, dropEm, objectiveEmotion, confirmSettings);
        topMenu.getChildren().addAll(title_w2v);
        centerMenu.getChildren().addAll(title_inputData, inputData, enterData);
        bottomMenu.getChildren().addAll(back_w2v);
        //assigning all objects to their respected layouts

        layoutw2v.setTop(topMenu);
        layoutw2v.setLeft(leftMenu);
        layoutw2v.setCenter(centerMenu);
        layoutw2v.setBottom(bottomMenu);
        //setting the different sections of the main border pane layout to the individual sub-layouts


        w2v = new Scene(layoutw2v, 800, 650);
        //setting the 'w2v' scene to contain the main border pane layout, aswell as setting its size

//objects - emotion

        Label title_emotion = new Label("Emotion Artificial Neural Networks");
        //the title of the emotion artificial neural netwrok

        Label textField_title = new Label("Enter Vector File:");
        TextField vectorFile = new TextField();
        Button enter_vectorFile = new Button("Enter");
        //creating the the title, input field and enter button for inputting a vector file

        Button back_emotion = new Button("Back");
        //creating a back button to move back to the main menu

        Label saveLabel = new Label("Press to Save to " + EmotionANN.saveNameANN + ".txt:");
        Button save = new Button("Save");
        //creating the button and label to save the created or edited artificial neural network

        //create the graph for data

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(0, 20, 1);
        xAxis.setLabel("Number of Iterations");
        yAxis.setLabel("Cost");
        //creating the chart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Cost Reduction Through Training:");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Cost of Edited Weights");
        lineChart.getData().add(series);

//activity of objects - emotion

        back_emotion.setOnAction(e -> window.setScene(main));
        //the code to go back to the main menu from the emotion ANN gui

        save.setOnAction(e -> {
            try {
                EmotionANN.saveANN();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        // saves the ANN



//layout - emotion

        GridPane leftMenu_emotion = new GridPane();
        HBox topMenu_emotion = new HBox();
        HBox bottomMenu_emotion = new HBox();
        GridPane centerMenu_emotion = new GridPane();
        //the emotion scene is a border pane, here the sub layouts of the border pane are created

        BorderPane layoutEmotion = new BorderPane();
        //the main border pane is created

        leftMenu_emotion.setPadding(new Insets(10, 10, 10, 10));
        //topMenu_emotion.setPadding(new Insets(10,10,10,10));
        centerMenu_emotion.setPadding(new Insets(10, 10, 10, 10));
        bottomMenu_emotion.setPadding(new Insets(10, 10, 10, 10));
        //setting the padding of the sub-layouts

        //this is the activity for a object, its here as its dynamically created later on

        enter_vectorFile.setOnAction(e -> {
            EmotionANN.trainANN(vectorFile.getText(), emotionNum);
            createGraph(EmotionANN.iter, EmotionANN.costs, series);
        });

        //the graph previously created is dynamically added to after the costs and iterations have been determined by the ANN being trained

        GridPane.setConstraints(textField_title, 1, 3);
        GridPane.setConstraints(vectorFile, 1, 4);
        GridPane.setConstraints(enter_vectorFile, 1, 5);
        GridPane.setConstraints(saveLabel, 1, 7);
        GridPane.setConstraints(save, 1, 8);
        //setting the positions of the objects in the grid pane for one of the menus


        leftMenu_emotion.setVgap(8);
        leftMenu_emotion.setHgap(10);
        centerMenu_emotion.setVgap(8);
        centerMenu_emotion.setHgap(10);
        //setting the gaping of objects within their respective layouts

        topMenu_emotion.getChildren().addAll(title_emotion);
        bottomMenu_emotion.setAlignment(Pos.BASELINE_RIGHT);
        bottomMenu_emotion.getChildren().addAll(back_emotion);
        leftMenu_emotion.getChildren().addAll(textField_title, vectorFile, enter_vectorFile, saveLabel, save);
        //all objects are added to  the respected scenes


        layoutEmotion.setTop(topMenu_emotion);
        layoutEmotion.setTop(leftMenu_emotion);
        layoutEmotion.setCenter(lineChart);
        layoutEmotion.setBottom(bottomMenu_emotion);
        //the sub-layouts are added to the main border pane layout

        emotion = new Scene(layoutEmotion, 800, 650);
        //the emotion scene has the main border pane layout added to it, its size is also dictated


//initialize the default scene
        window.setScene(main);
        window.show();
        window.setTitle("Main Menu");


    }
//called when the 'confirmSettings' button is pressed,
    //it edits a boolean value to see whether the data passed is test file name or raw text its self
    //assigns number values to the selected emotions
    //ultimatly its assigning computable values  to the choices in the drop down boxes in the word2vector menu
    private void settingChoice(ChoiceBox<String> inputType, ChoiceBox<String> objectiveEmotion) {

        if (inputType.getValue() == "Input File") {
            textFile = true;

        } else if (inputType.getValue() == "Input Raw Text") {
            textFile = false;
        }

        String emotion = objectiveEmotion.getValue();

        switch (emotion) {

            case "Fear":
                emotionNum = 1;
                break;

            case "Anger":
                emotionNum = 2;
                break;

            case "Sadness":
                emotionNum = 3;
                break;

            case "Joy":
                emotionNum = 4;
                break;

            case "Disgust":
                emotionNum = 5;
                break;

            case "Surprise":
                emotionNum = 6;
                break;

            case "Trust":
                emotionNum = 7;
                break;

            case "Anticipation":
                emotionNum = 8;
                break;


        }


    }

//sends the entered data into the text field, into a intermediate text file to be processed by the word2vec ANN
    private void inDataToTextFile(String inputData) {
        BufferedWriter writer = null;
        String filePath = "src/main/resources/GUI_to_w2v.txt";
        //file path of the intermediate file

        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(inputData);
            writer.close();

        } catch (IOException e) {

        }
        //tries to write the data to the file


        alertSaveName();
        //calls a alert box to get the name to save the word vectors too



    }

//when called an alert box opens to get the save name for a file containing word vectors
    private void alertSaveName() {

        String saveNameText = "";

        Stage alertBox = new Stage();
        alertBox.initModality(Modality.APPLICATION_MODAL);
        alertBox.setMinWidth(250);
        //creating the stage of the alert box

        Label label = new Label("Type the new file name for the word vectors: ");
        TextArea saveName = new TextArea();
        Button enter = new Button("Enter");
        //creating the objects of the alert box

        enter.setOnAction(e -> {
            try {
                Word2Vec_Main.new_corpus("GUI_to_w2v.txt", saveName.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            alertBox.close();
        });
        //when the enter button is pressed, then the word2vec ANN takes the text in the intermediate file 'GUI_to_w2v.txt' and converts it to word vectors
        //the result is saved in the save name passed into the method
        //when this button is pressed the alert box closes

        VBox layoutAlert = new VBox();
        layoutAlert.getChildren().addAll(label, saveName, enter);
        layoutAlert.setPadding(new Insets(10, 10, 10, 10));
        //creating the layout of the alert box

        layoutAlert.setSpacing(5);

        Scene scene = new Scene(layoutAlert);
        alertBox.setScene(scene);
        alertBox.showAndWait();
        //assigning the layout tot the scene and showing the scene


    }

//this finds out how the user want to use the emotion ANN, this is again in the form of a alert box
    private void alertNeuralNetwork() {

        Stage alertBox = new Stage();
        alertBox.initModality(Modality.APPLICATION_MODAL);
        alertBox.setMinWidth(250);
        //creating the scene of the alert box

        Label choice = new Label("Select ANN Option: ");
        Button createNew = new Button("Create ");
        Button Load = new Button("Load ");
        Button Use = new Button("Use Existing");
        //creating the objects in the scene

        createNew.setOnAction(e -> {
            conditionANN(true);
            alertBox.close();
        });
        //when this button is selected, a new emotion ANN is created with random weights
        //with the intention to train the network

        Load.setOnAction(e -> {
            conditionANN(false);
            alertBox.close();
        });
        //when this button is selected, a previously created emotion ANN is loaded,
        // the specific  weights are loaded into the emotion ANN

        Use.setOnAction(e -> {
            loadUseANN();
            alertBox.close();
        });
        //when this button is selected, a emotion ANN and file to be analysed is chosen, the file is determined to be of a specific emotion

        //whenever  any of these buttons are pressed then the alert box closes

        VBox layoutAlert = new VBox();
        layoutAlert.getChildren().addAll(choice, createNew, Load, Use);
        layoutAlert.setPadding(new Insets(10, 10, 10, 10));
        //layoutAlert.setAlignment(Pos.BASELINE_LEFT);
        layoutAlert.setSpacing(5);
        //the layout is created and the objects are added to the layout

        Scene scene = new Scene(layoutAlert);
        alertBox.setScene(scene);
        alertBox.showAndWait();
        //the layout is added to the scene and the scene is shown


    }

//when the intention of the user is to train a new or existing emotion ANN this method is run.
    //this is another alert box
    private void conditionANN(boolean New) {

        Stage alertBox = new Stage();
        alertBox.initModality(Modality.APPLICATION_MODAL);
        alertBox.setMinWidth(250);
        //the alert box scene is created


        Label label = new Label("Enter the name of the Artificial Neural Network: ");
        TextArea saveName = new TextArea();
        Button enter = new Button("Enter");
        //the buttons in the scene are  created

        enter.setOnAction(e -> {
            EmotionANN.conditionANN(saveName.getText(), New);
            window.setScene(emotion);
            alertBox.close();
        });
        //global variables in the 'EmotionANN' are set based on the passed parameters, these determine hoe to treat the input data in the future
        //once this is pressed the alert box closes

        VBox layoutAlert = new VBox();
        layoutAlert.getChildren().addAll(label, saveName, enter);
        layoutAlert.setPadding(new Insets(10, 10, 10, 10));
        //layoutAlert.setAlignment(Pos.BASELINE_LEFT);
        layoutAlert.setSpacing(5);
        //the layout is created, objects are added, spaccing is defined and padding on the border of the scene is added

        Scene scene = new Scene(layoutAlert);
        alertBox.setScene(scene);
        alertBox.showAndWait();
        //the scene has the layout applied and is shown

    }

//this method is run when the user is loading a emotion ANN to anylyse a text file for its emotion.
    private void loadUseANN() {
        Stage alertBox = new Stage();
        alertBox.initModality(Modality.APPLICATION_MODAL);
        alertBox.setMinWidth(250);
        //the sence is created to get the name of the emotion ANN

        Label label = new Label("Enter the name of the Artificial Neural Network: ");
        TextArea nameANN = new TextArea();
        Button enter = new Button("Enter");
        //objects are created for the scene

        enter.setOnAction(e -> {
            alertVectorFile_Analyse(nameANN.getText());
            alertBox.close();
        });
        //when this button is pressed , the emotion ANN name is passed to the final method to anlyse the vector file

        VBox layoutAlert = new VBox();
        layoutAlert.getChildren().addAll(label, nameANN, enter);
        layoutAlert.setPadding(new Insets(10, 10, 10, 10));
        //layoutAlert.setAlignment(Pos.BASELINE_LEFT);
        layoutAlert.setSpacing(5);
        //the layout is created and the objects are added, as well as the paddiung of the borders being set and the spacing between the objects being set

        Scene scene = new Scene(layoutAlert);
        alertBox.setScene(scene);
        alertBox.showAndWait();
        //the scene is shown and the layout is assigned
    }

//when the results of a vector file being analysed by a specific emotion ANN, the results are shown here
    private void alertResults(String vecFile, String nameANN) {
        Stage alertBox = new Stage();
        alertBox.initModality(Modality.APPLICATION_MODAL);
        alertBox.setMinWidth(250);
        //the alert box scene is created

        Label labelTitle = new Label("The Artificial Neural Network, " + nameANN + " dictates that the data of " + vecFile + " is...");
        labelTitle.setFont(new Font("Hallo", 20));
        Label Fear = new Label("Fear = " + (EmotionANN.a3Average[0] * 100) + "%");
        Label Anger = new Label("Anger = " + (EmotionANN.a3Average[1] * 100) + "%");
        Label Sadness = new Label("Sadness = " + (EmotionANN.a3Average[2] * 100) + "%");
        Label Joy = new Label("Joy = " + (EmotionANN.a3Average[3] * 100) + "%");
        Label Disgust = new Label("Disgust = " + (EmotionANN.a3Average[4] * 100) + "%");
        Label Surprise = new Label("Surprise = " + (EmotionANN.a3Average[5] * 100) + "%");
        Label Trust = new Label("Trust = " + (EmotionANN.a3Average[6] * 100) + "%");
        Label Anticipation = new Label("Anticipation = " + (EmotionANN.a3Average[7] * 100) + "%");
        //all the results are created as the data is pulled from the Emoton ANN handler class after analysis


        VBox layoutAlert = new VBox();
        layoutAlert.getChildren().addAll(labelTitle, Fear, Anger, Sadness, Joy, Disgust, Surprise, Trust, Anticipation);
        layoutAlert.setPadding(new Insets(10, 10, 10, 10));
        //layoutAlert.setAlignment(Pos.BASELINE_LEFT);
        layoutAlert.setSpacing(5);
        //the layout is created and characteristics are set

        Scene scene = new Scene(layoutAlert);
        alertBox.setScene(scene);
        alertBox.showAndWait();
        //the layout is assigned to the scene and the scene is shown

    }

//the name of the vector file to be analysed by a Emotion ANN is input by the user
    //for the vector file to be analysed is called here ,the method to show the alert box with the results is also called
    private void alertVectorFile_Analyse(String nameANN) {
        Stage alertBox = new Stage();
        alertBox.initModality(Modality.APPLICATION_MODAL);
        alertBox.setMinWidth(250);
        //the alert box scene is create

        Label label = new Label("Enter the name of the vector file: ");
        TextArea vecFile = new TextArea();
        Button enter = new Button("Enter");
        //the objects of the scene are created

        enter.setOnAction(e -> {
            EmotionANN.analyseData(vecFile.getText(), nameANN);
            alertResults(vecFile.getText(), nameANN);
            alertBox.close();
        });
        //when this button is pressed , the method to analyse the vector file is called
        //the method to show the results is also called
        //the alertbox is then closed


        VBox layoutAlert = new VBox();
        layoutAlert.getChildren().addAll(label, vecFile, enter);
        layoutAlert.setPadding(new Insets(10, 10, 10, 10));
        //layoutAlert.setAlignment(Pos.BASELINE_LEFT);
        layoutAlert.setSpacing(5);
        //the layout is create, objects are added and arrangement on spacing and padding is set

        Scene scene = new Scene(layoutAlert);
        alertBox.setScene(scene);
        alertBox.showAndWait();
        //the scene is created and the layout is added to it
    }

    //when a network has been trained, this method is called to plot the costs and iterations in the emotion gui
    private final void createGraph(int numOfIter, ArrayList<Double> costs, XYChart.Series series) {

        //populating the series with data

        for (int i = 0; i < numOfIter; i++) {
            series.getData().add(new XYChart.Data(i, costs.get(i)));
            //System.out.println(i +" "+costs.get(i));
        }
        //this loops runs the amount of times the emotion ANN had to train to get the answer right
        //each iteration the average cost of all output nodes and the iteration number is pasted on the graph


    }


}
