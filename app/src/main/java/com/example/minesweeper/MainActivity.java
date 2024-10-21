package com.example.minesweeper;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int GRID_SIZE = 8;  // Tamaño del tablero
    private Button[][] buttons = new Button[GRID_SIZE][GRID_SIZE];
    private boolean[][] mines = new boolean[GRID_SIZE][GRID_SIZE];
    private int mineCount = 10;  // Número de minas
    private TextView tvMineCount, tvTimer;
    private Handler timerHandler = new Handler();
    private int timeElapsed = 0;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMineCount = findViewById(R.id.tvMineCount);
        tvTimer = findViewById(R.id.tvTimer);
        Button btnReset = findViewById(R.id.btnReset);
        GridLayout mineGrid = findViewById(R.id.mineGrid);

        // Inicia el tablero
        initializeBoard(mineGrid);

        // Inicia el temporizador
        startTimer();

        // Reiniciar juego
        btnReset.setOnClickListener(v -> resetGame());
    }

    private void initializeBoard(GridLayout mineGrid) {
        mineGrid.removeAllViews();
        Random random = new Random();
        mineCount = 10;
        gameOver = false;
        tvMineCount.setText("Mines: " + mineCount);

        // Inicializa las minas
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                mines[i][j] = false;  // Sin minas inicialmente

                // Crea el botón
                buttons[i][j] = new Button(this);
                buttons[i][j].setLayoutParams(new GridLayout.LayoutParams());
                buttons[i][j].setOnClickListener(new CellClickListener(i, j));

                // Agrega el botón al GridLayout
                mineGrid.addView(buttons[i][j]);
            }
        }

        // Coloca las minas en posiciones aleatorias
        for (int i = 0; i < mineCount; i++) {
            int row = random.nextInt(GRID_SIZE);
            int col = random.nextInt(GRID_SIZE);
            mines[row][col] = true;  // Coloca una mina
        }
    }

    private class CellClickListener implements View.OnClickListener {
        private int row, col;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View view) {
            if (gameOver) return;

            // Si la celda tiene una mina
            if (mines[row][col]) {
                buttons[row][col].setText("M");
                gameOver = true;
                tvMineCount.setText("Game Over!");
            } else {
                buttons[row][col].setText("0");
            }
        }
    }

    private void startTimer() {
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gameOver) {
                    timeElapsed++;
                    tvTimer.setText("Time: " + timeElapsed);
                    timerHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void resetGame() {
        timeElapsed = 0;
        tvTimer.setText("Time: 0");
        initializeBoard(findViewById(R.id.mineGrid));
    }
}