package comcesar1287.github.tagyou.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import comcesar1287.github.tagyou.R;

public class ConfigActivity extends AppCompatActivity {

    TextView textViewRadius, textViewAge;
    SeekBar seekBarRadius, seekBarAge;
    Switch swiMasc, swiFem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        initializeVariables();

        textViewRadius.setText("Selecionado: " + seekBarRadius.getProgress() + "/" + seekBarRadius.getMax());
        textViewAge.setText("Selecionado: " + seekBarAge.getProgress() + "/" + seekBarAge.getMax());

        radiusCurrent();
        ageCurrent();

    }


    private void initializeVariables() {
        textViewRadius = (TextView) findViewById(R.id.tv_radius);
        textViewAge = (TextView) findViewById(R.id.tv_age);

        seekBarRadius = (SeekBar) findViewById(R.id.sb_radius);
        seekBarAge = (SeekBar) findViewById(R.id.sb_age);

        swiMasc  = (Switch) findViewById(R.id.s_masc);
        swiFem  = (Switch) findViewById(R.id.s_fem);
    }

    public void radiusCurrent(){
        seekBarRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 2;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewRadius.setText("Selecionado: " + progress + "/" + seekBar.getMax());
            }
        });
    }

    public void ageCurrent(){
        seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textViewAge.setText("Selecionado: " + progress + "/" + seekBar.getMax());
            }
        });
    }


}
