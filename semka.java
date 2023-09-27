import java.util.Scanner;

public class semka {
    public static float fl_num(){
        Scanner s = new Scanner(System.in);
        System.out.println("Введите дроюное число: ");
        try {
            float input_us = s.nextFloat();
            return input_us;
        } catch (Exception InputMismatchException) {
            return -1;
        }
        
    }
 
    public static void main(String[] args) {
        float res = fl_num();
        if (res == -1) {
            fl_num();
        }else{
            System.out.println(res);
        }
        
    }
}
