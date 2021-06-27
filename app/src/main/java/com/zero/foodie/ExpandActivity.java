package com.zero.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

public class ExpandActivity extends AppCompatActivity {
    private NestedScrollView nested_scroll_view;
    private ImageButton imageButtonToggleText, imageButtonToggleInput;
    private View layoutExpandText, layoutExpandInput;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_expandableview);
        parent_view = findViewById(android.R.id.content);
        initToolbar();

        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {


        // input section
        imageButtonToggleInput = findViewById(R.id.activity_expansion_panel_btn_toggle_input);
        Button buttonHideInput = findViewById(R.id.activity_expansion_panel_btn_hide_input);
        Button buttonSaveInput = findViewById(R.id.activity_expansion_panel_btn_save_input);
        layoutExpandInput = findViewById(R.id.activity_expansion_panel_lyt_expand_input);
        layoutExpandInput.setVisibility(View.GONE);

        imageButtonToggleInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toggleSectionInput(imageButtonToggleInput);
            }
        });

        buttonHideInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                toggleSectionInput(imageButtonToggleInput);
            }
        });

        buttonSaveInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "Saved", Snackbar.LENGTH_SHORT).show();

            }
        });

        // nested scrollview
        nested_scroll_view = findViewById(R.id.activity_expansion_panels_nested_scroll_view);
    }


    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

}