package com.web2rev.DotConnect;

import java.lang.Runnable;
import android.util.Log;

public class GameEngine implements Runnable {
	public final String TAG = "com.web2rev.DotConnect.GameEngine";
	public int SEARCHPLY = 3;
	public int game_board[] = new int[64];
	public int draw_board[] = new int[64];
	public int searchmoves[] = new int[9];
	public int ply = 0;
	public int player = -1;
	public int levelChoiceOffset = 2;
	public int levelChoice = 0;
	public boolean players_move = true;
	public Thread searchengine = null;
	public DotConnectActivity mainActivity = null;

	public GameEngine(DotConnectActivity parentActivity) {
		mainActivity = parentActivity;
		resetGame();
	}

	public void EnableRestartButton(boolean enable) {
		if (enable) {
			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					DotConnectActivity.mainActivity.gameView.getResetButton()
							.setEnabled(true);
				}
			});
		} else {
			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					DotConnectActivity.mainActivity.gameView.getResetButton()
							.setEnabled(false);
				}
			});
		}
	}

	public void resetGame() {
		for (int x = 0; x < 64; x++)
			game_board[x] = draw_board[x] = 0;
		ply = 0;
		player = -1;
		players_move = true;
	}

	public void start() {
		if (!players_move) {
			searchengine = new Thread(this);
			searchengine.start();
			Log.i(TAG, "searchengine.start()");
		}
	}

	@Override
	public void run() {
		try {
			Log.i(TAG, "run()");
			SEARCHPLY = levelChoice + levelChoiceOffset;
			search();
			DotConnectActivity.mainActivity.gameView.move = searchmoves[0];
			DotConnectActivity.mainActivity.gameView.moveplayer = 1;
			DotConnectActivity.mainActivity.gameView.animation = DotConnectActivity.mainActivity.gameView.red_animation;
			(new Thread(DotConnectActivity.mainActivity.gameView)).start();
			searchengine = null;
			Log.i(TAG, "run() Complete");

		} catch (Exception e) {
			Log.i(TAG, e.getMessage() + e.getStackTrace());
		}
	}

	void unmake_move(int move) {
		int y;
		for (y = 0; y < 7; ++y)
			if (game_board[(y << 3) + move] != 0)
				break;
		game_board[(y << 3) + move] = 0;
	}

	void make_move(int move, int color) {
		int y;
		for (y = 0; y < 7; ++y)
			if (game_board[(y << 3) + move] != 0)
				break;
		game_board[((y - 1) << 3) + move] = color;
	}

	public int win() {
		int x, y, c, x2, y2, ct = 0;

		for (x = 0; x < 7; ++x)
			if (game_board[x] != 0)
				++ct;
		if (ct == 7)
			return 69; // tie
		for (y = 0; y < 7; ++y) {
			for (x = 0; x < 7; ++x) {
				if ((c = game_board[(x << 3) + y]) != 0) {
					// horizontal win
					for (ct = 0, y2 = y + 1; y2 < 7
							&& game_board[(x << 3) + y2] == c; ++y2, ++ct)
						;
					if (ct == 3) {
						return c;
					}
					// vertical win //
					for (ct = 0, x2 = x + 1; x2 < 7
							&& game_board[(x2 << 3) + y] == c; ++x2, ++ct)
						;
					if (ct == 3) {
						return c;
					}
					// downward diagnal win //
					for (ct = 0, x2 = x + 1, y2 = y + 1; x2 < 7 && y2 < 7
							&& game_board[(x2 << 3) + y2] == c; ++ct, ++x2, ++y2)
						;
					if (ct == 3) {
						return c;
					}
					// upward diagnal win //
					for (ct = 0, x2 = x + 1, y2 = y - 1; x2 < 7 && y2 >= 0
							&& game_board[(x2 << 3) + y2] == c; ++ct, ++x2, --y2)
						;
					if (ct == 3) {
						return c;
					}
				}
			}
		}
		return 0;
	}

	private int evaluate_position() {
		int x, y, c, x2, y2, ct, score = 0;

		for (y = 0; y < 7; ++y) {
			for (x = 0; x < 7; ++x) {
				if ((c = game_board[(x << 3) + y]) != 0) {
					if (y == 0 || y == 7)
						score -= 7;
					// horizontal win //
					for (ct = 0, y2 = y + 1; y2 < 7
							&& game_board[(x << 3) + y2] == c; ++y2, ++ct)
						;
					if (ct == 3)
						score += (c << 9);
					if (ct == 2)
						score += (c << 6);
					if (ct == 1)
						score += (c << 5);

					// vertical win //
					for (ct = 0, x2 = x + 1; x2 < 7
							&& game_board[(x2 << 3) + y] == c; ++x2, ++ct)
						;
					if (ct == 3)
						score += (c << 9);
					if (ct == 2)
						score += (c << 6);
					if (ct == 1)
						score += (c << 5);

					// downward diagnal win //
					for (ct = 0, x2 = x + 1, y2 = y + 1; x2 < 7 && y2 < 7
							&& game_board[(x2 << 3) + y2] == c; ++ct, ++x2, ++y2)
						;
					if (ct == 3)
						score += (c << 9);
					if (ct == 2)
						score += (c << 6);
					if (ct == 1)
						score += (c << 5);

					// upward diagnal win //
					for (ct = 0, x2 = x + 1, y2 = y - 1; x2 < 7 && y2 >= 0
							&& game_board[(x2 << 3) + y2] == c; ++ct, ++x2, --y2)
						;
					if (ct == 3)
						score += (c << 9);
					if (ct == 2)
						score += (c << 6);
					if (ct == 1)
						score += (c << 5);
				}
			}
		}
		return score + (int) Math.floor(Math.random() * 15);
	}

	private int search() {
		int ct, score, highscore, x;
		int generated_moves[];

		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			;
		}

		generated_moves = new int[9];

		++ply;
		player *= -1;
		highscore = player * -32000;

		if (ply < SEARCHPLY) {
			generate_moves(generated_moves);
			for (ct = 0; ct < 7 && generated_moves[ct] != 99; ct++) {
				make_move(generated_moves[ct], player);

				if ((score = win()) != 0)
					score = score * (10000 >> ply); // 10000/ply
				else
					score = search();

				if (player == 1) {
					if (score > highscore) {
						highscore = score;
						searchmoves[ply - 1] = generated_moves[ct];
					}
				} else {
					if (score < highscore) {
						highscore = score;
						searchmoves[ply - 1] = generated_moves[ct];
					}
				}
				unmake_move(generated_moves[ct]);
			}
		} else {

			--ply;
			player *= -1;
			return evaluate_position();
		}

		--ply;
		player *= -1;
		return highscore;
	}

	private void generate_moves(int moves[]) {
		int x, move_ct = 0;
		for (x = 0; x < 7; ++x)
			if (game_board[x] == 0)
				moves[move_ct++] = x;
		moves[move_ct] = 99; // magic number for end of list
	}

}
