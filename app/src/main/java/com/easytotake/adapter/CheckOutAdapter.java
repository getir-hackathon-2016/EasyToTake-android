package com.easytotake.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytotake.R;
import com.easytotake.activity.ProductDetailActivity;
import com.easytotake.anim.RoundedTransformation;
import com.easytotake.listener.RecyclerViewClickListener;
import com.easytotake.rest.AbstractCallback;
import com.easytotake.rest.RestClient;
import com.easytotake.rest.model.Product;
import com.easytotake.rest.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Response;

public class CheckOutAdapter extends RecyclerView.Adapter implements RecyclerViewClickListener {
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROGRESS = 0;

    private static final int TAKE = 5;
    private static RecyclerViewClickListener itemListener;
    private RecyclerView recyclerView;

    private List<Product> products = new ArrayList<>();

    private Context context = null;

    public CheckOutAdapter(RecyclerView recyclerView, Context context) {
        this.context = context;
        itemListener = this;

        final GridLayoutManager linearLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

        linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        this.recyclerView = recyclerView;

    }


    public void removeLoading() {
        products.remove(products.size() - 1);
        notifyItemRemoved(products.size());
    }


    public void addLoading() {
        products.add(null);
        notifyItemInserted(products.size() - 1);
    }

    public void loadMore() {
        addLoading();
        loadData();
    }

    private void loadData() {

        Call<List<Product>> call = RestClient.getService().shoppingCard("1");

        call.enqueue(new AbstractCallback<List<Product>>() {
            @Override
            public void onSuccess(Response<List<Product>> response) {

                removeLoading();

                if (response.body() != null) {
                    for (Product product : response.body()) {
                        products.add(product);
                        notifyItemInserted(products.size());
                    }
                    if (response.body().size() == 0) {
                        Snackbar.make(recyclerView, "No More Shopping Card", Snackbar.LENGTH_LONG).show();
                    }
                }


            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.item_check_out_view, parent, false);

            CellViewHolder cellViewHolder = new CellViewHolder(v);
            setupClickableViews(cellViewHolder);
            return cellViewHolder;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            return new ProgressViewHolder(v);
        }
    }

    private void setupClickableViews(final CellViewHolder cellViewHolder) {
        cellViewHolder.removeFromShoppingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Snackbar snackbar = Snackbar
                        .make(recyclerView, "Click YES for DELETE or wait UNDU", Snackbar.LENGTH_LONG);

                snackbar.setAction("YES", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int adapterPosition = cellViewHolder.getAdapterPosition();
                        products.remove(adapterPosition);
                        notifyItemRangeRemoved(adapterPosition, 1);
                    }
                });

                snackbar.setActionTextColor(Color.BLUE);
                snackbar.getView().setBackgroundColor(Color.RED);
                snackbar.show();


            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CellViewHolder) {
            ((CellViewHolder) holder).bindView(products.get(position), context);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    @Override
    public int getItemViewType(int position) {
        return products.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Product product = products.get(position);
        ProductDetailActivity.startProductDetailActivity(context, product);
    }

    public static class CellViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.removeFromShoppingCard)
        ImageView removeFromShoppingCard;
        @Bind(R.id.picture)
        ImageView picture;
        @BindDimen(R.dimen.global_menu_avatar_size)
        int avatarSize;


        public CellViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(Product product, Context context) {

            Picasso.with(context)
                    .load(Constants.Rest.BASE_URL + product.getPicture())
                    .placeholder(R.drawable.img_circle_placeholder)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new RoundedTransformation())
                    .into(picture);

            name.setText(product.getName());
            // TODO change string format...
            price.setText(String.valueOf(product.getPrice()) + " ₺");
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}
