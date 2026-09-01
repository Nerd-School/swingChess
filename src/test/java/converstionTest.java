void main() {
    String input = "g8";

    // 1. Convert 'e' to 5 (a=1, b=2, c=3, d=4, e=5...)
    int column = input.charAt(0) - 'a' + 1;

    // 2. Convert '4' to int 4
    int row = Character.getNumericValue(input.charAt(1));

    // Output results
    System.out.println("Column: " + column); // Prints 5
    System.out.println("Row: " + row);       // Prints 4

}