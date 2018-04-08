package goparento.com.sample.universalRecycler;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import goparento.com.sample.R;
import goparento.com.sample.Utilities.API;
import goparento.com.sample.Utilities.ActivitySwitchHelper;
import goparento.com.sample.Utilities.BasicCallBack;
import goparento.com.sample.Utilities.UniversalAsynTask;
import goparento.com.sample.holders.ArticleUnitVH;


/**
 * Created by mohit on one/7/16.
 **/
public class UniversalRecyclerAdapter extends RecyclerView.Adapter {

    private static final Map<String, Integer> sMap = new Hashtable<>();
    private static final int TYPE_INVALID = -1;
    private static final int TYPE_CHANNEL = 0;
    private static final int TYPE_YOUTUBE_LIST = 1;
    private static final int TYPE_CAT_NAME = 2;
    private static final int TYPE_COMMENT = 3;
    private static final int TYPE_ADVIEW = 4;
    private static final int CREATE_WALLPAPER = 5;
    private static final int FONT = 6;
    private static final int START_VIEW = 7;
    private static final int QUOTE_VIEW = 8;
    private static final int PROMO_VIEW = 9;
    private static final int PRODUCT_VIEW = 10;
    private static final int RECYCLER_VIEW = 11;
    private static final int BUTTON_VIEW = 12;
    private static final int SHORTS_VIEW = 13;


    static {
        sMap.put("cl", TYPE_CHANNEL);
        sMap.put("yy", TYPE_YOUTUBE_LIST);
        sMap.put("cat", TYPE_CAT_NAME);
        sMap.put("comment", TYPE_COMMENT);
        sMap.put("ad", TYPE_ADVIEW);
        sMap.put("cw", CREATE_WALLPAPER);
        sMap.put("font", FONT);
        sMap.put("start", START_VIEW);
        sMap.put("qv", QUOTE_VIEW);
        sMap.put("pv", PROMO_VIEW);
        sMap.put("product", PRODUCT_VIEW);
        sMap.put("rv", RECYCLER_VIEW);
        sMap.put("button", BUTTON_VIEW);
        sMap.put("avh", SHORTS_VIEW);


    }

    UniversalRecyclerView universalRecyclerView;
    String lastUrl;
    ArrayList<JSONObject> blocks = new ArrayList<>();
    BasicCallBack successResponseNoDataCallback;
    private JSONObject loaderJson, loaderBlock;
    private int pastVisibleItems, totalItemCount;
    private int visibleItemCount;
    private BasicCallBack mbasicCallBack;
    private BasicCallBack fontClickCallback;
    private AsyncDownloadTask asyncDownloadTask;
    private boolean isLoaderAdded = false;
    private String response;
    private String nextPageURL;
    private Context context;


