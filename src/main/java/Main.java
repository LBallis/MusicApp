import controller.ApiHelper;
import controller.Auth;
import model.Category;
import model.Result;
import view.Menu;
import view.View;

import java.io.IOException;
import java.util.*;


public class Main {

    static Menu menu = new Menu();

    public static void main(String[] args) {
        menu.initilize(args);
        menu.mainMenu();
    }

//    private static void executeCommand(String command) throws IOException, InterruptedException {
//        switch (command.toUpperCase(Locale.ROOT)){
//            case "AUTH":
//                auth.init();
//                isAuthorized = true;
//                if(auth.getAccessToken()) {
//                    System.out.println("Success!");
//                }
//                break;
//            case "NEW":
//                if (isAuthorized) {
//                    api.getNewReleases();
//                }else{
//                    System.out.println("Please, provide access for application.");
//                }
//                break;
//            case "FEATURED":
//                if (isAuthorized) {
//                    api.getFeatured();
//                }else {
//                    System.out.println("Please, provide access for application.");
//                }
//                break;
//            case "CATEGORIES":
//                if (isAuthorized) {
//                   results = api.getCategories();
//                }else{
//                    System.out.println("Please, provide access for application.");
//                }
//                break;
//            case "PLAYLISTS":
//                if (isAuthorized) {
//                    api.getPlaylist(playlistName);
//                }else {
//                    System.out.println("Please, provide access for application.");
//                }
//                break;
//            case "PREV":
//            case "NEXT":
//            case "EXIT":
//                exit = true;
//                break;
//        }
//    }



}
