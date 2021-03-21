package com.example.taskdrom.search;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskdrom.R;
import com.example.taskdrom.api.GitRequest;
import com.example.taskdrom.search.newpack.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<ExampleItem> exampleList;
    private List<ExampleItem> exampleListFull;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private Activity activity;
    private LinearLayoutManager linearLayoutManager;

    //Для загружающихся
    class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    //Для нормальных элементов
    class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1;
        TextView textView2;
        ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView1 = itemView.findViewById(R.id.text_view1);
            textView2 = itemView.findViewById(R.id.text_view2);
        }
    }

    //для ленивой загрузки
    public ExampleAdapter(List<ExampleItem> exampleList, RecyclerView recyclerView, Activity activity) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
        this.activity = activity;

        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return exampleList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public ExampleAdapter(List<ExampleItem> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(activity).inflate(R.layout.example_item, parent, false);
            return new ExampleViewHolder(v);
        } else if(viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ExampleViewHolder) {
            ExampleViewHolder exampleViewHolder = (ExampleViewHolder) holder;
            ExampleItem currentItem = exampleList.get(position);
            exampleViewHolder.imageView.setImageResource(currentItem.getImageResource());
            exampleViewHolder.textView1.setText(currentItem.getText1());
            exampleViewHolder.textView2.setText(currentItem.getText2());
        }
        else if(holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }
    @Override
    public int getItemCount() {
        return exampleList == null ? 0 : exampleList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    //Выгрузка всех репозиториев
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<ExampleItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                GitRequest request = new GitRequest();
                Map<String,String> map = request.findRepos(filterPattern, 1);
                for(Map.Entry<String, String> entry : map.entrySet()) {
                    filteredList.add(new ExampleItem(R.drawable.ic_launcher_background, entry.getKey(), entry.getValue()));
                }
            }


            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
