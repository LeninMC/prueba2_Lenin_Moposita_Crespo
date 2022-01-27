package com.nadershamma.apps.androidfunwithflags;


import static android.content.Intent.getIntent;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
    private TextView tvMostrar;

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

        tvMostrar = view.findViewById(R.id.txtMostrar);

       // Intent intent = new Intent();
        //Bundle datosExtra = getIntent(intent).getExtras();
        //String user = datosExtra.getString("key_user");
       // tvMostrar.setText(user);



        for (int i = 0; i < answersTableLayout.getChildCount(); i++) {
            try {
                if (answersTableLayout.getChildAt(i) instanceof TableRow) {
                    this.guessTableRows[i] = (TableRow) answersTableLayout.getChildAt(i);
                }
            } catch (ArrayStoreException e) {
                Log.e(QuizViewModelMCLB.getTagMCLB(),
                        "Error getting button rows on loop #" + String.valueOf(i), e);
            }
        }

        for (TableRow row : this.guessTableRows) {
            for (int column = 0; column < row.getChildCount(); column++) {
                (row.getChildAt(column)).setOnClickListener(guessButtonListener);
            }
        }

        this.questionNumberTextView.setText(
                getString(R.string.question, 1, QuizViewModelMCLB.getFlagsInQuizMCLB()));
        return view;
    }

    public void updateGuessRowsMCLB() {

        int numberOfGuessRows = this.quizViewModelMCLB.getGuessRowsMCLB();
        for (TableRow row : this.guessTableRows) {
            row.setVisibility(View.GONE);
        }
        for (int rowNumber = 0; rowNumber < numberOfGuessRows; rowNumber++) {
            guessTableRows[rowNumber].setVisibility(View.VISIBLE);
        }
    }

    public void resetQuizMCLB() {
        this.quizViewModelMCLB.clearFileNameListMCLB();
        this.quizViewModelMCLB.setFileNameListMCLB(getActivity().getAssets());
        this.quizViewModelMCLB.resetTotalGuessesMCLB();
        this.quizViewModelMCLB.resetCorrectAnswersMCLB();
        this.quizViewModelMCLB.clearQuizCountriesListMCLB();

        int flagCounter = 1;
        int numberOfFlags = this.quizViewModelMCLB.getFileNameListMCLB().size();
        while (flagCounter <= QuizViewModelMCLB.getFlagsInQuizMCLB()) {
            int randomIndex = this.random.nextInt(numberOfFlags);

            String filename = this.quizViewModelMCLB.getFileNameListMCLB().get(randomIndex);

            if (!this.quizViewModelMCLB.getQuizCountriesListMCLB().contains(filename)) {
                this.quizViewModelMCLB.getQuizCountriesListMCLB().add(filename);
                ++flagCounter;
            }
        }

        this.updateGuessRowsMCLB();
        this.loadNextFlag();
    }

    private void loadNextFlag() {
        AssetManager assets = getActivity().getAssets();
        String nextImage = this.quizViewModelMCLB.getNextCountryFlagMCLB();
        String region = nextImage.substring(0, nextImage.indexOf('-'));

        this.quizViewModelMCLB.setCorrectAnswerMCLB(nextImage);
        answerTextView.setText("");

        questionNumberTextView.setText(getString(R.string.question,
                (quizViewModelMCLB.getCorrectAnswersMCLB() + 1), QuizViewModelMCLB.getFlagsInQuizMCLB()));

        try (InputStream stream = assets.open(region + "/" + nextImage + ".png")) {
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);
            animate(false);
        } catch (IOException e) {
            Log.e(QuizViewModelMCLB.getTagMCLB(), "Error Loading " + nextImage, e);
        }

        this.quizViewModelMCLB.shuffleFilenameListMCLB();

        for (int rowNumber = 0; rowNumber < this.quizViewModelMCLB.getGuessRowsMCLB(); rowNumber++) {
            for (int column = 0; column < guessTableRows[rowNumber].getChildCount(); column++) {
                Button guessButton = (Button) guessTableRows[rowNumber].getVirtualChildAt(column);
                guessButton.setEnabled(true);
                String filename = this.quizViewModelMCLB.getFileNameListMCLB()
                        .get((rowNumber * 2) + column)
                        .substring(this.quizViewModelMCLB.getFileNameListMCLB()
                                .get((rowNumber * 2) + column).indexOf('-') + 1)
                        .replace('_', ' ');
                guessButton.setText(filename);
            }
        }

        int row = this.random.nextInt(this.quizViewModelMCLB.getGuessRowsMCLB());
        int column = this.random.nextInt(2);
        TableRow randomRow = guessTableRows[row];
        ((Button) randomRow.getChildAt(column)).setText(this.quizViewModelMCLB.getCorrectCountryNameMCLB());
    }

    public void animate(boolean animateOut) {
        if (this.quizViewModelMCLB.getCorrectAnswersMCLB() == 0) {
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

