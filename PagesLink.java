package main;

//this class is to define pages link
public class PagesLink {
    public String address;
    public int score_0, score_1, score_2, score_3;
    public int final_score;
    
    public PagesLink cpy(PagesLink new_pageslink) {
        PagesLink out = new PagesLink(); //create a new page link in case different links use the same one
        out.address = new_pageslink.address;
        out.score_0 = new_pageslink.score_0;
        out.score_1 = new_pageslink.score_1;
        out.score_2 = new_pageslink.score_2;
        out.score_3 = new_pageslink.score_3;
        out.final_score = new_pageslink.final_score;
        return out;
    }
}
