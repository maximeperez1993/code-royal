package fr.jafar.util;

import fr.jafar.structure.Position;
import fr.jafar.structure.Team;
import fr.jafar.structure.site.Site;
import fr.jafar.structure.site.SiteState;
import fr.jafar.structure.site.StructureType;
import fr.jafar.structure.unit.Unit;
import fr.jafar.structure.unit.UnitType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FinderTest {

    @Test
    public void filterSafeFromEnemyTowerRange() {
        // Given
        Site site = new Site(0, new Position(100, 100), 60);
        List<Site> sites = Arrays.asList(site);
        Site enemyTower = new Site(0, new Position(200, 100), 60);
        enemyTower.setState(new SiteState(0, 0, StructureType.TOWER, Team.ENEMY, 10, 100));
        List<Site> enemyTowers = Arrays.asList(enemyTower);
        Unit myQueen = new Unit(new Position(0, 100), Team.FRIENDLY, UnitType.QUEEN, 100);

        // When
        Optional<Site> result = new Finder<>(sites).filterSafeFromEnemyTowerRange(enemyTowers, myQueen).getOptional();

        // Then
        Assert.assertEquals(site, result.get());
    }

    @Test
    public void filterSafeFromEnemyTowerRange2() {
        // Given
        Site site = new Site(0, new Position(100, 100), 30);
        List<Site> sites = Arrays.asList(site);
        Site enemyTower = new Site(0, new Position(200, 100), 60);
        enemyTower.setState(new SiteState(0, 0, StructureType.TOWER, Team.ENEMY, 10, 129));
        List<Site> enemyTowers = Arrays.asList(enemyTower);
        Unit myQueen = new Unit(new Position(0, 100), Team.FRIENDLY, UnitType.QUEEN, 100);

        // When
        Optional<Site> result = new Finder<>(sites).filterSafeFromEnemyTowerRange(enemyTowers, myQueen).getOptional();

        // Then
        Assert.assertEquals(site, result.get());
    }

    @Test
    public void filterSafeFromEnemyTowerRange3() {
        // Given
        Site site = new Site(0, new Position(100, 100), 30);
        List<Site> sites = Arrays.asList(site);
        Site enemyTower = new Site(0, new Position(200, 100), 60);
        enemyTower.setState(new SiteState(0, 0, StructureType.TOWER, Team.ENEMY, 10, 130));
        List<Site> enemyTowers = Arrays.asList(enemyTower);
        Unit myQueen = new Unit(new Position(0, 100), Team.FRIENDLY, UnitType.QUEEN, 100);

        // When
        Optional<Site> result = new Finder<>(sites).filterSafeFromEnemyTowerRange(enemyTowers, myQueen).getOptional();

        // Then
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void filterSafeFromEnemyTowerRangeShouldReturnFalse() {
        // Given
        Site site = new Site(0, new Position(100, 100), 60);
        List<Site> sites = Arrays.asList(site);
        Site enemyTower = new Site(0, new Position(200, 100), 60);
        enemyTower.setState(new SiteState(0, 0, StructureType.TOWER, Team.ENEMY, 10, 190));
        List<Site> enemyTowers = Arrays.asList(enemyTower);
        Unit myQueen = new Unit(new Position(0, 100), Team.FRIENDLY, UnitType.QUEEN, 100);

        // When
        Optional<Site> result = new Finder<>(sites).filterSafeFromEnemyTowerRange(enemyTowers, myQueen).getOptional();

        // Then
        Assert.assertFalse(result.isPresent());
    }
}