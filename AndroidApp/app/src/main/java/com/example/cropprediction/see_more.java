package com.example.cropprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class see_more extends AppCompatActivity {
    Button bk,br,bz,back;
    ImageView kn,kph,kp,rn,rph,rp,zn,zph,zp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);
        bk = (Button) findViewById(R.id.bk);
        br = (Button) findViewById(R.id.br);
        bz = (Button) findViewById(R.id.bz);
        back = (Button) findViewById(R.id.back);
        kn = (ImageView) findViewById(R.id.nk);
        kph = (ImageView) findViewById(R.id.phk);
        kp = (ImageView) findViewById(R.id.pk);
        rn = (ImageView) findViewById(R.id.nr);
        rph = (ImageView) findViewById(R.id.phr);
        rp = (ImageView) findViewById(R.id.pr);
        zn = (ImageView) findViewById(R.id.nz);
        zp = (ImageView) findViewById(R.id.pz);
        zph = (ImageView) findViewById(R.id.phz);
        kn.setVisibility(View.GONE);
        kp.setVisibility(View.GONE);
        kph.setVisibility(View.GONE);
        zn.setVisibility(View.GONE);
        rn.setVisibility(View.GONE);
        rp.setVisibility(View.GONE);
        zp.setVisibility(View.GONE);
        zph.setVisibility(View.GONE);
        rph.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(see_more.this, CropsPredictedActivity.class));
            }
        });

        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kn.setVisibility(View.VISIBLE);
                kp.setVisibility(View.VISIBLE);
                kph.setVisibility(View.VISIBLE);
                zn.setVisibility(View.GONE);
                rn.setVisibility(View.GONE);
                rp.setVisibility(View.GONE);
                zp.setVisibility(View.GONE);
                zph.setVisibility(View.GONE);
                rph.setVisibility(View.GONE);
            }
        });
        bz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zn.setVisibility(View.VISIBLE);
                zp.setVisibility(View.VISIBLE);
                zph.setVisibility(View.VISIBLE);
                kn.setVisibility(View.GONE);
                rn.setVisibility(View.GONE);
                rp.setVisibility(View.GONE);
                kp.setVisibility(View.GONE);
                zph.setVisibility(View.GONE);
                kph.setVisibility(View.GONE);
            }
        });
        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rn.setVisibility(View.VISIBLE);
                rp.setVisibility(View.VISIBLE);
                rph.setVisibility(View.VISIBLE);
                kn.setVisibility(View.GONE);
                zn.setVisibility(View.GONE);
                zp.setVisibility(View.GONE);
                kp.setVisibility(View.GONE);
                zph.setVisibility(View.GONE);
                kph.setVisibility(View.GONE);
            }
        });
    }
}