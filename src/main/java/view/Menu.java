package view;

import controller.ApiHelper;
import controller.Auth;
import model.Result;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Menu {

    boolean isAuthorized = false;
    boolean exit = false;

    String playlistName = "";
    Scanner scanner = new Scanner(System.in);
    Auth auth = new Auth();
    View view = new View(auth);
    ApiHelper api = new ApiHelper(auth);
    List<Result> results;
    Map<Integer, List<Result>> resultMap;
    int pageNumber = 0;

    public void mainMenu(){
        while (!exit) {
            String command = scanner.nextLine();
            if(command.contains("playlists")){
                playlistName = command.replaceAll("playlists", "").trim();
                command = "playlists";
            }
            try {
                executeCommand(command);

            }catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeCommand(String command) throws IOException, InterruptedException {
        switch (command.toUpperCase(Locale.ROOT)){
            case "AUTH":
                auth.init();
                isAuthorized = true;
                if(auth.getAccessToken()) {
                    System.out.println("Success!");
                }
                break;
            case "NEW":
                if (isAuthorized) {
                    results = api.getNewReleases();
                    resultMap = view.pepareResults(results);
                    pageNumber = 1;
                    view.showResults(pageNumber, resultMap);
                }else{
                    System.out.println("Please, provide access for application.");
                }
                break;
            case "FEATURED":
                if (isAuthorized) {
                    results = api.getFeatured();
                    resultMap = view.pepareResults(results);
                    pageNumber = 1;
                    view.showResults(pageNumber, resultMap);
                }else {
                    System.out.println("Please, provide access for application.");
                }
                break;
            case "CATEGORIES":
                if (isAuthorized) {
                    results = api.getCategories();
                    resultMap = view.pepareResults(results);
                    pageNumber = 1;
                    view.showResults(pageNumber, resultMap);
                }else{
                    System.out.println("Please, provide access for application.");
                }
                break;
            case "PLAYLISTS":
                if (isAuthorized) {
                    results = api.getPlaylist(playlistName);
                    resultMap = view.pepareResults(results);
                    pageNumber = 1;
                    view.showResults(pageNumber, resultMap);
                }else {
                    System.out.println("Please, provide access for application.");
                }
                break;
            case "PREV":
                if (pageNumber == resultMap.keySet().size() + 1){
                    pageNumber -= 2;
                }else if (pageNumber > 0) {
                    pageNumber -= 1;
                }
                view.showResults(pageNumber, resultMap);
                break;
            case "NEXT":
                if (pageNumber == 0){
                    pageNumber += 2;
                }else if (pageNumber <= resultMap.keySet().size()) {
                    pageNumber += 1;
                }
                view.showResults(pageNumber, resultMap);
                break;
            case "EXIT":
                exit = true;
                break;
        }
    }

    public void initilize(String[] args){
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-access")){
                auth.uriGranted = true;
                auth.AUTH_URI = args[i+1];
            }
            if (args[i].equals("-resource")){
                api.uriGranted = true;
                api.API_URI = args[i+1];
            }
        }
    }
}
