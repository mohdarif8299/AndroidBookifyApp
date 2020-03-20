package com.books.bookify.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.books.bookify.R;
import com.books.bookify.details.DisplaySingleBook;
import com.books.bookify.models.BookAvailable;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private Context context;
    private List<BookAvailable> list;
    public BooksAdapter(List<BookAvailable> list,Context context){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.book_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapter.ViewHolder holder, int position) {
                              BookAvailable bookAvailable = list.get(position);
                              holder.title.setText(bookAvailable.getTitle());
                              holder.price.setText("\u20B9 "+" "+bookAvailable.getPrice());
                              holder.progressBar.setVisibility(View.VISIBLE);
                              holder.image.setOnClickListener(v ->
                                  context.startActivity(new Intent(context, DisplaySingleBook.class))
                              );
        Glide
                .with(context)
                .load(bookAvailable.getImage())
                .placeholder(R.drawable.add_books_images)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .fitCenter()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,price;
        ImageView image;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View view){
            super(view);
            title = view.findViewById(R.id.title);
            price = view.findViewById(R.id.price);
            image = view.findViewById(R.id.image);
            progressBar = view.findViewById(R.id.progress);
        }
    }
}
