package fr.jafar.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SiteManager {

	private final List<Site> sites;
	private List<Site> neutralSites;

	private List<Site> mySites;
	private List<Site> myReadySites;

	private List<Site> hisSites;
	private List<Site> hisReadySites;
	private List<Site> hisTrainingSites;

	public SiteManager(List<Site> sites) {
		this.sites = sites;
		this.neutralSites = new ArrayList<>(sites);
	}

	public void update(Scanner in) {
		this.sites.forEach(site -> site.update(in));
		this.neutralSites = this.sites.stream().filter(Site::isNeutral).collect(Collectors.toList());

		this.mySites = this.sites.stream().filter(Site::isFriendly).collect(Collectors.toList());
		this.myReadySites = this.mySites.stream().filter(Site::isReady).collect(Collectors.toList());

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

	public List<Site> getMySites() {
		return mySites;
	}

	public List<Site> getMyReadySites() {
		return myReadySites;
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
