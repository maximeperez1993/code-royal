package fr.jafar.api;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FinderTest {

    @Test
    public void shouldReturnClosestToQueen1() {
        // Given
        Position site1 = new Position(0, 10);
        Position site2 = new Position(0, 29);
        List<Position> sites = Arrays.asList(site1, site2);

        Position myQueen = new Position(0, 20);

        // When
        Position result = new Finder<>(sites).sortByClosestFrom(myQueen).get();

        // Then
        assertEquals(site2, result);
    }

    @Test
    public void shouldReturnClosestToQueen2() {
        // Given
        Position site1 = new Position(0, 12);
        Position site2 = new Position(0, 29);
        List<Position> sites = Arrays.asList(site1, site2);

        Position myQueen = new Position(0, 20);

        // When
        Position result = new Finder<>(sites).sortByClosestFrom(myQueen).get();

        // Then
        assertEquals(site1, result);
    }

}