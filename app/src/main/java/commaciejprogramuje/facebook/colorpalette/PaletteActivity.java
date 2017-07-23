package commaciejprogramuje.facebook.colorpalette;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class PaletteActivity extends AppCompatActivity {

    private static final String SIMPLE_NAME = PaletteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                addColor();
            }
        });

        Log.d(SIMPLE_NAME, "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(SIMPLE_NAME, "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(SIMPLE_NAME, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(SIMPLE_NAME, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(SIMPLE_NAME, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(SIMPLE_NAME, "onPause");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_palette, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            return true;
        } else if (id == R.id.action_add) {
            addColor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addColor() {
        Intent intent = new Intent(PaletteActivity.this, ColorActivity.class);
        startActivity(intent);
    }
}
