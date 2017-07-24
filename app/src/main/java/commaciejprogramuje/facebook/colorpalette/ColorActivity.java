package commaciejprogramuje.facebook.colorpalette;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ColorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    public static final String RED = "red";
    public static final String GREEN = "green";
    public static final String BLUE = "blue";
    public static final String COLOR_IN_HEX = "color in hex";
    @Bind(R.id.redSeekBar)
    SeekBar redSeekBar;
    @Bind(R.id.greenSeekBar)
    SeekBar greenSeekBar;
    @Bind(R.id.blueSeekBar)
    SeekBar blueSeekBar;
    @Bind(R.id.generateButton)
    Button generateButton;
    @Bind(R.id.saveButton)
    Button saveButton;
    @Bind(R.id.colorLinearLayout)
    ScrollView colorLinearLayout;

    Random random = new Random();
    int red;
    int green;
    int blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        ButterKnife.bind(this);

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.generateButton)
    public void generate() {
        red = random.nextInt(256);
        green = random.nextInt(256);
        blue = random.nextInt(256);

        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        fillBackground();
    }

    private void fillBackground() {
        int color = Color.rgb(red, green, blue);
        colorLinearLayout.setBackgroundColor(color);
    }

    @OnClick(R.id.saveButton)
    public void save() {
        String hex = "#" + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue);
        Intent data = new Intent();
        data.putExtra(COLOR_IN_HEX, hex);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.redSeekBar:
                red = progress;
                break;
            case R.id.greenSeekBar:
                green = progress;
                break;
            case R.id.blueSeekBar:
                blue = progress;
                break;
        }
        fillBackground();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RED, red);
        outState.putInt(GREEN, green);
        outState.putInt(BLUE, blue);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        red = savedInstanceState.getInt(RED);
        green = savedInstanceState.getInt(GREEN);
        blue = savedInstanceState.getInt(BLUE);
    }
}
