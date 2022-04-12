package com.blackcat.cryptography.form;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.blackcat.cryptography.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class Key {

    public interface Action {
        public void onSubmit(String s);
    }

    Context context;

    public Key(Context context) {
        this.context = context;
    }

    public void showDialog(Action action) {
        TextInputLayout inputLayout = new TextInputLayout(context);
        inputLayout.setPadding(
                context.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin),
                context.getResources().getDimensionPixelOffset(R.dimen.activity_vertical_margin),
                context.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin),
                0);
        inputLayout.setHint("Nhập k");

        EditText editText = new EditText(context);
        inputLayout.addView(editText);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered);
        dialogBuilder.setTitle("Nhập giá trị khóa");
        dialogBuilder.setView(inputLayout);
        dialogBuilder.setPositiveButton("Xác nhận", (dialogInterface, i) -> {
            if (Objects.requireNonNull(inputLayout.getEditText()).getText().toString().trim().isEmpty())
                inputLayout.setError("Vui lòng không để trống");
            else {
                action.onSubmit(inputLayout.getEditText().getText().toString().trim());
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Hủy", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }



}
