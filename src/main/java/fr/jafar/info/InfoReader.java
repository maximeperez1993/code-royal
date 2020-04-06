package fr.jafar.info;

import fr.jafar.structure.site.Site;
import fr.jafar.structure.unit.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class InfoReader {

    private InfoReader() {
    }

    public static List<Site> readSites(Scanner in) {
        int numSites = in.nextInt();
        List<Site> sites = new ArrayList<>();
        while (numSites-- > 0) {
            sites.add(Site.read(in));
        }
        return sites;
    }

    public static List<Unit> readUnits(Scanner in) {
        int numSites = in.nextInt();
        List<Unit> units = new ArrayList<>();
        while (numSites-- > 0) {
            units.add(Unit.read(in));
        }
        return units;
    }

    public static Site readTouchedSite(Scanner in, List<Site> sites) {
        int touchedSite = in.nextInt();
        return sites.stream().filter(site -> site.getId() == touchedSite).findFirst().orElse(null);
    }

    public static void readSitesStates(Scanner in, List<Site> sites) {
        sites.forEach(site -> site.update(in));
    }
}
