package com.chn.srecyclerviewsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                toActivity(R.layout.activity_sample);
                break;
            case R.id.button2:
                toActivity(R.layout.activity_sample2);
                break;
            case R.id.button3:
                toActivity(R.layout.activity_sample3);
                break;
            case R.id.button4:
                toActivity(R.layout.activity_sample4);
                break;
            case R.id.button5:
                toActivity(R.layout.activity_sample5);
                break;
        }

    }

    private void toActivity(int layoutId) {
        Intent intent = new Intent(this, SampleActivity.class);
        intent.putExtra("layoutId", layoutId);
        startActivity(intent);
    }


}
