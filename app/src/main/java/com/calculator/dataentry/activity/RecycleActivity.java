package com.calculator.dataentry.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.calculator.dataentry.R;
import com.calculator.dataentry.common.APIInterface;
import com.calculator.dataentry.common.ConnectionDetector;

import java.util.Calendar;

public class RecycleActivity extends AppCompatActivity {

    ProgressDialog pd;
    ConnectionDetector cd;
    APIInterface apiInterface ;
    LinearLayout llLedger,llcalender,llgallery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclebin_layout);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recycle Bin");

        llLedger=(LinearLayout)findViewById(R.id.ledger);
        llcalender=(LinearLayout)findViewById(R.id.calender);
        llgallery=(LinearLayout)findViewById(R.id.gallery);

        llLedger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent in = new Intent(RecycleActivity.this,ReLedgerActivity.class);
               startActivity(in);
            }
        });
        llcalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RecycleActivity.this,ReCalenderActivity.class);
                startActivity(in);
            }
        });
        llgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RecycleActivity.this,ReGalleryActivity.class);
                startActivity(in);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
