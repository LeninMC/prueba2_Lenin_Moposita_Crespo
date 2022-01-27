package com.nadershamma.apps.lifecyclehelpers;

import android.arch.lifecycle.ViewModel;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class QuizViewModelMCLB extends ViewModel {
    private static final String TAG = "FlagQuiz Activity";
    private static final int FLAGS_IN_QUIZ = 10;

    private List<String> fileNameList;
    private List<String> quizCountriesList;
    private Set<String> regionsSet;
    private String correctAnswer;
    private int totalGuesses;
    private int correctAnswers;
    private int guessRows;


    public QuizViewModelMCLB() {
        fileNameList = new ArrayList<>();
        quizCountriesList = new ArrayList<>();
    }

    public static String getTagMCLB() {
        return TAG;
    }

    public static int getFlagsInQuizMCLB() {
        return FLAGS_IN_QUIZ;
    }

    public int getTotalGuessesMCLB() {
        return totalGuesses;
    }

    public void setTotalGuessesMCLB(int totalGuesses) {
        this.totalGuesses += totalGuesses;
    }

    public void resetTotalGuessesMCLB() {
        this.totalGuesses = 0;
    }

    public List<String> getFileNameListMCLB() {
        return fileNameList;
    }

    public void setFileNameListMCLB(AssetManager assets) {
        try {
            for (String region : regionsSet) {
                String[] paths = assets.list(region);
                for (String path : paths) {
                    this.fileNameList.add(path.replace(".png", ""));
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading image file names", e);
        }
    }

    public void clearFileNameListMCLB(){
        this.fileNameList.clear();
    }

    public void shuffleFilenameListMCLB(){
        Collections.shuffle(this.fileNameList);
        int correct = this.fileNameList.indexOf(this.correctAnswer);
        this.fileNameList.add(this.fileNameList.remove(correct));
    }

    public List<String> getQuizCountriesListMCLB() {
        return quizCountriesList;
    }

    public void setQuizCountriesListMCLB(List<String> quizCountriesList) {
        this.quizCountriesList = quizCountriesList;
    }

    public void clearQuizCountriesListMCLB(){
        this.quizCountriesList.clear();
    }

    public Set<String> getRegionsSetMCLB() {
        return regionsSet;
    }

    public void setRegionsSetMCLB(Set<String> regions) {
        this.regionsSet = regions;
    }

    public String getCorrectAnswerMCLB() {
        return correctAnswer;
    }

    public String getCorrectCountryNameMCLB() {
        return correctAnswer.substring(correctAnswer.indexOf('-') + 1)
                .replace('_', ' ');
    }

    public void setCorrectAnswerMCLB(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getCorrectAnswersMCLB() {
        return correctAnswers;
    }

    public void setCorrectAnswersMCLB(int correctAnswers) {
        this.correctAnswers += correctAnswers;
    }

    public void resetCorrectAnswersMCLB() {
        this.correctAnswers = 0;
    }

    public int getGuessRowsMCLB() {
        return guessRows;
    }



    public void setGuessRowsMCLB(String choices) {
        this.guessRows = Integer.parseInt(choices) / 2;
    }

    public String getNextCountryFlagMCLB(){
        return quizCountriesList.remove(0);
    }
}
