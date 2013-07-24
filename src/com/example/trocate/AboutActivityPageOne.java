package com.example.trocate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AboutActivityPageOne extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.about1);

		ImageButton imgBtn = (ImageButton) findViewById(R.id.oneBackBtn);
		imgBtn.setImageResource(R.drawable.eyeicon);
	}

	public void pageOne(View view){
		return;
	}

	public void pageTwo(View view){
		Intent intent = new Intent(this, AboutActivityPageTwo.class);
		this.startActivity(intent);
		this.finish();
	}

	public void backToMainPage(View view) {
		this.finish();
	}
}
