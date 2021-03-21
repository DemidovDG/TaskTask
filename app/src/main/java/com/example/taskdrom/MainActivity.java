package com.example.taskdrom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.example.taskdrom.api.GitRequest;
import com.example.taskdrom.search.ExampleAdapter;
import com.example.taskdrom.search.ExampleItem;
import com.example.taskdrom.search.newpack.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ExampleAdapter adapter;
    private List<ExampleItem> exampleList;
    private String saveSearch = "";
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillExampleList();
        setUpRecyclerView();

    }

    //Для тестового заполнения
    private void fillExampleList() {
        exampleList = new ArrayList<>();
//        exampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "One", "йцу"));
//        exampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Two", "фыв"));
//        exampleList.add(new ExampleItem(R.drawable.ic_launcher_background, "Three", "ячс"));

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //        adapter = new ExampleAdapter(exampleList);
        adapter = new ExampleAdapter(exampleList, recyclerView, this);

        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                exampleList.add(null);
                adapter.notifyItemInserted(exampleList.size()-1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exampleList.remove(exampleList.size() - 1);
                        adapter.notifyItemRemoved(exampleList.size());

                        String filterPattern = saveSearch.toLowerCase().trim();
                        GitRequest request = new GitRequest();
                        Map<String,String> map = request.findRepos(filterPattern, page);
                        for(Map.Entry<String, String> entry : map.entrySet()) {
                            exampleList.add(new ExampleItem(R.drawable.ic_launcher_background, entry.getKey(), entry.getValue()));
                        }

                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();

                        ++page;
                    }
                }, 5000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText == null || newText.equals(""))
                    return false;
                saveSearch = newText;
                page = 2;

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}