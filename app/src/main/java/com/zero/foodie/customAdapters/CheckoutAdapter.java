package com.zero.foodie.customAdapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.zero.foodie.MainActivity;
import com.zero.foodie.MapActivity;
import com.zero.foodie.R;
import com.zero.foodie.customFragments.CartFragment;
import com.zero.foodie.model.CartModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckoutAdapter extends DialogFragment {

    public static final String TAG = "example_dialog";

    private Toolbar toolbar;
    private EditText address, phoneNumber;
    ImageButton launchGMap;
    public final static int REQUEST_CODE_B = 1;

    String lan, lon, phone;
    Context context;
    Button dialogDone;


    public CheckoutAdapter() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_checkout, container, false);
        launchGMap = view.findViewById(R.id.launchGMap);
        address = view.findViewById(R.id.addressCheckoutEditText);
        dialogDone = view.findViewById(R.id.dialogDone);
        phoneNumber = view.findViewById(R.id.phoneCheckoutEditText);
        toolbar = view.findViewById(R.id.toolbar_checkoutdialog);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String mont, day, hour, minute, second, finalUniqueID;
        Calendar calander = Calendar.getInstance();
        finalUniqueID = String.valueOf(calander.get(Calendar.YEAR));
        int month = calander.get(Calendar.MONTH) + 1;
        if (calander.get(Calendar.DAY_OF_MONTH) < 10) {
            day = "0" + calander.get(Calendar.DAY_OF_MONTH);
        } else {
            day = String.valueOf(calander.get(Calendar.DAY_OF_MONTH));
        }
        if (calander.get(Calendar.SECOND) < 10) {
            second = "0" + calander.get(Calendar.SECOND);
        } else {
            second = "" + calander.get(Calendar.SECOND);
        }
        if (calander.get(Calendar.MINUTE) < 10) {
            minute = "0" + calander.get(Calendar.MINUTE);
        } else {
            minute = "" + calander.get(Calendar.MINUTE);
        }
        if (calander.get(Calendar.HOUR) < 10) {
            hour = "0" + calander.get(Calendar.HOUR);
        } else {
            hour = "" + calander.get(Calendar.HOUR);
        }
        if (month < 10) {
            mont = "0" + month;
        } else {
            mont = month + "";
        }
        finalUniqueID = "a" + finalUniqueID + "" + mont + "" + day + "" + hour + "" + minute + "" + second;
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserLocation", context.MODE_PRIVATE);
        lan = sharedPreferences.getString("Latitude", "");
        lon = sharedPreferences.getString("Longitude", "");
        address.setText(lan + "," + lon);
        sharedPreferences = this.getActivity().getSharedPreferences("UserData", context.MODE_PRIVATE);
        phone = sharedPreferences.getString("phoneNumber", "");
        phoneNumber.setText(phone);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        String finalUniqueID1 = finalUniqueID;
        dialogDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lan == "" || lon == "") {
                    FancyToast.makeText(getContext(),"Please enable gps service", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                } else if (phone == "") {
                    phoneNumber.requestFocus();
                } else {
                    CartFragment.makeOrder(finalUniqueID1, lan, lon);
                    FancyToast.makeText(getContext(), "We have received your order Successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                    dismiss();
                }

            }
        });

        launchGMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, test, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MapActivity.class);

                startActivityForResult(intent, REQUEST_CODE_B);

            }
        });
        toolbar.setTitle("Additional Confirmation");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dismiss();
                return true;
            }
        });
    }

    public static CheckoutAdapter display(FragmentManager fragmentManager) {
        CheckoutAdapter exampleDialog = new CheckoutAdapter();
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_B:
                SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("UserLocation", context.MODE_PRIVATE);
                lan = sharedPreferences.getString("Latitude", "0");
                lon = sharedPreferences.getString("Longitude", "0");
                address.setText(lan + "," + lon);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}

