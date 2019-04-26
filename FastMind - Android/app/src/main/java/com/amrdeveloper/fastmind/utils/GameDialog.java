package com.amrdeveloper.fastmind.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;

public class GameDialog {

    public static Dialog showRequestDialog(Context context, String player, Action accept, Action refuse) {
        String challengeMessage = "Challenge Request from : ".concat(player);
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.request_dialog);
        dialog.setCancelable(false);

        TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
        ImageButton acceptAction = dialog.findViewById(R.id.acceptAction);
        ImageButton cancelAction = dialog.findViewById(R.id.cancelAction);

        dialogMessage.setText(challengeMessage);
        acceptAction.setOnClickListener(v -> {
            accept.apply();
            dialog.dismiss();
        });
        cancelAction.setOnClickListener(v -> {
            refuse.apply();
            dialog.dismiss();
        });

        dialog.show();
        return dialog;
    }

    public static Dialog showSingleWinnerDialog(Context context, Action accept, Action refuse) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.single_winner_dialog);
        dialog.setCancelable(false);

        ImageButton acceptAction = dialog.findViewById(R.id.acceptAction);
        ImageButton cancelAction = dialog.findViewById(R.id.cancelAction);

        acceptAction.setOnClickListener(v -> {
            accept.apply();
            dialog.dismiss();
        });
        cancelAction.setOnClickListener(v -> {
            refuse.apply();
            dialog.dismiss();
        });

        dialog.show();
        return dialog;
    }

    public static Dialog showWinnerDialog(Context context, int score, Action checked) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.win_dialog);
        dialog.setCancelable(false);

        TextView scorePoints = dialog.findViewById(R.id.scorePoints);
        ImageButton acceptAction = dialog.findViewById(R.id.acceptAction);

        scorePoints.setText("+".concat(String.valueOf(score)));
        acceptAction.setOnClickListener(v -> {
            dialog.dismiss();
            checked.apply();
        });

        dialog.show();
        return dialog;
    }

    public static Dialog showLoserDialog(Context context, int score, Action checked) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lose_dialog);
        dialog.setCancelable(false);

        TextView scorePoints = dialog.findViewById(R.id.scorePoints);
        ImageButton acceptAction = dialog.findViewById(R.id.checkedAction);

        scorePoints.setText("-".concat(String.valueOf(score)));
        acceptAction.setOnClickListener(v -> {
            dialog.dismiss();
            checked.apply();
        });

        dialog.show();
        return dialog;
    }

    public static Dialog showWaitDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.waiting_dialog);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
