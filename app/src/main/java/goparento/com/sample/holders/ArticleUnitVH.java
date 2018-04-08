package goparento.com.sample.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import goparento.com.sample.R;
import goparento.com.sample.Utilities.ActivitySwitchHelper;


/**
 * Created by Harsh on 2/12/17.
 */

public class ArticleUnitVH extends RecyclerView.ViewHolder {


    private ImageView articleImage;
    private TextView articleTitle;
    private String imageUrl, articleURl, articleText;


    public ArticleUnitVH(View itemView) {
        super(itemView);
        articleImage = itemView.findViewById(R.id.article_image);
        articleTitle = itemView.findViewById(R.id.article_title);

    }

    public void fill(final JSONObject jsonObject, final int pos) {

        articleText = jsonObject.optString("t");
        imageUrl = jsonObject.optJSONArray("subData").optJSONObject(0).optString("img");
        articleURl = jsonObject.optString("url");

        if (articleText != null)
            articleTitle.setText(articleText);

        if (imageUrl != null)
            Glide.with(itemView.getContext()).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(articleImage);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySwitchHelper.openInAppBrowser(articleURl, "GoParento App", itemView.getContext());

            }
        });


    }


}
