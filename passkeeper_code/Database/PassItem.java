package appdev.sapir.maor.passkeeper.Database;

/**
 * Created by User on 15/02/2016.
 */
public class PassItem {
    private long id;
    private String passName;
    private String userName;
    private String passValue;
    private String passUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassName() {
        return passName;
    }

    public void setPassName(String passName) {
        this.passName = passName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassValue() {
        return passValue;
    }

    public void setPassValue(String passValue) {
        this.passValue = passValue;
    }

    public String getPassUrl() {
        return passUrl;
    }

    public void setPassUrl(String passUrl) {
        this.passUrl = passUrl;
    }

    @Override
    public String toString() {
        return "PassItem{" +
                "id=" + id +
                ", passName='" + passName + '\'' +
                ", userName='" + userName + '\'' +
                ", passValue='" + passValue + '\'' +
                ", passUrl='" + passUrl + '\'' +
                '}';
    }
}

