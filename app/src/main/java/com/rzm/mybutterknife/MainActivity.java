package com.rzm.mybutterknife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.BindView;
import com.rzm.butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text1)
    TextView world;

    @BindView(R.id.text2)
    TextView bitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        world.setText("aaaaa");
    }
}
