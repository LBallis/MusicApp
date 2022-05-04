import controller.ApiHelper;
import controller.Auth;

import java.io.IOException;
import java.util.*;


public class Main {

    static boolean isAuthorized = false;
    static boolean exit = false;
    static boolean authUriGranted = false;
    static boolean apiUriGranted = false;
    static String AUTH_URI;
    static String API_URI;
    static String playlistName = "";

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++){
            if (args[i].equals("-access")){
                authUriGranted = true;
                AUTH_URI = args[i+1];
            }
            if (args[i].equals("-resource")){
                apiUriGranted = true;
                API_URI = args[i+1];
            }
        }

        Scanner scanner = new Scanner(System.in);

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


    private static void executeCommand(String command) throws IOException, InterruptedException {
        switch (command.toUpperCase(Locale.ROOT)){
            case "AUTH":
                Auth.init();
                isAuthorized = true;
                if(Auth.getAccessToken()) {
                    System.out.println("Success!");
                }
                break;
            case "NEW":
                if (isAuthorized) {
                    ApiHelper.getNewReleases();
                }else{
                    System.out.println("Please, provide access for application.");
                }
                break;
            case "FEATURED":
                if (isAuthorized) {
                    ApiHelper.getFeatured();
                }else {
                    System.out.println("Please, provide access for application.");
                }
                break;
            case "CATEGORIES":
                if (isAuthorized) {
                    ApiHelper.getCategories();
                }else{
                    System.out.println("Please, provide access for application.");
                }
                break;
            case "PLAYLISTS":
                if (isAuthorized) {
                    ApiHelper.getPlaylist(playlistName);
                }else {
                    System.out.println("Please, provide access for application.");
                }
                break;
            case "EXIT":
                System.out.println("---GOODBYE!---");
                exit = true;
                break;
        }
    }

}
