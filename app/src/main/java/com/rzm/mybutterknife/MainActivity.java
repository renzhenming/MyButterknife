package com.rzm.mybutterknife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.BindView;
import com.rzm.butterknife.ButterKnife;
import com.rzm.butterknife.Unbinder;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text1)
    TextView world;

    @BindView(R.id.text2)
    TextView bitch;
    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        world.setText("hello");
        bitch.setText("i am not a angle do you know");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
