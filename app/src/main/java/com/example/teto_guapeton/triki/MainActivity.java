package com.example.teto_guapeton.triki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] botones = new Button[3][3];

    private boolean jugador1_Turno = true;

    private int rondas;

    private int judador1puntos;
    private int judador2puntos;

    private TextView textViewjugador1;
    private TextView textViewjugador2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewjugador1 = findViewById(R.id.text_view_p1);
        textViewjugador2 = findViewById(R.id.text_view_p2);

        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                String botonid = "boton_" + i + j;
                int resID = getResources().getIdentifier(botonid,"id", getPackageName());
                botones[i][j]=findViewById(resID);
                botones[i][j].setOnClickListener(this);
            }
        }

        Button boton_Reset = findViewById(R.id.boton_reset);

        boton_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               reiniciar_juego();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")){
            return;
        }

        if(jugador1_Turno){
            ((Button) v).setText("x");
        }else{
            ((Button) v).setText("o");
        }

        rondas++;

        if (ganador()){
            if (jugador1_Turno){
                jugador1_ganador();
            }else {
                jugador2_ganador();
            }
        }else if (rondas == 9){
            empate();
        }else{
            jugador1_Turno = !jugador1_Turno;
        }
    }

    private boolean ganador(){
        String[][] field = new String[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                field[i][j] = botones[i][j].getText().toString();

            }
        }
        //se verifica a un ganador por las lineas verticales u horizontales
        for(int i=0; i<3; i++){
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")){
                return true;
            }
        }
        for(int i=0; i<3; i++){
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")){
                return true;
            }
        }
        //se verifica a un ganador por las diagoanles
        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")){
            return true;
        }
        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")){
            return true;
        }
        return false;
    }

    private void jugador1_ganador(){
        judador1puntos++;
        Toast.makeText(this,"Jugador 1 gana",Toast.LENGTH_SHORT).show();
        actualizar_puntos();
        reiniciar_tablero();
    }
    private void jugador2_ganador(){
        judador2puntos++;
        Toast.makeText(this,"Jugador 2 gana",Toast.LENGTH_SHORT).show();
        actualizar_puntos();
        reiniciar_tablero();
    }
    private void empate(){
        Toast.makeText(this,"Que pasa dogy!!",Toast.LENGTH_SHORT).show();
        reiniciar_tablero();
    }
    private void actualizar_puntos(){
    textViewjugador1.setText(" Jugador 1: " + judador1puntos);
    textViewjugador2.setText(" Jugador 2: " + judador2puntos);
    }
    private void reiniciar_tablero(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                botones[i][j].setText("");
            }
        }
        rondas = 0;
        jugador1_Turno = true;

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rondas=savedInstanceState.getInt("rondas");
        judador1puntos=savedInstanceState.getInt("jugador1puntos");
        judador2puntos=savedInstanceState.getInt("judador2puntos");
        jugador1_Turno=savedInstanceState.getBoolean("jugador1_Turno");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("rondas", rondas);
        outState.putInt("jugador1puntos", judador1puntos);
        outState.putInt("jugador2puntos", judador2puntos);
        outState.putBoolean("jugador1_turno", jugador1_Turno);
    }

    private void reiniciar_juego(){

        judador1puntos=0;
        judador2puntos=0;
        actualizar_puntos();
        reiniciar_tablero()
    }
}
