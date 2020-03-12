package com.example.android.miwok;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColorResource;

    public WordAdapter(Activity context, ArrayList<Word> list, int colorResource) {
        super(context, 0, list);
        mColorResource = colorResource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Word word = getItem(position);

        LinearLayout rootLayout = view.findViewById(R.id.baseView);
        rootLayout.setBackgroundColor(mColorResource);

        TextView local = view.findViewById(R.id.default_text_view);
        TextView miwokWord = view.findViewById(R.id.miwok_text_view);

        local.setText(word.getDefaultTranslation());
        miwokWord.setText(word.getMiwokTranslation());

        ImageView iconImage = view.findViewById(R.id.image);

        if (word.hasImage())
        {
            iconImage.setImageResource(word.getImageResourceId());
            iconImage.setVisibility(View.VISIBLE);
        }
        else
        {
            iconImage.setVisibility(View.GONE);
        }

        return view;
    }
}