    public UniversalRecyclerAdapter(final UniversalRecyclerView universalRecyclerView, Context context) {
        /*
        * Initialize loaderJson;
        * */
        this.universalRecyclerView = universalRecyclerView;
        this.context = context;

        /*
        *  Set on scroll listener.
        * */
        universalRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            LinearLayoutManager mLayoutManager = (LinearLayoutManager) universalRecyclerView.getLayoutManager();

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && blocks.size() > 1) {
                    if ((mLayoutManager.getChildCount() + mLayoutManager
                            .findFirstVisibleItemPosition()) >= mLayoutManager.getItemCount()) {
                        if (!TextUtils.isEmpty(nextPageURL)) {
                            if (!nextPageURL.equals(lastUrl)) {
                                //addLoader(1);
                                if (!TextUtils.isEmpty(nextPageURL)) {
                                    AsyncDownloadTask loaderTask = new
                                            AsyncDownloadTask(nextPageURL, false);
                                    loaderTask.executeOnExecutor(AsyncTask
                                            .THREAD_POOL_EXECUTOR);
                                }

                            }
                        }
                    }
                }
            }
        });
    }


    public static int getTypeValue(String type) {
        if (type != null && sMap.containsKey(type)) {
            return sMap.get(type);
        }
        return TYPE_INVALID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        switch (viewType) {

            case SHORTS_VIEW:
                itemView = inflater.inflate(R.layout.article_unit, parent, false);
                viewHolder = new ArticleUnitVH(itemView);
                break;


        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        JSONObject block = blocks.get(position);

        switch (holder.getItemViewType()) {


            case SHORTS_VIEW:
                ((ArticleUnitVH) holder).fill(block, position);

                break;

        }

    }

    public void setOnItemCallBack(BasicCallBack onItemCallBack) {
        this.mbasicCallBack = onItemCallBack;


    }

    public void setFontClickCallback(BasicCallBack fontClickCallback) {
        this.fontClickCallback = fontClickCallback;


    }


    @Override
    public int getItemCount() {
        return blocks.size();
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject block = blocks.get(position);
        String type = block.optString("ty", "");
        return getTypeValue(type);
    }


    public void addToListEnd(List<JSONObject> dataList) {
        if (dataList == null) {
            return;
        }
        String type;
        JSONObject data;
        for (int idx = 0; idx < dataList.size(); ++idx) {
            data = dataList.get(idx);
            type = data.optString("ty");
            int typeVal = getTypeValue(type);
            if (typeVal == TYPE_INVALID) {
                dataList.remove(idx);
            }
        }
        int location;
        synchronized (blocks) {
            location = blocks.size();
        }


        blocks.addAll(dataList);

        if (dataList.size() == 1) {
            notifyItemInserted(location);
        } else {
            notifyItemRangeInserted(location, dataList.size());
        }

        // dataList.clear();
    }

    public ArrayList<JSONObject> getBlocks() {
        return blocks;
    }

    private void onDelete(String eid) {
        if (TextUtils.isEmpty(eid)) return;
        Iterator<JSONObject> itr = blocks.iterator();
        int i = -1;
        while (itr.hasNext()) {
            i++;
            JSONObject object = itr.next();
            if (eid.equals(object.optString("id"))) {
                blocks.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    private void addToBlocks(List<JSONObject> dataList) {
        boolean showToast = false;
        for (JSONObject i : dataList) {
            blocks.add(i);
        }

    }


    public void addDataonBottom(JSONArray initialBlocks) {
        int index = getItemCount();
        int count = 0;
        for (int i = index; i < initialBlocks.length(); i++) {
            try {
                blocks.add(i, initialBlocks.getJSONObject(count));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            count++;
            index++;
        }

        notifyItemInserted(count);

    }

    public void clearData() {
        blocks.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void fill(String url, List<JSONObject> initialBlocks, BasicCallBack successResponseNoData,
                     final BasicCallBack onPresImageCountChangeCallback) {
        blocks.clear();
        notifyDataSetChanged();
        this.successResponseNoDataCallback = successResponseNoData;
        addToListEnd(initialBlocks);
        if (!TextUtils.isEmpty(url)) {
            asyncDownloadTask = new AsyncDownloadTask(url, false);
            asyncDownloadTask.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR);
        }


    }

    protected class AsyncDownloadTask extends UniversalAsynTask {
        private final String url;
        String jsonResult;
        boolean isPreviousCall = false;

        AsyncDownloadTask(String url, boolean isPreviousCall) {
            lastUrl = url;
            this.url = url;
            this.isPreviousCall = isPreviousCall;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            jsonResult = API.makeServiceCall(url);
            response = jsonResult;

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!TextUtils.isEmpty(jsonResult)) {
                JSONObject response = null;
                try {
                    response = new JSONObject(jsonResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (response == null) {
                    return;
                }

                try {

                    if (isLoaderAdded && blocks.size() > 0) {
                        JSONObject exLoader = blocks.get(blocks.size() - 1);
                        if ("prgs".equals(exLoader.optString("ty"))) {
                            blocks.remove(blocks.size() - 1);
                            notifyItemRemoved(blocks.size());
                            isLoaderAdded = false;
                        } else {
                            isLoaderAdded = false;
                        }

                    }

                    JSONArray responseArray = response.optJSONArray("data");

                    if (responseArray == null) {
                        responseArray = response.optJSONArray("blocks");
                    }

                    if (responseArray != null && responseArray.length() > 0) {
                        nextPageURL = response.optString("next");
                        List<JSONObject> blocksToAdd = new ArrayList<>();
                        for (int i = 0; i < responseArray.length(); i++) {
                            blocksToAdd.add(responseArray.getJSONObject(i));
                        }
                        if (successResponseNoDataCallback != null) {
                            successResponseNoDataCallback.callBack(ActivitySwitchHelper.STATUS_SUCCESS, blocksToAdd);
                        }
                        addToListEnd(blocksToAdd);


                    } else if (blocks != null && blocks.size() == 0) {
                        if (successResponseNoDataCallback != null) {
                            successResponseNoDataCallback.callBack(ActivitySwitchHelper.STATUS_FAIL, null);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                ActivitySwitchHelper.showToast(context, "Check your Network connection !");
            }


        }
    }
}



