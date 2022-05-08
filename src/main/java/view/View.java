package view;

import controller.Auth;
import model.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View {

    private Auth auth;

    public View() {
    }

    public View(Auth auth) {
        this.auth = auth;
    }

    public void promptForAuthorization(){
        System.out.println("use this link to request the access code:");
        System.out.println((auth.uriGranted ? auth.AUTH_URI : auth.AUTH_SPOTIFY_URI) +"/authorize"+"?client_id="+ auth.CLIENT_ID +"&redirect_uri="+ auth.REDIRECT_URI +"&response_type=code");
    }

    public void showAccessToken(){
        System.out.println(auth.accessToken);
    }

    public Map<Integer, List<Result>> pepareResults(List<Result> resultList){
       Map<Integer, List<Result>> resultPages = new HashMap<>();
       int pageNumber = 0;
       for (int i = 0; i < resultList.size(); i += 5){
           pageNumber += 1;
           resultPages.put( pageNumber , resultList.subList(i, Math.min(i + 5, resultList.size())));
       }
       return resultPages;
    }


    public void showResults(int pageNo, Map<Integer, List<Result>> resultMap) {
        if (pageNo > resultMap.keySet().size() || pageNo < 1) {
            System.out.println("No more pages.");
        } else {
            List<Result> results = resultMap.get(pageNo);
            for (Result result : results) {
                System.out.println(result);
            }
            System.out.println("---PAGE " + pageNo + " OF " + resultMap.keySet().size() + "---");
        }
    }
}
