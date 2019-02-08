package htaka.com.truefalse;

/**
 * Created by admin on 9/4/16.
 */
public class HT_Item {
    public String username;
    public String userscore;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserscore() {
        return userscore;
    }

    public void setUserscore(String userscore) {
        this.userscore = userscore;
    }

    public HT_Item(String username, String userscore) {
        this.username = username;
        this.userscore = userscore;
    }

}
