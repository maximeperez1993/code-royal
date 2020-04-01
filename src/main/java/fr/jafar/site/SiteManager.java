package fr.jafar.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SiteManager {

    private final List<Site> sites;

    public SiteManager(List<Site> sites) {
        this.sites = sites;
    }

    public void update(Scanner in) {
        this.sites.forEach(site -> site.update(in));
    }

    public List<Site> getSites() {
        return sites;
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
