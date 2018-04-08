package goparento.com.sample.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import goparento.com.sample.R;
import goparento.com.sample.Utilities.ActivitySwitchHelper;
import goparento.com.sample.Utilities.BasicCallBack;
import goparento.com.sample.Utilities.CheckNetwork;
import goparento.com.sample.Utilities.Utils;
import goparento.com.sample.universalRecycler.UniversalRecyclerView;


public class ShortsFragment extends Fragment {

    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    String feedUrl;

    public static ShortsFragment newInstance(String url) {
        ShortsFragment myFragment = new ShortsFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shorts_page, container, false);
        if (!CheckNetwork.isInternetAvailable(inflater.getContext())) {
            view = inflater.inflate(R.layout.internet, container, false);
            TextView textView = (TextView) view.findViewById(R.id.open_setting);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));

                }
            });
            return view;
        }

        feedUrl = getArguments().getString("url");

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        final UniversalRecyclerView universalRecyclerView = view.findViewById(R.id.video_list_view);

        final BasicCallBack basicCallBack = new BasicCallBack() {
            @Override
            public void callBack(int status, Object data) {
                if (status == ActivitySwitchHelper.STATUS_SUCCESS) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };


        universalRecyclerView.fill(feedUrl, null, basicCallBack);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                universalRecyclerView.clearAllBlocks();
                universalRecyclerView.fill(feedUrl, null, basicCallBack);
                if (!CheckNetwork.isInternetAvailable(getContext())) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        (view.findViewById(R.id.go_parento_brand)).setVisibility(View.VISIBLE);
        (view.findViewById(R.id.go_parento_brand)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openGoParentoApp(getContext());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}


