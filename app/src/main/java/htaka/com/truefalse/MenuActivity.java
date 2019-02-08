package htaka.com.truefalse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MenuActivity extends AppCompatActivity {

    final private UnityAdsListener unityAdListener = new UnityAdsListener();
    InterstitialAd mInterstitialAd;
    Button retryBtn, displayBtn ,resultBtn;
    TextView scoreTxt, commentTxt;
    Intent i;
    int score = 0, noloop;
    List<Integer> videoTxt=new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupWindowAnimations();

        retryBtn = (Button) findViewById(R.id.button);
        resultBtn = (Button) findViewById(R.id.resultButton);
        displayBtn = (Button) findViewById(R.id.rewardedBtn);
        commentTxt = (TextView) findViewById(R.id.scoreText);
        scoreTxt = (TextView) findViewById(R.id.commenTxt);


        LoadAllAds();

        LoadContentComments();

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadInterstitialAd();
            }
        });

        displayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRewardedVideoAd();
            }
        });

        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GOTOResult();

            }
        });
    }

    private void GOTOResult() {
        Intent ru=new Intent(MenuActivity.this,ResultsActivity.class);
        ru.putExtra("Score",score);
        startActivity(ru);
    }

    private void GOTOMain() {
        Intent startOver=new Intent(MenuActivity.this,MainActivity.class);
        startActivity(startOver);
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);
        }
    }

    private void LoadContentVideo() {
        Random rand=new Random();
        VideoView mVid = (VideoView) findViewById(R.id.videoView);
        Uri video = Uri.parse("android.resource://htaka.com.truefalse/" + videoTxt.get(rand.nextInt(videoTxt.size())));
        mVid.setVideoURI(video);
        mVid.requestFocus();
        mVid.start();
    }

    private void LoadContentComments() {
        i = getIntent();
        score = i.getIntExtra("Score", score);
        commentTxt.setText("" + score);

        if (score == 0) {

            scoreTxt.setText("شنو هاذاااااا");

            videoTxt.add(R.raw.yakhara);

        } else if (score >= 1 && score < 10) {

            scoreTxt.setText("مبروووووك لقد وصلت اكثر من 0");

            videoTxt.add(R.raw.talama);
            videoTxt.add(R.raw.reapetlol);
            videoTxt.add(R.raw.funshout);
            videoTxt.add(R.raw.manclap);
            videoTxt.add(R.raw.altana);
            videoTxt.add(R.raw.lamtazbot);
            videoTxt.add(R.raw.epicry);

        } else if (score >= 10 && score < 109) {

            scoreTxt.setText("لااااا هيك كثير تقريبا ١٪ من الناس يوصلو النتيجة ");

            videoTxt.add(R.raw.monkey);
            videoTxt.add(R.raw.koldancea);
            videoTxt.add(R.raw.koldanceb);
            videoTxt.add(R.raw.jono);
            videoTxt.add(R.raw.funarabshoot);
            videoTxt.add(R.raw.takhmes);
            videoTxt.add(R.raw.happy);
            videoTxt.add(R.raw.shopdancing);
            videoTxt.add(R.raw.shalelha);

        } else{

            scoreTxt.setText("لقد فزت باللعبة الرجاء نشر النتيجة لكي تصبح الملك");

            videoTxt.add(R.raw.dancevideo);
        }
        LoadContentVideo();
    }

    private void LoadAllAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3120105269514394/7221098268");

        //Unity rewarded ad
        noloop=getIntent().getIntExtra("Loop",noloop);
        if( noloop == 0 ) {
            noloop++;
            if(!UnityAds.isInitialized()) {
                UnityAds.initialize(this, "1132400", unityAdListener);
            }else{
                displayBtn.setText("شاهد فيديو وأكمل اللعب");
                displayBtn.setEnabled(true);
            }
        } else {
            displayBtn.setVisibility(View.GONE);
        }

        //Interstitial Ad from AdMob
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3120105269514394/8697831460");
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void loadRewardedVideoAd() {
        if (UnityAds.isReady()) {
            UnityAds.show(this);
        } else {
            displayBtn.setEnabled(false);
        }
    }

    private void loadInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            GOTOMain();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void beginPlayingGame(int sc) {
        i = new Intent(MenuActivity.this, MainActivity.class);
        i.putExtra("Loop",noloop);
        i.putExtra("Score", sc);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
    }

    private class UnityAdsListener implements IUnityAdsListener {
        @Override
        public void onUnityAdsReady(String s) {
            displayBtn.setText("شاهد فيديو وأكمل اللعب");
            displayBtn.setEnabled(true);
        }

        @Override
        public void onUnityAdsStart(String s) {

        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
            if (finishState != UnityAds.FinishState.SKIPPED) {
                beginPlayingGame(score);
            }
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
            Log.d("Ads Unity", s);
        }
    }
}
