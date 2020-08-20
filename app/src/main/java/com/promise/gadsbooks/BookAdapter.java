package com.promise.gadsbooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.squareup.picasso.Picasso;

public class BookAdapter extends FirestorePagingAdapter<Book, BookAdapter.BookHolder> {
    private Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    /**
     * Construct a new FirestorePagingAdapter from the given {@link FirestorePagingOptions}.
     *
     * @param options
     */
    public BookAdapter(@NonNull FirestorePagingOptions<Book> options, SwipeRefreshLayout swipeRefreshLayout, Context context) {
        super(options);
        this.context = context;
      this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookHolder holder, int position, @NonNull Book model) {
        holder.topic.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        Picasso.get().load(model.getImage()).placeholder(R.drawable.appstore).error(R.drawable.appstore).fit().into(holder.imageView);
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new BookHolder(view);
    }

    public static class BookHolder extends RecyclerView.ViewHolder{
         TextView topic, description;
         ImageView imageView;
        public BookHolder(@NonNull View itemView) {
            super(itemView);
            topic = itemView.findViewById(R.id.topic);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        switch (state){
            case ERROR:
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                break;
            case LOADED:
            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                break;
            case LOADING_MORE:
            case LOADING_INITIAL:
                swipeRefreshLayout.setRefreshing(true);
                break;

        }
        super.onLoadingStateChanged(state);
    }
}
