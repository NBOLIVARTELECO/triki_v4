package com.example.teto_guapeton.triki;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;



    private Button[][] botones = new Button[3][3];
    private ArrayList<Button> ultraBotones = new ArrayList<Button>();

    private TicTacToeGame game = new TicTacToeGame();

    private int rondas;

    private int judador1puntos;
    private int judador2puntos;
    ///
    private char name1;
    private char name2;
    ///
    private TextView textViewjugador1;
    private TextView textViewjugador2;

    private Toolbar toolbar;


    private SharedPreferences mPrefs;
    private TicTacToeGame mGame;
    private boolean mSoundOn;
    private TextView mInfoTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recuperación de datos desde la clase viewmodel
        MyViewModel model = ViewModelProviders.of(this).get(MyViewModel.class);
        game = model.game;

        bindSomeExtraShit();
        bindFuckingButtons();
        bindFuckingResetButton();
        setupFuckingToolbar();

        if (game.isGameInProgress()){
           drawGameInProgress();
        }else{
            startNewGame();
        }

        mHumanMediaPlayer = MediaPlayer.create(this,R.raw.click1);
        mComputerMediaPlayer = MediaPlayer.create(this,R.raw.click2);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound", true);

    }

    private void drawGameInProgress() {
        char[] savedGame = game.getBoard();
        for (int index = 0; index < ultraBotones.size(); index++) {
            final Button button = ultraBotones.get(index);
            if (savedGame[index]== TicTacToeGame.COMPUTER_PLAYER || savedGame[index] == TicTacToeGame.HUMAN_PLAYER){
            button.setText(""+savedGame[index]);
            } else {
                button.setText("");
            }

            button.setEnabled(true);
            final int finalIndex = index;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClick(button, finalIndex);
                    mHumanMediaPlayer.start();
                }
            });
        }
    }

    private void startNewGame() {
        game.clearBoard();

        for (int index = 0; index < ultraBotones.size(); index++) {
            final Button button = ultraBotones.get(index);
            button.setText("");
            button.setEnabled(true);
            final int finalIndex = index;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClick(button, finalIndex);
                    mHumanMediaPlayer.start();
                }
            });
        }
    }

    private void handleClick(Button button, int index) {
        if (button.isEnabled()) {
            setMove(TicTacToeGame.HUMAN_PLAYER, index);

            int winner = game.checkForWinner();
            if (winner == 0) {
                int move = game.getComputerMove();
                setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                winner = game.checkForWinner();
            }

            switch (winner) {
                case 0:
                    //showToast("It's your turn");
                    break;
                case 1:
                    showToast("Nadie Ganó");
                    startNewGame();
                    break;
                case 2:

                    String defaultMessage = getResources().getString(R.string.result_human_wins);
//                    mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
                    showToast("gano/////");
                    judador1puntos++;
                    actualizar_puntos();

                  //  reiniciar_tablero();
                    startNewGame();
                    break;
                default:
                    showToast("La máquina Ganó!");
                    judador2puntos++;
                    actualizar_puntos();
                    startNewGame();
                   // reiniciar_tablero();
            }
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void setMove(char player, int move) {
        game.setMove(player, move);

        Button button = ultraBotones.get(move);
        button.setEnabled(false);
        button.setText(""+player);
        if (player == TicTacToeGame.HUMAN_PLAYER) {
            button.setTextColor(Color.rgb(0, 200, 0));
          //  button.setBackground(this.getResources().getDrawable(R.drawable.o_1));
        } else {
            button.setTextColor(Color.rgb(200, 0, 0));
        }
    }


    private void bindSomeExtraShit() {
        textViewjugador1 = findViewById(R.id.text_view_p1);
        textViewjugador2 = findViewById(R.id.text_view_p2);
    }

    private void setupFuckingToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindFuckingResetButton() {
        Button boton_Reset = findViewById(R.id.boton_reset);

        boton_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
                mComputerMediaPlayer.start();
            }
        });
    }

    private void bindFuckingButtons() {
       ultraBotones.clear();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String botonid = "boton_" + i + j;
                int resID = getResources().getIdentifier(botonid, "id", getPackageName());
                Button button = findViewById(resID);
                ultraBotones.add(button);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu_1) {
        getMenuInflater().inflate(R.menu.menu_1, menu_1);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.editar:
                Toast.makeText(this, "Edición de jugador", Toast.LENGTH_SHORT).show();

                Dialog dialog = null;
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.Editar_jugadores);
                final CharSequence[] ediciones = {
                        getResources().getString(R.string.Editar_jugador1),
                        getResources().getString(R.string.Editar_jugador2)};
                int selected = 0;

                builder.setSingleChoiceItems(ediciones, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                final EditText editText = new EditText(MainActivity.this);
                                Dialog dialog_1 = null;
                                AlertDialog.Builder builder_1 = new AlertDialog.Builder(MainActivity.this);
                                builder_1.setTitle(R.string.Editar_jugador1);
                                builder_1.setView(editText);
                                builder_1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TextView textView = findViewById(R.id.text_view_p1);
                                        textView.setText(editText.getText());


                                    }
                                });
                                builder_1.setNegativeButton("Cancelar", null);

                                dialog_1 = builder_1.create();
                                dialog_1.show();
                                break;

                            case 1:

                                final EditText editText1 = new EditText(MainActivity.this);
                                Dialog dialog_2 = null;
                                AlertDialog.Builder builder_2 = new AlertDialog.Builder(MainActivity.this);
                                builder_2.setTitle(R.string.Editar_jugador2);
                                builder_2.setView(editText1);
                                builder_2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TextView textView = findViewById(R.id.text_view_p2);
                                        textView.setText(editText1.getText());

                                    }
                                });
                                builder_2.setNegativeButton("Cancelar", null);

                                dialog_2 = builder_2.create();
                                dialog_2.show();
                        }

                        Toast.makeText(getApplicationContext(), ediciones[which], Toast.LENGTH_SHORT).show();

                    }
                });

                dialog = builder.create();
                dialog.show();

                break;

            case R.id.borrar:
                Toast.makeText(this, "Has empezado de nuevo", Toast.LENGTH_SHORT).show();
                reiniciar_juego();
                break;

            case android.R.id.home:

                //
                final EditText editText1 = new EditText(MainActivity.this);
                Dialog dialog_2 = null;
                AlertDialog.Builder builder_2 = new AlertDialog.Builder(MainActivity.this);
                builder_2.setMessage("¿Seguro que quieres irte como una gallina?");
                builder_2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
                builder_2.setNegativeButton("Cancelar", null);

                dialog_2 = builder_2.create();
                dialog_2.show();
                //
                break;

            case R.id.Settings:
                startActivityForResult(new Intent(this, settings.class), 0);

                return true;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CANCELED) {
// Apply potentially new settings
            mSoundOn = mPrefs.getBoolean("sound", false);

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder)); /*
            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficulty(TicTacToeGame.DificultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficulty(TicTacToeGame.DificultyLevel.Harder);
            else
                mGame.setDifficulty(TicTacToeGame.DificultyLevel.Expert);*/
        }
    }

    @Override
    public void onClick(View v) {
    }


    private void reiniciar_juego() {

        judador1puntos = 0;
        judador2puntos = 0;
        actualizar_puntos();
        startNewGame();
    }

    private void actualizar_puntos(){
        textViewjugador1.setText(" Jugador 1: " + judador1puntos);
        textViewjugador2.setText(" Jugador 2: " + judador2puntos);
    }

}
