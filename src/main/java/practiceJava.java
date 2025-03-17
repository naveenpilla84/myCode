import java.security.Key;
import java.util.*;

public class practiceJava {
    public static void main(String[] args) {
        ArrayList<String> cars = new ArrayList<String>();
        LinkedList<String> ll = new LinkedList<String>();
        ll.addFirst("Volvo");
        ll.getFirst();
        cars.add("BMW");
        cars.add("Ford");
        cars.add("Mazda");
        cars.sort(Collections.reverseOrder());

        String value="my first java Program";
        String rev="";
        String[] values=value.split(" ");
        for (String v:values) {
            StringBuffer sb=new StringBuffer(v);
            rev=rev+sb.reverse().toString()+" ";
        }
        System.out.println(rev.trim());

        //Count number of repeated and not repeated char in String
        String name="Arasala Bhanu Mohana";
        Map<String, Integer> mp=new HashMap<>();
        Map<Character,Integer> mp1=new HashMap<>();
        //method1
        for (char c:name.toCharArray()) {
            mp1.put(c,mp1.getOrDefault(c,0)+1);
        }
        for (Map.Entry<Character,Integer>count:mp1.entrySet()) {
            if (count.getValue()>=1){
                System.out.println(count.getKey() + " -> " + count.getValue()+" Times");
            }
        }
        //method2
        String name1="Pilla Naga Kondarao";
        String[] splitedName=name1.toLowerCase().split("");
        for ( String a:splitedName) {
            mp.put(a,mp.getOrDefault(a,0)+1);
        }
        for (Map.Entry<String, Integer> entry:mp.entrySet()) {
                if(entry.getValue()>=1){
                    System.out.println(entry.getKey() + " -> " + entry.getValue()+" Times");
                }
        }

        //Method1
        int Number=92386434;
        int reverse=0;
        while (Number!=0){
            int n=Number%10;
            reverse=reverse*10+n;
            Number=Number/10;
        }
        System.out.println(reverse);

        //Method2
        String reversed = new StringBuilder(Integer.toString(1234523463)).reverse().toString();
        System.out.println(reversed);

        //Reverse a String Without Using Built-in Functions
        char[] arry= name.toCharArray();
        int left=0,right=arry.length-1;
        while(left<right){
            char temp=arry[left];
            arry[left]=arry[right];
            arry[right]=temp;
            right--;
            left++;
        }
        System.out.println(arry);


        int l=0,r=name1.length()-1;
        while (l<r){
            if(name1.charAt(l)!=name1.charAt(r)){

            }
            l++;
            r--;
        }















    }
}
