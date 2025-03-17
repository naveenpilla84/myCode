import java.util.ArrayList;

public class practice {


    public static boolean paalindrome(String name) {

        int left=0,right=name.length()-1;
        while (left<right){
            if (name.charAt(left)!=name.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    public static void reverse(String name){
        int left=0,right=name.length()-1;
        String rev="";
        for (int i =right; i >=left ; i--) {
            rev=rev+name.charAt(i);
        }
        System.out.println(rev);
    }


    public static void main(String[] args) {
//        System.out.println(paalindrome("madam"));
        reverse("naveen");
    }

}

class FactorialRecursion {
    public static int factorial(int n) {
        if (n == 0){
            return 1;
        }else{
            return n * factorial(n - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(factorial(5));
    }
}

class FibonacciRecursion{
    public static int Fibonacci(int n){
        if (n<=1){
            return n;
        }else {
            return Fibonacci(n-1)+Fibonacci(n-2);
        }
    }
    public static void main(String[] args) {
        int n = 5;
        for (int i = 0; i < n; i++) {
            System.out.println(Fibonacci(i));
        }
    }

}
