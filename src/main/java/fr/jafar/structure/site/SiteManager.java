package fr.jafar.structure.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class SiteManager {

    private final List<Site> sites;
    private Optional<Site> touchedSite;

    public SiteManager(List<Site> sites) {
        this.sites = sites;
    }

    public void update(Scanner in) {
        int touchedSite = in.nextInt();
        this.sites.forEach(site -> site.update(in));
        this.touchedSite = this.sites.stream().filter(site -> site.getId() == touchedSite).findFirst();
    }

    public List<Site> getSites() {
        return sites;
    }

    public Optional<Site> getTouchedSite() {
        return touchedSite;
    }


    public static SiteManager read(Scanner in) {
        int numSites = in.nextInt();
        List<Site> sites = new ArrayList<>();
        while (numSites-- > 0) {
            sites.add(Site.read(in));
        }
        return new SiteManager(sites);
    }
}
