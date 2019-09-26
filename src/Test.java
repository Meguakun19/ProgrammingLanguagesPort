import java.io.*;
import java.util.*;

public class Test
{
    private static final int LETTER=0;
    private static final int DIGIT=1;
    private static final int UNKNOWN=99;
    private static final int EOF=-1;
    private static final int INT_LIT=10;
    private static final int IDENT=11;
    private static final int ASSIGN_OP=20;
    private static final int ADD_OP=21;
    private static final int SUB_OP=22;
    private static final int MULT_OP=23;
    private static final int DIV_OP=24;
    private static final int LEFT_PAREN=25;
    private static final int RIGHT_PAREN=26;


    private static int characterClass;
    private static char myLexeme[];
    private static char mynextCharacter;
    private static int myLexicalLen;
    private static int myToken;
    private static int nextmyToken;
    private static File file;
    private static FileInputStream fisss;
    public static int lookup(char ch)
    {
        switch (ch)
        {
            case '(':
                addChar();
                nextmyToken = LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextmyToken = RIGHT_PAREN;
                break;
            case '+':
                addChar();
                nextmyToken = ADD_OP;
                break;
            case '-':
                addChar();
                nextmyToken = SUB_OP;
                break;
            case '*':
                addChar();
                nextmyToken = MULT_OP;
                break;
            case '/':
                addChar();
                nextmyToken = DIV_OP;
                break;
            default:
                addChar();
                nextmyToken = EOF;
                break;
        }
        return nextmyToken;
    }
    public static void addChar()
    {
        if (myLexicalLen <= 98)
        {
            myLexeme[myLexicalLen++] = mynextCharacter;
            // myLexeme[myLexicalLen] = 0;
        }
        else
            System.out.println("Error -myLexeme is too long\n");
    }
    public static void getChar()
    {
        try
        {
            if(fisss.available()>0)
            {
                mynextCharacter=(char)fisss.read();
                if(Character.isLetter(mynextCharacter))
                    characterClass=LETTER;
                else if(Character.isDigit(mynextCharacter))
                    characterClass=DIGIT;
                else
                    characterClass=UNKNOWN;
            }
            else
                characterClass=EOF;
            //   System.out.println(mynextCharacter+" "+characterClass);

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void getNonBlank()
    {
        while(Character.isSpaceChar(mynextCharacter))
            getChar();

    }
    public static int lex()
    {
        myLexicalLen = 0;
        getNonBlank();
        switch (characterClass)
        {
            /* parse identifiers */
            case LETTER:
                addChar();
                getChar();
                while (characterClass == LETTER || characterClass == DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextmyToken = IDENT;
                break;
            /* parse integer literals and integers */
            case DIGIT:
                addChar();
                getChar();
                while(characterClass == DIGIT)
                {
                    addChar();
                    getChar();
                }
                nextmyToken = INT_LIT;
                break;
            /* parentheses and operators */
            case UNKNOWN:
                lookup(mynextCharacter);
                getChar();
                break;
            /* EOF */
            case EOF:
                nextmyToken = EOF;
                myLexeme[0] = 'E';
                myLexeme[1] = 'O';
                myLexeme[2] = 'F';
                myLexeme[3] = 0;
                break;

        } /* end of switch */
        System.out.print("Next myToken is :"+nextmyToken+" Next myLexeme is :");
        for(int i=0;i<myLexicalLen;i++)
            System.out.print(myLexeme[i]);
        System.out.println();
        return nextmyToken;
    }
    public static void main(String args[])
    {
        myLexicalLen=0;
        myLexeme=new char[100];
        for(int i=0;i<100;i++)
            myLexeme[i]='0';
        file = new File("input1.txt");
        if (!file.exists())
        {
            System.out.println( "input1.txt does not exist.");
            return;
        }
        if (!(file.isFile() && file.canRead()))
        {
            System.out.println(file.getName() + " cannot be read from.");
            return;
        }
        try
        {
            fisss = new FileInputStream(file);
            char current;
            while (fisss.available() > 0)
            {
                getChar();
                //   System.out.println(mynextCharacter+" "+characterClass);
                lex();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
