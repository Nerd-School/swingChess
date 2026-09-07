import java.util.HashMap;
void main() {
    HashMap<Integer, String> numberConversionMap =  new HashMap<>();

    numberConversionMap.put(0, "a");
    numberConversionMap.put(1, "b");
    numberConversionMap.put(2, "c");
    numberConversionMap.put(3, "d");
    numberConversionMap.put(4, "e");
    numberConversionMap.put(5, "f");
    numberConversionMap.put(6, "g");
    numberConversionMap.put(7, "h");

    int[] pos = {7, 7};
    String row;
    String column;

    row = (pos[0]+1) + "";
    column = numberConversionMap.get(pos[1]);
    System.out.println(column + row);

    /*
    String input = "a2";

    // 1. Convert 'e' to 5 (a=1, b=2, c=3, d=4, e=5...)
    int column = input.charAt(0) - 'a';

    // 2. Convert '4' to int 4
    int row = Character.getNumericValue(input.charAt(1));

    row = 8 - row;

    // Output results
    System.out.println("Column: " + column); // Prints 5
    System.out.println("Row: " + row);       // Prints 4

     */

}