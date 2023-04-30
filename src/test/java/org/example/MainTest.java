package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class MainTest {

    private static ArrayList<String> fsm1;
    private static ArrayList<String> fsm2;

    @BeforeAll
    public static void setUpClass() {
        fsm1 = new ArrayList<>();
        fsm1.add("2");
        fsm1.add("2");
        fsm1.add("0");
        fsm1.add("1 1");
        fsm1.add("0 a 1");
        fsm1.add("0 b 1");
        fsm1.add("1 a 1");
        fsm1.add("1 b 1");

        fsm2 = new ArrayList<>();
        fsm2.add("3");
        fsm2.add("2");
        fsm2.add("0");
        fsm2.add("1 1");
        fsm2.add("0 a 1");
        fsm2.add("0 b 1");
        fsm2.add("1 a 1");
        fsm2.add("1 c 1");
    }

    @BeforeEach
    public void printTestName(TestInfo testInfo) {
        System.out.println("Running test: " + testInfo.getDisplayName());
    }

    @Test
    public void testAlphabetGetter() {
        ArrayList<Character> expected1 = new ArrayList<>(Arrays.asList('a', 'b'));
        ArrayList<Character> result1 = Main.alphabetGetter(fsm1);

        assertIterableEquals(expected1, result1);

        ArrayList<Character> expected2 = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        ArrayList<Character> result2 = Main.alphabetGetter(fsm2);

        assertIterableEquals(expected2, result2);
    }

    @Test
    public void testGetAllWords() {
        ArrayList<String> expected1 = new ArrayList<>(Arrays.asList("aa", "ab", "ba", "bb"));
        ArrayList<Character> alphabet1 = new ArrayList<>(Arrays.asList('a', 'b'));
        ArrayList<String> result1 = new ArrayList<>();
        Main.getAllWords(result1, alphabet1, "", 2);
        assertThat(result1, containsInAnyOrder(expected1.toArray()));

        ArrayList<String> expected2 = new ArrayList<>(Arrays.asList("aa", "ab", "ac", "ba", "bb", "bc", "ca", "cb", "cc"));
        ArrayList<Character> alphabet2 = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        ArrayList<String> result2 = new ArrayList<>();
        Main.getAllWords(result2, alphabet2, "", 2);
        assertThat(result2, containsInAnyOrder(expected2.toArray()));
    }

    @Test
    public void testStatesGetter() {
        ArrayList<Character> expected1 = new ArrayList<>(Arrays.asList('0', '1'));
        ArrayList<Character> result1 = Main.statesGetter(fsm1);
        assertEquals(expected1, result1, "Failed for FSM1");

        ArrayList<Character> expected2 = new ArrayList<>(Arrays.asList('0', '1'));
        ArrayList<Character> result2 = Main.statesGetter(fsm2);
        assertEquals(expected2, result2, "Failed for FSM2");
    }

    @Test
    public void testFinalStatesGetter() {
        ArrayList<Character> expected1 = new ArrayList<>(Arrays.asList('1'));
        ArrayList<Character> result1 = Main.finalStatesGetter(fsm1);

        assertThat(result1, hasSize(1));
        assertThat(result1, containsInAnyOrder(expected1.toArray()));

        ArrayList<Character> expected2 = new ArrayList<>(Arrays.asList('1'));
        ArrayList<Character> result2 = Main.finalStatesGetter(fsm2);

        assertThat(result2, hasSize(1));
        assertThat(result2, containsInAnyOrder(expected2.toArray()));
    }

    @Test
    public void testCheckerFSM() {
        boolean result1 = Main.checkerFSM(fsm1, 2);
        assertTrue(result1);
        boolean result2 = Main.checkerFSM(fsm2, 2);
        assertThat(result2, equalTo(false));
    }

    @Test
    public void testCheckerFSMException() {
        assertThrows(StackOverflowError.class, () -> Main.checkerFSM(fsm1, 0));
        assertThrows(StackOverflowError.class, () -> Main.checkerFSM(fsm2, 0));
    }

    @Test
    public void testMainMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        ByteArrayInputStream mockIn = new ByteArrayInputStream("2\n".getBytes());
        System.setIn(mockIn);

        String[] args = {};
        Main.main(args);

        String consoleOutput = outputStream.toString();
        assertThat(consoleOutput, containsString("Setted alphabet: [a, b, c]"));
        assertThat(consoleOutput, containsString("Enter number for words lenght: "));
        assertThat(consoleOutput, containsString("All possible words: [aa, ab, ac, ba, bb, bc, ca, cb, cc]"));
        assertThat(consoleOutput, anyOf(containsString("Setted FSM can read all words out of alphabet."),
                containsString("Setted FSM can NOT read all words out of alphabet.")));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void testCheckerFSMWithParameterized(int number) {
        boolean result = Main.checkerFSM(fsm1, number);
        assertTrue(result);
    }
}
