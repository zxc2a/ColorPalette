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


class ColorAdapter extends RecyclerView.Adapter<ColorViewHolder> {
    private static final String COLORS_KEY = "colors";
    private List<String> colors = new ArrayList<>();
    private final LayoutInflater layoutInflater;
    private final SharedPreferences sharedPreferences;

    private ColorClickedListener colorClickedListener;

    ColorAdapter(LayoutInflater layoutInflater, SharedPreferences sharedPreferences) {
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

    int addColor(String color) {
        colors.add(color);
        int position = colors.size() - 1;
        notifyItemInserted(position);
        storeInPreferences();
        return position;
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

    void remove(int position) {
        colors.remove(position);
        storeInPreferences();
    }

    void clicked(int position) {
        if (colorClickedListener != null) {
            colorClickedListener.onColorClicked(colors.get(position));
        }
    }

    void replace(String oldColor, String colorInHex) {
        int indexOf = colors.indexOf(oldColor);
        colors.set(indexOf, colorInHex);
        notifyItemChanged(indexOf);
        storeInPreferences();
    }

    void clear() {
        colors.clear();
        notifyDataSetChanged();
        sharedPreferences.edit()
                .clear()
                .apply();
    }

    interface ColorClickedListener {
        void onColorClicked(String colorInHex);
    }

    void setColorClickedListener(ColorClickedListener colorClickedListener) {
        this.colorClickedListener = colorClickedListener;
    }
}

class ColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView textView;
    private final ColorAdapter colorAdapter;


    ColorViewHolder(View itemView, ColorAdapter colorAdapter) {
        super(itemView);
        textView = (TextView) itemView;
        textView.setOnClickListener(this);
        this.colorAdapter = colorAdapter;
    }

    public void setColor(String color) {
        textView.setText(color);
        int backgroundColor = Color.parseColor(color);
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(PaletteActivity.getTextColorFromColor(backgroundColor));
    }

    @Override
    public void onClick(View view) {
        colorAdapter.clicked(getAdapterPosition());
    }
}
