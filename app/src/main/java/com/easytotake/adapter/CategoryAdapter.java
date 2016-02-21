package com.easytotake.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.easytotake.R;
import com.easytotake.activity.CategoryDetailActivity;
import com.easytotake.anim.RoundedTransformation;
import com.easytotake.listener.RecyclerViewClickListener;
import com.easytotake.rest.AbstractCallback;
import com.easytotake.rest.RestClient;
import com.easytotake.rest.model.Category;
import com.easytotake.rest.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Response;

public class CategoryAdapter extends RecyclerView.Adapter implements RecyclerViewClickListener {
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROGRESS = 0;

    private static final int TAKE = 5;
    private static RecyclerViewClickListener itemListener;
    private int PAGE = -1;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private RecyclerView recyclerView;
    private boolean haveMoreData = true;
    private List<Category> categories = new ArrayList<>();
    private Context context = null;


    public CategoryAdapter(RecyclerView recyclerView, Context context) {

        this.context = context;
        itemListener = this;

        final GridLayoutManager linearLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (haveMoreData) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        loadMore();
                        loading = true;
                    }
                }

            }
        });


        linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return categories.get(position) != null ? 1 : 2;
            }
        });

        this.recyclerView = recyclerView;

    }


    public void removeLoading() {
        categories.remove(categories.size() - 1);
        notifyItemRemoved(categories.size());
    }


    public void addLoading() {
        categories.add(null);
        notifyItemInserted(categories.size() - 1);
    }

    public void loadMore() {
        addLoading();
        loadData();
    }

    private void loadData() {

        PAGE++;

        Call<List<Category>> call = RestClient.getService().categories(PAGE, TAKE);

        call.enqueue(new AbstractCallback<List<Category>>() {
            @Override
            public void onSuccess(Response<List<Category>> response) {

                removeLoading();

                for (Category category : response.body()) {
                    categories.add(category);
                    notifyItemInserted(categories.size());
                }
                setLoaded();

                if (response.body().size() == 0) {
                    haveMoreData = false;
                    Snackbar.make(recyclerView, "No More Categories", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //TODO handel exception
            }
        });
    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.item_category_view, parent, false);
            return new CellViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CellViewHolder) {
            ((CellViewHolder) holder).bindView(categories.get(position), context);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    @Override
    public int getItemViewType(int position) {
        return categories.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Category category = categories.get(position);
        CategoryDetailActivity.startCategoryDetailActivity(context, category.getOid());
    }

    public static class CellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.imageLiked)
        public ImageButton imageLiked;
        @Bind(R.id.tsLikesCounter)
        public TextSwitcher tsLikesCounter;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.picture)
        ImageView picture;
        @BindDimen(R.dimen.global_menu_avatar_size)
        int avatarSize;

        public CellViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        public void bindView(Category category, Context context) {

            int adapterPosition = getAdapterPosition();
            imageLiked.setImageResource(adapterPosition % 2 == 0 ? R.mipmap.ic_heart_red : R.mipmap.ic_heart_outline_grey);

            Picasso.with(context)
                    .load(Constants.Rest.BASE_URL + category.getPicture())
                    .placeholder(R.drawable.img_circle_placeholder)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new RoundedTransformation())
                    .into(picture);

            name.setText(category.getName());

            Random r = new Random();
            int result = r.nextInt(100 - 10) + 10;
            tsLikesCounter.setCurrentText(result + " likes");

        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view, getAdapterPosition());
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}
