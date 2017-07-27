package commaciejprogramuje.facebook.colorpalette;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PaletteActivity extends AppCompatActivity implements ColorAdapter.ColorClickedListener {
    public static final int REQUEST_CODE_CREATE = 1;
    public static final int REQUEST_CODE_EDIT = 2;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.colorRecyclerView)
    RecyclerView colorRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private ColorAdapter colorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addColor();
            }
        });

        colorAdapter = new ColorAdapter(getLayoutInflater(), PreferenceManager.getDefaultSharedPreferences(this));
        colorAdapter.setColorClickedListener(this);
        colorRecyclerView.setLayoutManager(new LinearLayoutManager(PaletteActivity.this));
        colorRecyclerView.setAdapter(colorAdapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                colorAdapter.remove(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(colorRecyclerView);
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
            colorAdapter.clear();
            return true;
        } else if (id == R.id.action_add) {
            addColor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addColor() {
        Intent intent = new Intent(PaletteActivity.this, ColorActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CREATE) {
                String colorInHex = data.getStringExtra(ColorActivity.COLOR_IN_HEX_KEY);
                Snackbar.make(fab, getString(R.string.new_color_created, colorInHex), Snackbar.LENGTH_LONG)
                        .show();
                colorAdapter.addColor(colorInHex);
            } else if (requestCode == REQUEST_CODE_EDIT) {
                String colorInHex = data.getStringExtra(ColorActivity.COLOR_IN_HEX_KEY);
                String oldColor = data.getStringExtra(ColorActivity.OLD_COLOR_KEY);

                colorAdapter.replace(oldColor, colorInHex);
            }
        }

    }

    @Override
    public void onColorClicked(String colorInHex) {
        Intent intent = new Intent(PaletteActivity.this, ColorActivity.class);
        intent.putExtra(ColorActivity.OLD_COLOR_KEY, colorInHex);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }
}
