package com.web2rev.DotConnect;

import java.util.Vector;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import java.lang.Runnable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class DotConnectView extends SurfaceView implements
SurfaceHolder.Callback, Runnable {
	final int ANIMATION_SLEEP = 8;
	private Bitmap mBitmap = null;
	public Bitmap blue_animation[] = new Bitmap[13];
	public Bitmap red_animation[] = new Bitmap[13];
	public Bitmap animation[] = null;
	public Bitmap board_image = null;
	public Bitmap red_checker = null;
	public Bitmap blue_checker = null;
	Canvas mCanvas;
	public DotConnectActivity mainActivity = null;
	public SurfaceHolder mSurfaceHolder;
	public int entered_move;
	public int move;
	public int moveplayer = -1;
	public boolean initialPaint = true;
	public boolean moveinprogress = false;
	public Thread viewThread = null;
	public boolean won = false;
	public int mBoardHeight = 1;
	public int mBoardWidth = 1;
	public int mCanvasHeight = 1;
	public int mCanvasWidth = 1;
	public Bitmap mBackgroundImage = null;

	public DotConnectView(Context context) {
		super(context);
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mainActivity = (DotConnectActivity) context;
		loadImages();
	}
	protected void loadImages() {
		mBackgroundImage = board_image = BitmapFactory
		.decodeStream(mainActivity.getResources().openRawResource(
				R.drawable.con4));
		mCanvasWidth = mBoardWidth = board_image.getWidth();
		mCanvasHeight = mBoardHeight = board_image.getHeight();
		blue_checker = BitmapFactory.decodeStream(mainActivity.getResources()
				.openRawResource(R.drawable.blue));
		red_checker = BitmapFactory.decodeStream(mainActivity.getResources()
				.openRawResource(R.drawable.red));
		blue_animation[0] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b0));
		red_animation[0] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r0));
		blue_animation[1] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b1));
		red_animation[1] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r1));
		blue_animation[2] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b2));
		red_animation[2] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r2));
		blue_animation[3] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b3));
		red_animation[3] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r3));
		blue_animation[4] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b4));
		red_animation[4] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r4));
		blue_animation[5] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b5));
		red_animation[5] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r5));
		blue_animation[6] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b6));
		red_animation[6] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r6));
		blue_animation[7] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b7));
		red_animation[7] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r7));
		blue_animation[8] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b8));
		red_animation[8] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r8));
		blue_animation[9] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b9));
		red_animation[9] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r9));
		blue_animation[10] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b10));
		red_animation[10] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r10));
		blue_animation[11] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b11));
		red_animation[11] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r11));
		blue_animation[12] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.b12));
		red_animation[12] = BitmapFactory.decodeStream(mainActivity
				.getResources().openRawResource(R.drawable.r12));
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		int curW = mBitmap != null ? mBitmap.getWidth() : 0;
		int curH = mBitmap != null ? mBitmap.getHeight() : 0;
		if (curW >= w && curH >= h) {
			return;
		}

		if (curW < w)
			curW = w;
		if (curH < h)
			curH = h;

		Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
				Bitmap.Config.RGB_565);
		Canvas newCanvas = new Canvas();
		newCanvas.setBitmap(newBitmap);
		if (mBitmap != null) {
			newCanvas.drawBitmap(mBitmap, 0, 0, null);
		}
		mBitmap = newBitmap;
		mCanvas = newCanvas;
		drawMainBoard(newCanvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
	}

	public void drawMainBoard(Canvas c) {
		Canvas canvas = c;
		DotConnectActivity mainActivity = DotConnectActivity.mainActivity;
		GameEngine gameEngine = mainActivity.gameEngine;
		canvas.drawRGB(0, 0, 0);
		canvas.drawBitmap(mBackgroundImage, 0, 0, null);
		float yratio = (((float) this.mCanvasHeight / (float) this.mBoardHeight));
		float xratio = (((float) this.mCanvasWidth / (float) this.mBoardWidth));
		int x, y;
		for (x = 0; x < 7; ++x)
			for (y = 0; y < 7; ++y)
				if (gameEngine.draw_board[(y << 3) + x] != 0)
					canvas.drawBitmap(
							gameEngine.draw_board[(y << 3) + x] == 1 ? red_checker
									: blue_checker,
									(int) ((float) 70 + (x * 29) * (float) xratio),
									(int) ((float) (6 + x * 3 + 28 * y + 9) * (float) yratio),
									null);
		if (won)
			ShowWonLabel(canvas);
		else if (gameEngine.players_move)
			ShowYourTurnLabel(canvas);
		else
			ShowThinkingLabel(canvas);
	}
	protected Object sync=new Object();
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (moveinprogress)
			return false;
		synchronized (sync) {
			moveinprogress = true;
			int action = event.getActionMasked();
			Vector<Float> vx = new Vector<Float>();
			Vector<Float> vy = new Vector<Float>();
			Vector<Float> vPressure = new Vector<Float>();
			Vector<Float> vTouchMajor = new Vector<Float>();
			if (action != MotionEvent.ACTION_UP
					&& action != MotionEvent.ACTION_CANCEL) {
				int N = event.getHistorySize();
				int P = event.getPointerCount();
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < P; j++) {
						vx.add(event.getHistoricalX(j, i));
						vy.add(event.getHistoricalY(j, i));
						vPressure.add(event.getHistoricalPressure(j, i));
						vTouchMajor.add(event.getHistoricalTouchMajor(j, i));
					}
				}
				int cx = (int) event.getX(0);
				int cy = (int) event.getY(0);
				TryMove(cx, cy);
			}
			return true;
		}
	}

	public boolean TryMove(int x, int y) {
		if (initialPaint)
			return false;
		Log.i("DotConnectActivity", "DotConnectActivity.TryMove(" + x + "," + y
				+ ")");
		DotConnectActivity mainActivity = DotConnectActivity.mainActivity;
		GameEngine gameEngine = mainActivity.gameEngine;
		entered_move = (int) Math.floor((x - 60) / 29);
		float yy = ((float) y * ((float) this.mBoardHeight / (float) this.mCanvasHeight));
		float xx = ((float) x * ((float) this.mBoardWidth / (float) this.mCanvasWidth));
		if (yy > 5 && yy < 225 && xx > 60 && xx < 265
				&& gameEngine.players_move
				&& gameEngine.game_board[entered_move] == 0) {
			move = entered_move;
			moveplayer = -1;
			animation = this.blue_animation;
			(viewThread = new Thread(this)).start();
		}
		return true;
	}

	@Override
	public void run() {
		try {
			if (initialPaint) {
				Canvas c = null;
				try {
					c = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {
						try {
							Thread.sleep(ANIMATION_SLEEP);
						} catch (Exception e) {
							;
						}
						drawMainBoard(mCanvas);
						drawMainBoard(c);
					}
				} finally {
					if (c != null) {
						mSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
				initialPaint = false;
			} else {
				float yratio = (((float) this.mCanvasHeight / (float) this.mBoardHeight));
				float xratio = (((float) this.mCanvasWidth / (float) this.mBoardWidth));
				mainActivity.gameEngine.players_move = moveplayer == 1;
				mainActivity.gameEngine.EnableRestartButton(false);
				Canvas c = null;
				for (int xx = 0; xx < 64; xx++)
					mainActivity.gameEngine.draw_board[xx] = mainActivity.gameEngine.game_board[xx];
				DotConnectActivity.mainActivity.gameEngine.make_move(move,
						moveplayer);
				Log.i(mainActivity.gameEngine.TAG, "make_move_game=" + move
						+ " " + moveplayer);
				for (int yy = 0; yy < 7
				&& mainActivity.gameEngine.draw_board[(yy << 3)
				                                      + mainActivity.gameView.move] == 0; ++yy) {
					for (int a = 0; a < 13; ++a) {
						if (yy == 6
								|| (a > 6 && mainActivity.gameEngine.game_board[((yy + 1) << 3)
								                                                + mainActivity.gameView.move] != 0)) {
							break;
						}
						try {
							c = mSurfaceHolder.lockCanvas(null);
							synchronized (mSurfaceHolder) {
								try {
									Thread.sleep(ANIMATION_SLEEP);
								} catch (Exception e) {
									;
								}
								drawMainBoard(mCanvas);
								drawMainBoard(c);
								mCanvas.drawBitmap(
										animation[a],
										(int) ((float) 70 + (move * 29)
												* (float) xratio),
												(int) ((float) (6
														+ DotConnectActivity.mainActivity.gameView.move
														* 3 + 28 * yy + 9) * (float) yratio),
														null);
								c.drawBitmap(
										animation[a],
										(int) ((float) 70 + (move * 29)
												* (float) xratio),
												(int) ((float) (6
														+ DotConnectActivity.mainActivity.gameView.move
														* 3 + 28 * yy + 9) * (float) yratio),
														null);
							}
						} finally {
							if (c != null) {
								mSurfaceHolder.unlockCanvasAndPost(c);
							}
						}
					}
				}
				try {
					c = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {
						try {
							Thread.sleep(ANIMATION_SLEEP);
						} catch (Exception e) {
							;
						}
						won = false;
						if (mainActivity.gameEngine.win() != 0) {
							won = true;
							mainActivity.gameEngine.players_move = false;
						}
						for (int xx = 0; xx < 64; xx++)
							mainActivity.gameEngine.draw_board[xx] = mainActivity.gameEngine.game_board[xx];
						drawMainBoard(mCanvas);
						drawMainBoard(c);
					}
				} finally {
					if (c != null) {
						mSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
				if (!won && moveplayer == -1) {
					mainActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							DotConnectActivity.mainActivity.gameEngine.ply = 0;
							DotConnectActivity.mainActivity.gameEngine.player = -1;
							DotConnectActivity.mainActivity.gameEngine.start();
						}
					});
				} else {
					moveinprogress = false;
					mainActivity.gameEngine.EnableRestartButton(true);
				}
			}
		} catch (Exception e) {
			Log.i(DotConnectActivity.TAG, e.getMessage() + e.getStackTrace());
		}
	}

	public void ShowWonLabel(Canvas c) {
		DrawText(c, moveplayer == 1 ? "I Won! Try Again!"
				: "You Won! Press Restart!", 95, 267, 24, 0xFFFF0000, null);
	}

	public void ShowYourTurnLabel(Canvas c) {
		DrawText(c, "Your Move", this.mCanvasWidth / 2 - 46,
				1, 23, 0xFFFF0000, null);
	}

	public void ShowThinkingLabel(Canvas c) {
		DrawText(c, "Thinking", this.mCanvasWidth / 2 - 46,
				1, 23, 0xFFFF0000, null);
	}

	protected void DrawText(Canvas canvas, String text, int x, int y,
			int fontSize, int c, Paint p) {
		Paint mTextPaint = p == null ? new Paint() : p;
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(fontSize);
		mTextPaint.setColor(c);
		canvas.drawText(text, x, y, mTextPaint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		setSurfaceSize(width, height);
	}

	public void setSurfaceSize(int width, int height) {
			mCanvasWidth = width;
			mCanvasHeight = height;
			mBackgroundImage = board_image.createScaledBitmap(board_image,
					width, height, true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		initialPaint = true;
		(viewThread = new Thread(this)).start();
		this.getResetButton().setEnabled(true);
		moveinprogress = false;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		try {
			boolean retry = true;
			while (retry) {
				try {
					viewThread.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
		} catch (Exception e) {
			;
		}
		try {
			boolean retry = true;
			while (retry) {
				try {
					DotConnectActivity.mainActivity.gameEngine.searchengine
					.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
		} catch (Exception ex) {
			;
		}
	}

	public Button getResetButton() {
		return (Button) DotConnectActivity.mainActivity
		.findViewById(R.id.buttonRestart);
	}
}
