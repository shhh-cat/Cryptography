package com.blackcat.cryptography;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blackcat.cryptography.cipher.AffineCipher;
import com.blackcat.cryptography.cipher.CaesarCipher;
import com.blackcat.cryptography.cipher.HillCipher;
import com.blackcat.cryptography.cipher.RailFenceCipher;
import com.blackcat.cryptography.cipher.SubstitutionCipher;
import com.blackcat.cryptography.cipher.VigenereCipher;
import com.blackcat.cryptography.form.FormFragment;
import com.blackcat.cryptography.form.Key;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.blackcat.cryptography.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private static final int ENCRYPT = 1;
    private static final int DECRYPT = 0;
    private MaterialButton encrypt,decrypt,setKeyInput, setKeyFile;
    private MaterialTextView keyContent;
    private String keyValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        encrypt = binding.encryptCeasar;
        decrypt = binding.decryptCeasar;
        setKeyFile = binding.setKeyFromFile;
        setKeyInput = binding.setKeyFromInput;
        keyContent = binding.key;

        encrypt.setOnClickListener(this);
        decrypt.setOnClickListener(this);
        setKeyInput.setOnClickListener(this);
        setKeyFile.setOnClickListener(this);

        setContentView(binding.getRoot());

        setKey("<TRỐNG>");

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        navView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @SuppressLint("SetTextI18n")
    private void setKey(String s) {
        keyContent.setText("Khóa k: " + s);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == encrypt.getId()) {
            checkHasKey(ENCRYPT);
        }
        if (view.getId() == decrypt.getId()) {
            checkHasKey(DECRYPT);
        }
        if (view.getId() == setKeyFile.getId()) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");

            activityResultLauncher.launch(intent);
        }
        if (view.getId() == setKeyInput.getId()) {
            Key key = new Key(this);
            key.showDialog(s -> {
                keyValue = s;
                setKey(s);
            });
        }
    }

    private void checkHasKey(int type) {
        if (keyValue.isEmpty())
            new MaterialAlertDialogBuilder(this).setMessage("Vui lòng đặt khóa k").setPositiveButton("Xác nhận",null).show();
        else {
            chooseCipher(type);
        }
    }

    private boolean isNumeric(String s) {
        try {
            int i = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isAlphabetic(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isAlphabetic(Character.codePointAt(s,i)))
                return false;
        }
        return true;
    }

    private boolean isAffineKey(String s) {
        String[] parts = s.split(",");
        if (parts.length != 2) return false;
        return isNumeric(parts[0]) && isNumeric(parts[1]);
    }

    private void chooseCipher(int type) {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered);
        dialog.setTitle("Chọn mật mã");
        String[] ciphers= new String[] {
            "Caesar",
            "Substitution",
            "Rail Fence",
            "Affine",
            "Vigenere",
            "Hill",
        };
        dialog.setSingleChoiceItems(ciphers, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String typeString = (type == ENCRYPT ? "Bản mã: " : "Bản rõ: ");
                FormFragment formFragment;
                switch (ciphers[i]) {
                    case "Caesar":
                        if (!isNumeric(keyValue)) {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Caesar: khóa k là một số").setPositiveButton("Xác nhận",null).show();
                            dialogInterface.dismiss();
                            return;
                        }

                        CaesarCipher caesarCipher = new CaesarCipher(Integer.parseInt(keyValue));
                         formFragment = FormFragment.newInstance(
                                "Ceasar Cipher",
                                keyContent.getText().toString(),
                                typeString, new FormFragment.Action() {
                                    @Override
                                    public String onChange(String input) {
                                        return type == ENCRYPT ? caesarCipher.encrypt(input) : caesarCipher.decrypt(input);
                                        //return "ahihi";
                                    }
                                });
                        break;
                    case "Substitution":
                        if (!isAlphabetic(keyValue)) {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Substitution: khóa k phải có toàn bộ các kí tự Alphabet").setPositiveButton("Xác nhận",null).show();
                            dialogInterface.dismiss();
                            return;
                        }

                        SubstitutionCipher substitutionCipher = new SubstitutionCipher(keyValue);
                        formFragment = FormFragment.newInstance(
                                "Substitution Cipher",
                                keyContent.getText().toString(),
                                typeString, new FormFragment.Action() {
                                    @Override
                                    public String onChange(String input) {
                                        return type == ENCRYPT ? substitutionCipher.encrypt(input) : substitutionCipher.decrypt(input);
                                        //return "ahihi";
                                    }
                                });
                        break;
                    case "Rail Fence":
                        if (!isNumeric(keyValue)) {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Rail Fence: khóa k là một số").setPositiveButton("Xác nhận",null).show();
                            dialogInterface.dismiss();
                            return;
                        }
                        if (Integer.parseInt(keyValue) < 0) {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Rail Fence: khóa k phải không được âm").setPositiveButton("Xác nhận",null).show();
                            dialogInterface.dismiss();
                            return;
                        }

                        RailFenceCipher railFenceCipher = new RailFenceCipher(Integer.parseInt(keyValue));
                        formFragment = FormFragment.newInstance(
                                "Rail Fence Cipher",
                                keyContent.getText().toString(),
                                typeString, new FormFragment.Action() {
                                    @Override
                                    public String onChange(String input) {
                                        return type == ENCRYPT ? railFenceCipher.encrypt(input) : railFenceCipher.decrypt(input);
                                        //return "ahihi";
                                    }
                                });
                        break;
                    case "Affine":
                        if (!isAffineKey(keyValue)) {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Affine: khóa k không đúng định dạng\nĐịnh dạng: a,b\n(a và b là một số)").setPositiveButton("Xác nhận", null).show();
                            dialogInterface.dismiss();
                            return;
                        }
                        String[] p = keyValue.split(",");

                        AffineCipher affineCipher = new AffineCipher(p[0],p[1]);
                        formFragment = FormFragment.newInstance(
                                "Affine Cipher",
                                keyContent.getText().toString(),
                                typeString, new FormFragment.Action() {
                                    @Override
                                    public String onChange(String input) {
                                        return type == ENCRYPT ? affineCipher.encrypt(input) : affineCipher.decrypt(input);
                                        //return "ahihi";
                                    }
                                });
                        break;
                    case "Vigenere":
                        if (!isAlphabetic(keyValue)) {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Vigenere: khóa k phải có toàn bộ các kí tự Alphabet").setPositiveButton("Xác nhận", null).show();
                            dialogInterface.dismiss();
                            return;
                        }
                        VigenereCipher vigenereCipher = new VigenereCipher();
                        formFragment = FormFragment.newInstance(
                                "Vigenere Cipher",
                                keyContent.getText().toString(),
                                typeString, new FormFragment.Action() {
                                    @Override
                                    public String onChange(String input) {
                                        return type == ENCRYPT ? vigenereCipher.setKey(VigenereCipher.generateKey(input,keyValue)).encrypt(input) : vigenereCipher.setKey(VigenereCipher.generateKey(input,keyValue)).decrypt(input);
                                    }
                                });
                        break;
                    case "Hill":
                        if (!isAlphabetic(keyValue)) {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Hill: khóa k phải có toàn bộ các kí tự Alphabet").setPositiveButton("Xác nhận", null).show();
                            dialogInterface.dismiss();
                            return;
                        }
                        double sq = Math.sqrt(keyValue.length());
                        if (sq != (long) sq) {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Hill: khóa k không thể tạo thành ma trận vuông").setPositiveButton("Xác nhận", null).show();
                            dialogInterface.dismiss();
                            return;
                        }
                        int size = (int) sq;

                        HillCipher hillCipher = new HillCipher(size);
                        if (!hillCipher.isInvertible(keyValue,size))
                        {
                            new MaterialAlertDialogBuilder(MainActivity.this).setTitle("Lỗi").setMessage("Hill: khóa k không thể đảo ngược").setPositiveButton("Xác nhận", null).show();
                            dialogInterface.dismiss();
                            return;
                        }
                        formFragment = FormFragment.newInstance(
                                "Hill Cipher",
                                keyContent.getText().toString(),
                                typeString, new FormFragment.Action() {
                                    @Override
                                    public String onChange(String input) {
                                        hillCipher.cofact(hillCipher.km,size);
                                        return type == ENCRYPT ? hillCipher.encrypt(input,size) : hillCipher.decrypt(input,size);
                                    }
                                });
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + ciphers[i]);
                }

                formFragment.show(getSupportFragmentManager(),formFragment.getTag());
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("Hủy", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        dialog.show();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Uri uri;
                    if (data != null) {
                        uri = data.getData();

                        try {
                            InputStream in = getContentResolver().openInputStream(uri);
                            BufferedReader r = new BufferedReader(new InputStreamReader(in));
                            String text = r.readLine();
                            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered);
                            dialogBuilder.setTitle("Khóa từ tệp txt");
                            dialogBuilder.setMessage("Khóa : \"" + text + "\"");
                            dialogBuilder.setPositiveButton("Xác nhận", (dialogInterface, i) -> {
                                keyValue = text.trim();
                                setKey(keyValue);
                                dialogInterface.dismiss();
                            });
                            dialogBuilder.setNegativeButton("Hủy", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            });
                            dialogBuilder.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

}