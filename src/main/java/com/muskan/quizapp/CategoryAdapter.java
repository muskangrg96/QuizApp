package com.muskan.quizapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Category category;
    private Context context;
    private List<Category> categoryList;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategory;


        public ViewHolder(View view) {
            super(view);

            tvCategory = (TextView) view.findViewById(R.id.tvCategory);


            tvCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // product = productListList.get(getAdapterPosition());
                    Intent i = new Intent(context, QuizActivity.class);

                    // i.putExtra("productid", product.getId());
                    //  i.putExtra("name", product.getName());
                    context.startActivity(i);
                }
            });
        }


    }

    public CategoryAdapter(Context mContext, List<Category> categoryList) {
        this.context = mContext;
        this.categoryList = categoryList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);


        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        category = categoryList.get(position);
        holder.tvCategory.setText(category.getCatName());


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}