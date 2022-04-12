package com.blackcat.cryptography.form;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.blackcat.cryptography.databinding.FragmentFormBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends DialogFragment {

    public interface Action {
        public String onChange(String input);
    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "subtitle";
    private static final String ARG_PARAM3 = "type";

    private FragmentFormBinding binding;
    private View root;

    private String mTitle;
    private String mSubTitle;
    private String mTypeOutput;
    private TextInputLayout inputLayout;
    private EditText input;
    private MaterialTextView type, contentOutput;
    private Action action;
    private MaterialToolbar toolbar;

    public FormFragment(Action action) {
        // Required empty public constructor
        this.action = action;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param subTitle Parameter 2.
     * @return A new instance of fragment FormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormFragment newInstance(String title, String subTitle, String type, Action action) {
        FormFragment fragment = new FormFragment(action);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, subTitle);
        args.putString(ARG_PARAM3, type);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            mSubTitle = getArguments().getString(ARG_PARAM2);
            mTypeOutput = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFormBinding.inflate(inflater,container,false);
        type = binding.outputType;
        contentOutput = binding.outputContent;
        inputLayout = binding.input;
        toolbar = binding.topAppBarFeedBack;
        input = inputLayout.getEditText();


        root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setTitle(mTitle);
        toolbar.setSubtitle(mSubTitle);
        contentOutput.setText("");
        type.setText(mTypeOutput);

        toolbar.setNavigationOnClickListener(view1 -> dismiss());

        if (input != null) {
            input.setText("");
            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().trim().isEmpty())
                        contentOutput.setText("");
                    else {
                        String s = action.onChange(editable.toString().trim());
                        if (s != null)
                            contentOutput.setText(s);
                    }


                }
            });
        }
    }
}