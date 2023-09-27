import java.util.Scanner;

public class semka4 {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String userInput = s.nextLine();
        if (userInput == ""){
            throw new RuntimeException();
        }
        else{
            System.out.println(userInput);
        }
    }
    
}
