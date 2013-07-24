package com.example.trocate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AboutActivityPageTwo extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayShowTitleEnabled(false);
		setContentView(R.layout.about2);

		ImageButton imgBtn = (ImageButton) findViewById(R.id.twoBackBtn);
		imgBtn.setImageResource(R.drawable.eyeicon);


	}

	public void pageOne(View view){
		Intent intent = new Intent(this, AboutActivityPageOne.class);
		this.startActivity(intent);
		this.finish();
	}

	public void pageTwo(View view){
		return;
	}

	public void backToMainPage(View view) {
		this.finish();
	}
}
