package commaciejprogramuje.facebook.colorpalette;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.szymczyk on 2017-07-25.
 */

public class ColorAdapter extends RecyclerView.Adapter<ColorViewHolder> {
    public static final String COLORS_KEY = "colors";
    private List<String> colors = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final SharedPreferences sharedPreferences;

    ColorClickedListener colorClickedListener;

    public ColorAdapter(LayoutInflater layoutInflater, SharedPreferences sharedPreferences) {
        this.layoutInflater = layoutInflater;
        this.sharedPreferences = sharedPreferences;

        try {
            String colorsJson = sharedPreferences.getString(COLORS_KEY, "[]");
            JSONArray jsonArray = new JSONArray(colorsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                colors.add(i, jsonArray.getString(i));
            }
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ColorViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, int position) {
        String colorInHex = colors.get(position);
        holder.setColor(colorInHex);
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public void addColor(String color) {
        colors.add(color);
        notifyItemInserted(colors.size() - 1);

        storeInPreferences();
    }

    private void storeInPreferences() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < colors.size(); i++) {
                jsonArray.put(i, colors.get(i));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(COLORS_KEY, jsonArray.toString());
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void remove(int position) {
        colors.remove(position);
        storeInPreferences();
    }

    public void clicked(int position) {
        if (colorClickedListener != null) {
            colorClickedListener.onColorClicked(colors.get(position));
        }
    }

    public void replace(String oldColor, String colorInHex) {
        int indexOf = colors.indexOf(oldColor);
        colors.set(indexOf, colorInHex);
        notifyItemChanged(indexOf);
        storeInPreferences();
    }

    public void clear() {
        colors.clear();
        notifyDataSetChanged();
        sharedPreferences.edit()
                .clear()
                .apply();
    }

    public interface ColorClickedListener {
        void onColorClicked(String colorInHex);
    }

    public void setColorClickedListener(ColorClickedListener colorClickedListener) {
        this.colorClickedListener = colorClickedListener;
    }
}

class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private String color;
    private TextView textView;
    private final ColorAdapter colorAdapter;


    public ColorViewHolder(View itemView, ColorAdapter colorAdapter) {
        super(itemView);
        textView = (TextView) itemView;
        textView.setOnClickListener(this);
        this.colorAdapter = colorAdapter;
    }

    public void setColor(String color) {
        this.color = color;
        textView.setText(color);
        textView.setBackgroundColor(Color.parseColor(color));
    }

    public String getColor() {
        return color;
    }

    @Override
    public void onClick(View view) {
        colorAdapter.clicked(getAdapterPosition());
    }
}
