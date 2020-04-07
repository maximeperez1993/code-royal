package fr.jafar.brain.micro;

import fr.jafar.info.Faction;
import fr.jafar.info.Manager;
import fr.jafar.structure.Position;
import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;
import fr.jafar.structure.unit.UnitType;
import fr.jafar.util.MapInfos;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EscaperTest {

    private static final Position p1 = new Position(0, 0);
    private static final Position p2 = new Position(0, 100);
    private static final Position p3 = new Position(0, 200);
    private static final Position p4 = new Position(0, 300);
    private static final Position p5 = new Position(0, 400);
    private static final List<Position> positions = Arrays.asList(p1, p2, p3, p4, p5);

    /**
     * J'ai fais un schéma ici : https://www.geogebra.org/classic/huwjnvrj
     */
    @Test
    public void behindSite() {
        // Given
        Unit danger = buildKnight(new Position(100, 100));
        Site site = new Site(0, new Position(200, 200), 60);

        Manager manager = mockManager(Arrays.asList(danger));
        Escaper escaper = new Escaper(manager);

        // When
        Position result = escaper.behindSite(site, danger);

        // Then
        assertEquals(new Position(259, 267), result);
    }

    @Test
    public void shouldGetFarthestPositionWithOneUnitAndLinearPath() {
        // Given
        List<Unit> hisSoldiers = Arrays.asList(buildKnight(new Position(0, 1)));

        Manager manager = mockManager(hisSoldiers);
        Escaper escaper = new Escaper(manager);

        // When
        Position result = escaper.getFarthestPosition(positions);

        // Then
        assertEquals(p5, result);
    }

    @Test
    public void shouldGetFarthestPositionWithOneUnitAndCardinals() {
        // Given
        List<Unit> hisSoldiers = Arrays.asList(buildKnight(MapInfos.RIGHT));

        Manager manager = mockManager(hisSoldiers);
        Escaper escaper = new Escaper(manager);

        // When
        Position result = escaper.getFarthestPosition(MapInfos.CARDINALS);

        // Then
        assertEquals(MapInfos.BOTTOM_LEFT, result);
    }


    @Test
    public void shouldGetFarthestPositionWithTwoUnitAndCardinals() {
        // Given
        List<Unit> hisSoldiers = Arrays.asList(
            buildKnight(MapInfos.RIGHT),
            buildKnight(MapInfos.BOTTOM)
        );

        Manager manager = mockManager(hisSoldiers);
        Escaper escaper = new Escaper(manager);

        // When
        Position result = escaper.getFarthestPosition(MapInfos.CARDINALS);

        // Then
        assertEquals(MapInfos.TOP_LEFT, result);
    }

    private Manager mockManager(List<Unit> hisKnights) {
        Faction his = mock(Faction.class);
        when(his.knights()).thenAnswer(i -> hisKnights.stream());
        Manager manager = mock(Manager.class);
        when(manager.his()).thenReturn(his);
        return manager;
    }

    private Unit buildKnight(Position position) {
        return new Unit(position, Team.ENEMY, UnitType.KNIGHT, 100);
    }

}