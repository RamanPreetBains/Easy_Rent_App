package com.example.easyrent.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.easyrent.R;
import com.example.easyrent.extra.CardDetailsData;
import com.example.easyrent.extra.SessionData;

public class PaymentFragment extends Fragment {
    private Button btn_edit;
    private boolean check;
    private EditText et_cvv, et_cc_number;
    private EditText etYear, etMonth;
    private CardView addCardLayout;
    private CardView cardView;
    String[] monthArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String[] yearArray = {"2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032"};
    private String month = "01";
    private String year = "2021 ";
    private TextView tv_cvv, tv_exp_date, tv_user_name, tv_card_number;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
        initViews(root);
        initViewsWithData();
        return root;
    }

    private void initViews(View root) {
        btn_edit = root.findViewById(R.id.btn_edit);
        et_cvv = root.findViewById(R.id.et_cvv);
        et_cc_number = root.findViewById(R.id.et_cc_number);
        etYear = root.findViewById(R.id.et_year);
        etMonth = root.findViewById(R.id.et_month);
        addCardLayout = root.findViewById(R.id.rl_card_edit);
        cardView = root.findViewById(R.id.cv_credit_card);
        tv_cvv = root.findViewById(R.id.tv_cvv);
        tv_exp_date = root.findViewById(R.id.tv_exp_date);
        tv_user_name = root.findViewById(R.id.tv_user_name);
        tv_card_number = root.findViewById(R.id.tv_card_number);
    }

    private void initViewsWithData() {

        if (SessionData.I.localData.cardDetailsData != null) {
            check = false;
            btn_edit.setText("Edit");
            cardView.setVisibility(View.VISIBLE);
            addCardLayout.setVisibility(View.GONE);
            showCardDetails();
        } else {
            check = true;
            btn_edit.setText("Add");
            cardView.setVisibility(View.GONE);
            addCardLayout.setVisibility(View.VISIBLE);
        }
        btn_edit.setOnClickListener(v -> {
            if(check) {
                if (isValidate()) {
                    CardDetailsData cardDetailsData = new CardDetailsData();
                    cardDetailsData.setCardNumber(et_cc_number.getText().toString().trim());
                    cardDetailsData.setCvv(et_cvv.getText().toString().trim());
                    cardDetailsData.setMonth(etMonth.getText().toString().trim());
                    cardDetailsData.setYear(etYear.getText().toString().trim());
                    cardDetailsData.setUserName(SessionData.I.localData.currentUserData.getFirstName() + " " + SessionData.I.localData.currentUserData.getLastName());
                    SessionData.I.localData.cardDetailsData = cardDetailsData;
                    SessionData.I.saveLocalData();
                    btn_edit.setText("Edit");
                    cardView.setVisibility(View.VISIBLE);
                    addCardLayout.setVisibility(View.GONE);
                    showCardDetails();
                    check = false;
                }
            }else
            {
                btn_edit.setText("Add");
                cardView.setVisibility(View.GONE);
                addCardLayout.setVisibility(View.VISIBLE);
                check = true;
            }

        });
    }

    private void showCardDetails() {
        tv_cvv.setText(SessionData.I.localData.cardDetailsData.getCvv());
        tv_exp_date.setText(SessionData.I.localData.cardDetailsData.getMonth() + "/" + SessionData.I.localData.cardDetailsData.getYear());
        tv_user_name.setText(SessionData.I.localData.cardDetailsData.getUserName());
        tv_card_number.setText(SessionData.I.localData.cardDetailsData.getCardNumber());
    }

    private boolean isValidate() {
        boolean isValid = true;
        if (et_cc_number.getText().toString().trim().isEmpty() ||
                etMonth.getText().toString().trim().isEmpty() ||
                etYear.getText().toString().trim().isEmpty() ||
                et_cvv.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        int cardLength = Integer.parseInt(String.valueOf(et_cc_number.getText().toString().length()));
        if (cardLength < 14 || cardLength > 16) {
            et_cc_number.setError("Please enter card number between 14-16 digit");
            isValid = false;
        }
        if (Integer.parseInt(etMonth.getText().toString().trim()) > 12 || Integer.parseInt(etMonth.getText().toString().trim()) < 1) {
            etMonth.setError("Set month between 1 to 12");
            isValid = false;
        }
        if (Integer.parseInt(etYear.getText().toString().trim()) > 2030 || Integer.parseInt(etYear.getText().toString().trim()) < 2021) {
            etYear.setError("Set year between 2021 to 2030");
            isValid = false;
        }
        if (et_cvv.getText().toString().trim().length() > 3 || et_cvv.getText().toString().trim().length() < 1) {
            et_cvv.setError("set CVV between 3-4 digits");
            isValid = false;
        }

        return isValid;
    }
}