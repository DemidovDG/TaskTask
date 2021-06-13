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

import com.example.taskdrom.networktools.api.GitRequest;
import com.example.taskdrom.networktools.model.GitRepo;
import com.example.taskdrom.search.ExampleAdapter;
import com.example.taskdrom.search.ExampleItem;
import com.example.taskdrom.search.newpack.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    private List<GitRepo> listRepo;

    private ExampleAdapter adapter;
    private List<ExampleItem> exampleList;
    private String saveSearch = "";
    private int page = 1;

    private Thread thread = new Thread();

    private HashMap<String, String> params;

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
                if(exampleList.size() < 10) {
                    return;
                }

                exampleList.add(null);
                adapter.notifyItemInserted(exampleList.size()-1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ++page;

                        exampleList.remove(exampleList.size() - 1);
                        adapter.notifyItemRemoved(exampleList.size());

                        String filterPattern = saveSearch.toLowerCase().trim();
                        params = new HashMap<>();
                        params.put(GitRequest.PARAM_QUERY, filterPattern + " in:name");
                        params.put(GitRequest.PARAM_PER_PAGE, "10");
                        params.put(GitRequest.PARAM_PAGE, String.valueOf(page));

                        GitRequest request = new GitRequest();
                        String respString = request.get(GitRequest.SEARCH_REPO, params);
                        List<GitRepo> list = request.repoFromJSON(respString);
                        if(list != null && !list.isEmpty())
                            for (GitRepo repo : list) {
                                exampleList.add(new ExampleItem(repo.getAvatar(), repo.getOwner() + "/" + repo.getName(), repo.getDesc()));
                            }

                        //добавляю еще в список к тем
                        listRepo.addAll(list);
                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();

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
                if(newText == null || newText.length() < 3)
                    return false;
                if(thread.isAlive()) {
                    saveSearch = newText;
                    return false;
                }
                page = 1;
                saveSearch = newText;

                thread = new Thread(() -> {
                    String anotherSearch;
                    do {
                        anotherSearch = saveSearch;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                exampleList.clear();
                                exampleList.add(new ExampleItem(null, "waiting...", "waiting..."));
                                adapter.notifyDataSetChanged();

                            }
                        });


                        params = new HashMap<>();
                        params.put(GitRequest.PARAM_QUERY, anotherSearch + " in:name");
                        params.put(GitRequest.PARAM_PER_PAGE, "10");
                        params.put(GitRequest.PARAM_PAGE, "1");

                        GitRequest request = new GitRequest();
                        String respString = request.get(GitRequest.SEARCH_REPO, params);
                        listRepo = request.repoFromJSON(respString);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                exampleList.clear();

                                if(listRepo != null && !listRepo.isEmpty())
                                for (GitRepo repo : listRepo) {
                                    exampleList.add(new ExampleItem(repo.getAvatar(), repo.getOwner() + "/" + repo.getName(), repo.getDesc()));
                                }

                                adapter.notifyDataSetChanged();
                                adapter.setLoaded();
                            }
                        });


                    } while (!saveSearch.equals(anotherSearch));
                });

                thread.start();

                return false;
            }
        });


        return true;
    }

    public List<GitRepo> getListRepo() {
        return listRepo;
    }
}