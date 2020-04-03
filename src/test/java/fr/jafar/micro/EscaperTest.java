package fr.jafar.micro;

import fr.jafar.api.Position;
import fr.jafar.unit.Unit;
import fr.jafar.unit.UnitManager;
import fr.jafar.util.MapInfos;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class EscaperTest {

    private static final Position p1 = new Position(0,0);
    private static final Position p2 = new Position(0,10);
    private static final Position p3 = new Position(0,20);
    private static final Position p4 = new Position(0,30);
    private static final Position p5 = new Position(0,40);
    private static final List<Position> positions = Arrays.asList(p1,p2,p3,p4,p5);


    @Test
    public void shouldGetFarthestPositionWithOneUnitAndLinearPath() {
        // Given
        List<Unit> hisSoldiers = Arrays.asList(mockUnit(new Position(0,1)));

        Unit myQueen = mock(Unit.class);
        UnitManager unitManager = mock(UnitManager.class);
        when(unitManager.getHisSoldiers()).thenReturn(hisSoldiers);
        Escaper escaper = new Escaper(myQueen, unitManager);

        // When
        Position result = escaper.getFarthestPosition(positions);

        // Then
        assertEquals(p5, result);
    }

    @Test
    public void shouldGetFarthestPositionWithOneUnitAndCardinals() {
        // Given
        List<Unit> hisSoldiers = Arrays.asList(mockUnit(MapInfos.RIGHT));

        Unit myQueen = mock(Unit.class);
        UnitManager unitManager = mock(UnitManager.class);
        when(unitManager.getHisSoldiers()).thenReturn(hisSoldiers);
        Escaper escaper = new Escaper(myQueen, unitManager);

        // When
        Position result = escaper.getFarthestPosition(MapInfos.CARDINALS);

        // Then
        assertEquals(MapInfos.BOTTOM_LEFT, result);
    }

    @Test
    public void shouldGetFarthestPositionWithTwoUnitAndCardinals() {
        // Given
        List<Unit> hisSoldiers = Arrays.asList(
                mockUnit(MapInfos.RIGHT),
                mockUnit(MapInfos.BOTTOM)
        );

        Unit myQueen = mock(Unit.class);
        UnitManager unitManager = mock(UnitManager.class);
        when(unitManager.getHisSoldiers()).thenReturn(hisSoldiers);
        Escaper escaper = new Escaper(myQueen, unitManager);

        // When
        Position result = escaper.getFarthestPosition(MapInfos.CARDINALS);

        // Then
        assertEquals(MapInfos.TOP_LEFT, result);
    }

    private Unit mockUnit(Position position) {
        Unit unit = mock(Unit.class);
        when(unit.getPosition()).thenReturn(position);
        return unit;
    }
}