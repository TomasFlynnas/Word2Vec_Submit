

import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Scanner;


public class Word2Vec_Main {

    private static Logger log = LoggerFactory.getLogger(Word2Vec_Main.class);
    private static Scanner input = new Scanner(System.in);


    public static void main(String[] args) throws Exception {


    }


    public static void new_corpus(String fileName, String saveName) throws Exception {


        // Gets Path to Text file
        String filePath = new ClassPathResource(fileName).getFile().getAbsolutePath();

        log.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        /*
            CommonPreprocessor gets rid of all number, punctuation... as the ANN can only
            process tokens that are strings
         */
        t.setTokenPreProcessor(new CommonPreprocessor());

        //setting the hyper-parameters of the model


        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(1)
                .iterations(1)
                .layerSize(5)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();


        //passing the tokens through the model
        log.info("Fitting Word2Vec model....");
        vec.fit();

        //this saves the vectors in the default location
        log.info("Save vectors....");
        //WordVectorSerializer.writeWord2VecModel(vec, saveName +".txt");
        WordVectorSerializer.writeWordVectors(vec, saveName + ".txt");

        //the vector file is then moved to somewhere more suitable for later use
        Files.move(Paths.get(saveName + ".txt"), Paths.get("src/main/resources/" + saveName + ".txt"), StandardCopyOption.REPLACE_EXISTING);

    }


}