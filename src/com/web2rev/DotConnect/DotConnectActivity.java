package com.web2rev.DotConnect;

import android.app.Activity;
import android.graphics.Canvas;
import android.widget.ImageView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.Vector;

public class DotConnectActivity extends Activity{
	public static String TAG = "com.web2rev.DotConnect.DotConnectActivity";
	public static DotConnectActivity mainActivity;
	public GameEngine gameEngine = null;
	public DotConnectView gameView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = this;
		setContentView(R.layout.main);
		Spinner spinnerLevel = (Spinner) findViewById(R.id.spinnerLevel);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.arrayLevels,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLevel.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				gameEngine.levelChoice = position;
				Log.i(TAG, "position=" + position + " id=" + id);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				Log.i(TAG, "onNothingSelected");
			}
		});
		gameEngine = new GameEngine(this);
		ShowBoard();

	}
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	public void ShowBoard() {
		DotConnectView boardImageView = new DotConnectView(this);
		LinearLayout ll = (LinearLayout) findViewById(R.id.boardLayout);
		ll.addView(boardImageView);
		gameView = boardImageView;
		
		gameView.getResetButton().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gameEngine.resetGame();
				gameView.initialPaint = true;
				gameView.moveinprogress = false;
				gameView.moveplayer = -1;
				gameView.won = false;
				(gameView.viewThread = new Thread(gameView)).start();
			}
		});
	}
}