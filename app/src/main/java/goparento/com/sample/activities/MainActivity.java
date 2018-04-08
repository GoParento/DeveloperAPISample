package goparento.com.sample.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import goparento.com.sample.Utilities.Config;
import goparento.com.sample.fragments.ShortsFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openShortsFragment(Config.shortsFeedInEnglish);  // TO CHANGE URLS SEE CONFIG FILE.

    }

    public void openShortsFragment(String feedUrl) {

        ShortsFragment fragment = ShortsFragment.newInstance(feedUrl);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, fragment);
        transaction.commit();
    }


 /*
    If you want to use you in TabActivity you can call fragment like

    return ShortsFragment.newInstance(feedUrl);

 */



}
