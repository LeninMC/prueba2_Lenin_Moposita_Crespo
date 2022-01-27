package com.nadershamma.apps.eventhandlers;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nadershamma.apps.androidfunwithflags.MainActivityFragmentMCLB;
import com.nadershamma.apps.androidfunwithflags.R;
import com.nadershamma.apps.androidfunwithflags.ResultsDialogFragmentMCLB;
import com.nadershamma.apps.lifecyclehelpers.QuizViewModelMCLB;

public class GuessButtonListenerMCLB implements OnClickListener {
    private MainActivityFragmentMCLB mainActivityFragmentMCLB;
    private Handler handler;

    public GuessButtonListenerMCLB(MainActivityFragmentMCLB mainActivityFragmentMCLB) {
        this.mainActivityFragmentMCLB = mainActivityFragmentMCLB;
        this.handler = new Handler();
    }

    @Override
    public void onClick(View v) {
        Button guessButton = ((Button) v);
        String guess = guessButton.getText().toString();
        String answer = this.mainActivityFragmentMCLB.getQuizViewModel().getCorrectCountryNameMCLB();
        this.mainActivityFragmentMCLB.getQuizViewModel().setTotalGuessesMCLB(1);

        if (guess.equals(answer)) {
            this.mainActivityFragmentMCLB.getQuizViewModel().setCorrectAnswersMCLB(1);
            this.mainActivityFragmentMCLB.getAnswerTextView().setText(answer + "!");
            this.mainActivityFragmentMCLB.getAnswerTextView().setTextColor(
                    this.mainActivityFragmentMCLB.getResources().getColor(R.color.correct_answer));

            this.mainActivityFragmentMCLB.disableButtons();

            if (this.mainActivityFragmentMCLB.getQuizViewModel().getCorrectAnswersMCLB()
                    == QuizViewModelMCLB.getFlagsInQuizMCLB()) {
                ResultsDialogFragmentMCLB quizResults = new ResultsDialogFragmentMCLB();
                quizResults.setCancelable(false);
                try {
                    quizResults.show(this.mainActivityFragmentMCLB.getChildFragmentManager(), "Quiz Results");
                } catch (NullPointerException e) {
                    Log.e(QuizViewModelMCLB.getTagMCLB(),
                            "GuessButtonListener: this.mainActivityFragment.getFragmentManager() " +
                                    "returned null",
                            e);
                }
            } else {
                this.handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mainActivityFragmentMCLB.animate(true);
                            }
                        }, 2000);
            }
        } else {
            this.mainActivityFragmentMCLB.incorrectAnswerAnimation();
            guessButton.setEnabled(false);
        }
    }
}
