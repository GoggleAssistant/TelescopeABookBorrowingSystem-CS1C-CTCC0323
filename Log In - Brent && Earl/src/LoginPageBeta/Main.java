package LoginPageBeta;

public class Main {
    public static void main(String[] args) {
        IDandPasswords idandPasswords = new IDandPasswords();
        new LoginPage(idandPasswords.getLoginInfo());
    }
}
