package com.example.appmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageButton btnPrev, btnPlay, btnStop, btnNext;

    ImageView imgHinh;
    List<Song> arraySong;
    int position = 0;

    Animation animation;

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        AddSong();
        KhoiTaoMediaPlayer();
        animation = AnimationUtils.loadAnimation(this, R.anim.disc_rotate);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if (position<0) {
                    position = arraySong.size()-1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position>arraySong.size()-1) {
                    position = 0;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                KhoiTaoMediaPlayer();
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.play_music);
                KhoiTaoMediaPlayer();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    // neu dang phat thi cho pause -> doi hinh play
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play_music);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                SetTimeTotal();
                UpdateTimeSong();
                imgHinh.startAnimation(animation);
            }
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void UpdateTimeSong() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhDangGio.format(mediaPlayer.getCurrentPosition()));
                //Update progress skSong
                skSong.setProgress(mediaPlayer.getCurrentPosition());

                // kiem tra thoi gian bai het neu ket thuc thi chuyen bai
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if (position>arraySong.size()-1) {
                            position = 0;
                        }
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        KhoiTaoMediaPlayer();
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.pause);
                        SetTimeTotal();
                        UpdateTimeSong();
                    }
                });

                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    private void SetTimeTotal() {
        SimpleDateFormat dinhDangGio = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhDangGio.format(mediaPlayer.getDuration()));
        // gan max của skSong = mediaPlayer.getDuration()
        skSong.setMax(mediaPlayer.getDuration());
    }
    private void KhoiTaoMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
    }

    private void AddSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Chắc có yêu là đây", R.raw.chac_co_yeu_la_day));
        arraySong.add(new Song("Đi về nhà", R.raw.di_ve_nha));
        arraySong.add(new Song("Em đừng đi", R.raw.em_dung_di));
        arraySong.add(new Song("Không thể say", R.raw.khong_the_say));
        arraySong.add(new Song("Mang tiền về cho mẹ", R.raw.mang_tien_ve_cho_me));
        arraySong.add(new Song("My love", R.raw.my_love));
        arraySong.add(new Song("Ngủ một mình", R.raw.ngu_mot_minh));
    }

    private void AnhXa() {
        txtTimeSong     = (TextView)  findViewById(R.id.textViewTimeSong);
        txtTitle        = (TextView)  findViewById(R.id.textViewTitle);
        txtTimeTotal    = (TextView)  findViewById(R.id.textViewTimeTotal);
        skSong          = (SeekBar) findViewById(R.id.seekBarSong);
        btnNext         = (ImageButton)  findViewById(R.id.imageButtonNext);
        btnPrev         = (ImageButton)  findViewById(R.id.imageButtonPrev);
        btnPlay         = (ImageButton)  findViewById(R.id.imageButtonPlay);
        btnStop         = (ImageButton)  findViewById(R.id.imageButtonStop);
        imgHinh         = (ImageView) findViewById(R.id.imageViewMusic);
    }
}