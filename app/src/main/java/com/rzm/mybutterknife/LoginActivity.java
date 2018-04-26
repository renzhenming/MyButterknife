package com.rzm.mybutterknife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.BindView;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.text3)
    TextView text3;

    @BindView(R.id.text4)
    TextView text4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
