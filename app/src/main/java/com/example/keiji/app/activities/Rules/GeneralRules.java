package com.example.keiji.app.activities.Rules;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keiji.app.activities.R;

public class GeneralRules extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_general_rules, container, false);
        TextView rulesText = result.findViewById(R.id.general_view);
        rulesText.setMovementMethod(new ScrollingMovementMethod());
        return result;
    }
}