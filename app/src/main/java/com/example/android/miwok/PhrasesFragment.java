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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

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


    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> phrases = new ArrayList<>();

        phrases.add(new Word("Where are you going?",
                "minto wuksus", R.raw.phrase_where_are_you_going));
        phrases.add(new Word("What is your name?",
                "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        phrases.add(new Word("My name is...",
                "oyaaset...", R.raw.phrase_my_name_is));
        phrases.add(new Word("How are you feeling?",
                "michәksәs?", R.raw.phrase_how_are_you_feeling));
        phrases.add(new Word("I’m feeling good.",
                "kuchi achit", R.raw.phrase_im_feeling_good));
        phrases.add(new Word("Are you coming?",
                "әәnәs'aa?", R.raw.phrase_are_you_coming));
        phrases.add(new Word("Yes, I’m coming.",
                "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        phrases.add(new Word("I’m coming.",
                "әәnәm", R.raw.phrase_im_coming));
        phrases.add(new Word("Let’s go.",
                "yoowutis", R.raw.phrase_lets_go));
        phrases.add(new Word("Come here.",
                "әnni'nem", R.raw.phrase_come_here));

        WordAdapter adapter = new WordAdapter(getActivity(), phrases, getResources().getColor(R.color.category_phrases));

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);

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
