package goparento.com.sample.universalRecycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import goparento.com.sample.Utilities.BasicCallBack;


/**
 * Created by mohit on 1/7/16.
 */

public class UniversalRecyclerView extends RecyclerView {

    private UniversalRecyclerAdapter mAdapter;



    public UniversalRecyclerView(Context context) {
        super(context);
        if (!isInEditMode())
            init(context);
    }

    public UniversalRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context);

    }

    public UniversalRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init(context);

    }

    private void init(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false);
        this.setLayoutManager(layoutManager);
        mAdapter = new UniversalRecyclerAdapter(this,context);
        setAdapter(mAdapter);
    }

    public void fill(String url){
        fill(url, null);
    }

    public void fill(String url , List<JSONObject> initialBlocks){
        fill(url, initialBlocks, null);
    }
    public void fill(List<JSONObject> initialBlocks){
        fill("", initialBlocks, null);
    }

    public void fill(String url , List<JSONObject> initialBlocks, BasicCallBack successResponseNoData){
        fill(url, initialBlocks, successResponseNoData, null);
    }

    public void fill(String url , List<JSONObject> initialBlocks, BasicCallBack successResponseNoData, final BasicCallBack onPresImageCountChangeCallback){
        mAdapter.fill(url, initialBlocks, successResponseNoData, onPresImageCountChangeCallback);
    }

    public void addDataOnBottom(JSONArray initialBlocks){
        mAdapter.addDataonBottom(initialBlocks);
        scrollToPosition(mAdapter.getItemCount()-1);
    }

    @Override
    public Adapter getAdapter(){
        return mAdapter;
    }

    public void clearAllBlocks() {
        mAdapter.clearData();
    }

}