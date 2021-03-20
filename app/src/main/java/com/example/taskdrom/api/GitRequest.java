package com.example.taskdrom.api;

import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitRequest{
    RepositoryService service;
    Thread thread;
    Map<String, String> response = new HashMap<>();

    public GitRequest() {
        service = new RepositoryService();
    }

    //тест для поиска и выгрузки репозиториев
    public Map<String, String> findRepos(String name) {
        response = new HashMap<>();
        if(thread != null && thread.isAlive()) {
            thread.interrupt();
        }

        thread = new Thread(() -> {
//            Map<String, String> searchQuery = new HashMap<>();
//            searchQuery.put("name", name);
            try {
                List<SearchRepository> list = service.searchRepositories(name, 1);
                for(SearchRepository sr : list) {
                    response.put(sr.toString(), sr.getOwner());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }
}
