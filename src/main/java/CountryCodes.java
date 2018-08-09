import java.util.HashMap;
import java.util.Scanner;

public class CountryCodes {

    public static void main(String[] args) {
        System.out.println("Enter 3 letter country code:");

        Scanner sc = new Scanner(System.in);

        HashMap<String, String> countryCodes = new HashMap<>();

        countryCodes.put("ZWE", "Zimbabwe");
        countryCodes.put("AFG", "Afganistan");
        countryCodes.put("ALB", "Albania");
        countryCodes.put("BMU", "Bermuda");
        countryCodes.put("CYM", "Cayman Islands");
        countryCodes.put("CXR", "Christmas Island");


        String threeLetterCode = sc.nextLine();

        while(!threeLetterCode.equals("EXT")){

            String val = countryCodes.get(threeLetterCode);

            if(val != null){
                System.out.println("The count with the code "+ threeLetterCode+ " is : " + val);
            }
            if(threeLetterCode.length()>3){
                System.out.println("I assume that you entered a country name and will search for it's code");
                if(countryCodes.values().contains(threeLetterCode)) {
                    String finalThreeLetterCode = threeLetterCode;
                    countryCodes.forEach((key, value) -> {
                        if(value.equals(finalThreeLetterCode)){
                            System.out.println("Country code of " + value + " is: " + key);
                        }
                    });
                } else {
                    System.out.println("We never heard about that country");
                }
            }

            System.out.println("Enter new code: ");
            threeLetterCode = sc.nextLine();
        }


    }
}
