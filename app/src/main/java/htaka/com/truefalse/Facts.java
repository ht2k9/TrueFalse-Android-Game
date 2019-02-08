package htaka.com.truefalse;

/**
 * Created by admin on 9/11/16.
 */
public class Facts {
    public String fact1;
    public String fact2;

    public Facts(String fact1, String fact2, int answer, String marked1, String marked2) {
        this.fact1 = fact1;
        this.fact2 = fact2;
        this.answer = answer;
        this.marked1 = marked1;
        this.marked2 = marked2;
    }

    public int answer;
    public String marked1;
    public String marked2;


}
