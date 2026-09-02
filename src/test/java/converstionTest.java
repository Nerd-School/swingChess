void main() {
    String input = "a2";

    // 1. Convert 'e' to 5 (a=1, b=2, c=3, d=4, e=5...)
    int column = input.charAt(0) - 'a';

    // 2. Convert '4' to int 4
    int row = Character.getNumericValue(input.charAt(1));

    row = 8 - row;

    // Output results
    System.out.println("Column: " + column); // Prints 5
    System.out.println("Row: " + row);       // Prints 4

}