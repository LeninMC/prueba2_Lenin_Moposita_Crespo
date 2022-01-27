package com.nadershamma.apps.androidfunwithflags;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.nadershamma.apps.eventhandlers.GuessButtonListenerMCLB;
import com.nadershamma.apps.lifecyclehelpers.QuizViewModelMCLB;

public class MainActivityFragmentMCLB extends Fragment {

    private SecureRandom random;
    private Animation shakeAnimation;
    private ConstraintLayout quizConstraintLayout;
    private TextView questionNumberTextView;
    private ImageView flagImageView;
    private TableRow[] guessTableRows;
    private TextView answerTextView;
    private QuizViewModelMCLB quizViewModelMCLB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.quizViewModelMCLB = ViewModelProviders.of(getActivity()).get(QuizViewModelMCLB.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main_mclb, container, false);
        OnClickListener guessButtonListener = new GuessButtonListenerMCLB(this);
        TableLayout answersTableLayout = view.findViewById(R.id.answersTableLayout);

        this.random = new SecureRandom();
        this.shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake_mclb);
        this.shakeAnimation.setRepeatCount(3);
        this.quizConstraintLayout = view.findViewById(R.id.quizConstraintLayout);
        this.questionNumberTextView = view.findViewById(R.id.questionNumberTextView);
        this.flagImageView = view.findViewById(R.id.flagImageView);

        this.guessTableRows = new TableRow[4];
        this.answerTextView = view.findViewById(R.id.answerTextView);



        for (int i = 0; i < answersTableLayout.getChildCount(); i++) {
            try {
                if (answersTableLayout.getChildAt(i) instanceof TableRow) {
                    this.guessTableRows[i] = (TableRow) answersTableLayout.getChildAt(i);
                }
            } catch (ArrayStoreException e) {
                Log.e(QuizViewModelMCLB.getTag(),
                        "Error getting button rows on loop #" + String.valueOf(i), e);
            }
        }

        for (TableRow row : this.guessTableRows) {
            for (int column = 0; column < row.getChildCount(); column++) {
                (row.getChildAt(column)).setOnClickListener(guessButtonListener);
            }
        }

        this.questionNumberTextView.setText(
                getString(R.string.question, 1, QuizViewModelMCLB.getFlagsInQuiz()));
        return view;
    }

    public void updateGuessRowsMCLB() {

        int numberOfGuessRows = this.quizViewModelMCLB.getGuessRows();
        for (TableRow row : this.guessTableRows) {
            row.setVisibility(View.GONE);
        }
        for (int rowNumber = 0; rowNumber < numberOfGuessRows; rowNumber++) {
            guessTableRows[rowNumber].setVisibility(View.VISIBLE);
        }
    }

    public void resetQuizMCLB() {
        this.quizViewModelMCLB.clearFileNameList();
        this.quizViewModelMCLB.setFileNameList(getActivity().getAssets());
        this.quizViewModelMCLB.resetTotalGuesses();
        this.quizViewModelMCLB.resetCorrectAnswers();
        this.quizViewModelMCLB.clearQuizCountriesList();

        int flagCounter = 1;
        int numberOfFlags = this.quizViewModelMCLB.getFileNameList().size();
        while (flagCounter <= QuizViewModelMCLB.getFlagsInQuiz()) {
            int randomIndex = this.random.nextInt(numberOfFlags);

            String filename = this.quizViewModelMCLB.getFileNameList().get(randomIndex);

            if (!this.quizViewModelMCLB.getQuizCountriesList().contains(filename)) {
                this.quizViewModelMCLB.getQuizCountriesList().add(filename);
                ++flagCounter;
            }
        }

        this.updateGuessRowsMCLB();
        this.loadNextFlag();
    }

    private void loadNextFlag() {
        AssetManager assets = getActivity().getAssets();
        String nextImage = this.quizViewModelMCLB.getNextCountryFlag();
        String region = nextImage.substring(0, nextImage.indexOf('-'));

        this.quizViewModelMCLB.setCorrectAnswer(nextImage);
        answerTextView.setText("");

        questionNumberTextView.setText(getString(R.string.question,
                (quizViewModelMCLB.getCorrectAnswers() + 1), QuizViewModelMCLB.getFlagsInQuiz()));

        try (InputStream stream = assets.open(region + "/" + nextImage + ".png")) {
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);
            animate(false);
        } catch (IOException e) {
            Log.e(QuizViewModelMCLB.getTag(), "Error Loading " + nextImage, e);
        }

        this.quizViewModelMCLB.shuffleFilenameList();

        for (int rowNumber = 0; rowNumber < this.quizViewModelMCLB.getGuessRows(); rowNumber++) {
            for (int column = 0; column < guessTableRows[rowNumber].getChildCount(); column++) {
                Button guessButton = (Button) guessTableRows[rowNumber].getVirtualChildAt(column);
                guessButton.setEnabled(true);
                String filename = this.quizViewModelMCLB.getFileNameList()
                        .get((rowNumber * 2) + column)
                        .substring(this.quizViewModelMCLB.getFileNameList()
                                .get((rowNumber * 2) + column).indexOf('-') + 1)
                        .replace('_', ' ');
                guessButton.setText(filename);
            }
        }

        int row = this.random.nextInt(this.quizViewModelMCLB.getGuessRows());
        int column = this.random.nextInt(2);
        TableRow randomRow = guessTableRows[row];
        ((Button) randomRow.getChildAt(column)).setText(this.quizViewModelMCLB.getCorrectCountryName());
    }

    public void animate(boolean animateOut) {
        if (this.quizViewModelMCLB.getCorrectAnswers() == 0) {
            return;
        }
        int centreX = (quizConstraintLayout.getLeft() + quizConstraintLayout.getRight()) / 2;
        int centreY = (quizConstraintLayout.getTop() + quizConstraintLayout.getBottom()) / 2;
        int radius = Math.max(quizConstraintLayout.getWidth(), quizConstraintLayout.getHeight());
        Animator animator;
        if (animateOut) {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizConstraintLayout, centreX, centreY, radius, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    loadNextFlag();
                }
            });
        } else {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizConstraintLayout, centreX, centreY, 0, radius);
        }

        animator.setDuration(500);
        animator.start();
    }

    public void incorrectAnswerAnimation(){
        flagImageView.startAnimation(shakeAnimation);

        answerTextView.setText(R.string.incorrect_answer);
        answerTextView.setTextColor(getResources().getColor(R.color.wrong_answer));
    }

    public void disableButtons() {
        for (TableRow row : this.guessTableRows) {
            for (int column = 0; column < row.getChildCount(); column++) {
                (row.getChildAt(column)).setEnabled(false);
            }
        }
    }

    public TextView getAnswerTextView() {
        return answerTextView;
    }

    public QuizViewModelMCLB getQuizViewModel() {
        return quizViewModelMCLB;
    }
}

