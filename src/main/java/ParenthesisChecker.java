import java.util.Scanner;
import java.util.Stack;

public class ParenthesisChecker {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter expression with parenthesis: ");
        String exp = sc.next();
        Boolean isParenthesisBalanced = parenthesisChecker(exp);
        System.out.println(isParenthesisBalanced);
    }

    public static boolean parenthesisChecker(String str) {
        Stack<Character> stack = new Stack<>();
        int length = str.length();

        boolean isBalanced = true;
        for (int i = 0; i < length; i++) {
            char nextCharacter = str.charAt(i);
            switch (nextCharacter) {
                case '(':
                case '[':
                case '{':
                    stack.push(nextCharacter);
                    break;
                case ')':
                case ']':
                case '}':
                    if(stack.isEmpty()) {
                        isBalanced = true;
                    } else {
                        stack.pop();
                    }
                    break;
            }
        }
        if(!stack.isEmpty())
            isBalanced = false;
        return isBalanced;
    }
}
