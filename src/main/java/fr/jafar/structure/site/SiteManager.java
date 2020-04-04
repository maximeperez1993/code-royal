package fr.jafar.structure.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SiteManager {

    private final List<Site> sites;
    private List<Site> neutralSites;

    private Optional<Site> touchedSite;
    private Site myStartSite;
    private Site hisStartSite;

    private List<Site> mySites;
    private List<Site> myReadySites;
    private List<Site> myBarracks;
    private List<Site> myReadyBarracks;
    private List<Site> myMines;

    private List<Site> hisSites;
	private List<Site> hisReadySites;
	private List<Site> hisTrainingSites;

	public SiteManager(List<Site> sites) {
		this.sites = sites;
		this.neutralSites = new ArrayList<>(sites);
	}

	public void setStartSites(Site myStartSite, Site hisStartSite) {
		this.myStartSite = myStartSite;
		this.hisStartSite = hisStartSite;
	}

	public void update(Scanner in) {
        int touchedSite = in.nextInt();
        this.sites.forEach(site -> site.update(in));
        this.touchedSite = this.sites.stream().filter(site -> site.getId() == touchedSite).findFirst();
        this.neutralSites = this.sites.stream().filter(Site::isNeutral).collect(Collectors.toList());

        this.mySites = this.sites.stream().filter(Site::isFriendly).collect(Collectors.toList());
        this.myBarracks = this.mySites.stream().filter(Site::isBarrack).collect(Collectors.toList());

        this.myReadySites = this.mySites.stream().filter(Site::isReady).collect(Collectors.toList());
        this.myReadyBarracks = this.myBarracks.stream().filter(Site::isReady).collect(Collectors.toList());
        this.myMines = this.mySites.stream().filter(Site::isMine).collect(Collectors.toList());

        this.hisSites = this.sites.stream().filter(Site::isNeutral).filter(Site::isEnemy).collect(Collectors.toList());
		this.hisReadySites = this.hisSites.stream().filter(Site::isReady).collect(Collectors.toList());
		this.hisTrainingSites = this.hisSites.stream().filter(Site::isTraining).collect(Collectors.toList());
    }

    public List<Site> getSites() {
        return sites;
    }

    public List<Site> getNeutralSites() {
        return neutralSites;
    }

    public Optional<Site> getTouchedSite() {
        return touchedSite;
    }

    public Site getMyStartSite() {
        return myStartSite;
    }

    public Site getHisStartSite() {
        return hisStartSite;
    }

    public List<Site> getMySites() {
		return mySites;
	}

	public List<Site> getMyReadySites() {
		return myReadySites;
	}

	public List<Site> getMyBarracks() {
		return myBarracks;
	}

	public List<Site> getMyReadyBarracks() {
		return myReadyBarracks;
	}

	public List<Site> getMyMines() {
		return myMines;
	}

	public List<Site> getHisSites() {
		return hisSites;
	}

	public List<Site> getHisReadySites() {
		return hisReadySites;
	}

	public List<Site> getHisTrainingSites() {
		return hisTrainingSites;
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
