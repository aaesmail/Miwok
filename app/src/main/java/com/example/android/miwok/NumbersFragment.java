package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    private MediaPlayer mediaPlayer = null;

    private AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener audioFocusChange = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                mediaPlayer.start();
            }
            else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {
                if (mediaPlayer != null)
                {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        }
    };

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(audioFocusChange);
        }
    };

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> numbersWords = new ArrayList<Word>();

        numbersWords.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        numbersWords.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        numbersWords.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        numbersWords.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        numbersWords.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        numbersWords.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        numbersWords.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        numbersWords.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        numbersWords.add(new Word("nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        numbersWords.add(new Word("ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), numbersWords, getResources().getColor(R.color.category_numbers));

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = (Word)parent.getItemAtPosition(position);
                try {
                    if (mediaPlayer != null)
                    {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    int result = audioManager.requestAudioFocus(audioFocusChange, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                    if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        mediaPlayer = MediaPlayer.create(getActivity(), word.getSoundResourceId());
                        mediaPlayer.start();

                        mediaPlayer.setOnCompletionListener(completionListener);
                    }
                } catch (Exception e)
                {
                    Toast.makeText(getActivity(), "Error Playing Media!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(audioFocusChange);
        }
    }
}
