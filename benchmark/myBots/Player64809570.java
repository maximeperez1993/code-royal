import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.Objects;
import java.util.List;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.Comparator;


class Player {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        Scanner in = new Scanner(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        Task solver = new Task();
        solver.solve(1, in, out);
        out.close();
    }

    static class Task {
        public void solve(int testNumber, Scanner in, PrintWriter out) {
            Engine engine = new Engine(in);

            while (true) {
                engine.update(in);
            }
        }

    }

    static class BuildLocationManager {
        private final Manager manager;
        private final SiteManager siteManager;

        public BuildLocationManager(Manager manager) {
            this.manager = manager;
            this.siteManager = manager.getSiteManager();
        }

        public Site get(BuildRequest.Option option) {
            if (option == BuildRequest.Option.FRONTIER) {
                return getAggressive();
            }
            if (option == BuildRequest.Option.CLOSEST) {
                return getClosest();
            }
            return getPassive();
        }

        private Site getAggressive() {
            return new Finder<>(siteManager.getNeutralSites()).sortByClosestFrom(MapInfos.MIDDLE).get();
        }

        private Site getClosest() {
            return new Finder<>(siteManager.getNeutralSites()).sortByClosestFrom(manager.getUnitManager().getMyQueen()).get();
        }

        private Site getPassive() {
            return new Finder<>(siteManager.getNeutralSites()).sortByFarthestFrom(siteManager.getHisStartSite()).get();
        }

    }

    static class Unit implements Positionable {
        private final Position position;
        private final Team team;
        private final UnitType unitType;
        private final int health;

        public Unit(Position position, Team team, UnitType unitType, int health) {
            this.position = position;
            this.team = team;
            this.unitType = unitType;
            this.health = health;
        }

        public Position getPosition() {
            return position;
        }

        public boolean isMyQueen() {
            return unitType == UnitType.QUEEN && team == Team.FRIENDLY;
        }

        public boolean isHisQueen() {
            return unitType == UnitType.QUEEN && team == Team.ENEMY;
        }

        public boolean isMySoldier() {
            return unitType != UnitType.QUEEN && team == Team.FRIENDLY;
        }

        public boolean isHisSoldier() {
            return unitType != UnitType.QUEEN && team == Team.ENEMY;
        }

        public static Unit read(Scanner in) {
            return new Unit(Position.read(in), Team.read(in), UnitType.read(in), in.nextInt());
        }

    }

    static class Escaper {
        private final Manager manager;

        public Escaper(Manager manager) {
            this.manager = manager;
        }

        public Position getEscapePosition() {
            return this.getFarthestPosition(MapInfos.CARDINALS);
        }

        public Position getFarthestPosition(List<Position> positions) {
            return positions.stream()
                    .max((p1, p2) -> (int) (sumDistance(p1) - sumDistance(p2)))
                    .orElseThrow(IllegalStateException::new);
        }

        private double sumDistance(Position position) {
            return this.manager.getUnitManager().getHisSoldiers().stream()
                    .mapToDouble(unit -> position.getDistance(unit.getPosition()))
                    .sum();
        }

    }

    static class Position implements Positionable {
        private final int x;
        private final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public double getDistance(Position p) {
            return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public static Position read(Scanner scanner) {
            return new Position(scanner.nextInt(), scanner.nextInt());
        }

        public Position getPosition() {
            return this;
        }

        public String toString() {
            return "(" + x + " " + y + ")";
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y;
        }

        public int hashCode() {
            return Objects.hash(x, y);
        }

    }

    static class BuildRequest {
        private StructureType structureType;
        private UnitType unitType;
        private BuildRequest.Option option;

        public BuildRequest() {
        }

        public BuildRequest a(StructureType structureType) {
            this.structureType = structureType;
            return this;
        }

        public BuildRequest of(UnitType type) {
            this.unitType = type;
            return this;
        }

        public BuildRequest in(BuildRequest.Option option) {
            this.option = option;
            return this;
        }

        public void check() {
            if (this.option == null) {
                throw new IllegalStateException("Can't build without option");
            }
            if (this.structureType == StructureType.NO_STRUCTURE) {
                throw new IllegalStateException("Can't build NO_STRUCTURE");
            }
            if (this.structureType == StructureType.BARRACKS && this.unitType == null) {
                throw new IllegalStateException("Can't make a BARRACKS with no UnitType");
            }
            if (this.structureType == StructureType.TOWER && this.unitType != null) {
                throw new IllegalStateException("Can't make a TOWER with UnitType");
            }
        }

        public StructureType getStructureType() {
            return structureType;
        }

        public UnitType getUnitType() {
            return unitType;
        }

        public BuildRequest.Option getOption() {
            return option;
        }

        public String toString() {
            return "BuildRequest{" +
                    "structureType=" + structureType +
                    ", unitType=" + unitType +
                    ", option=" + option +
                    '}';
        }

        enum Option {
            FRONTIER,
            SAFE_BASE,
            CLOSEST,
            ;
        }

    }

    static class Site implements Positionable {
        private final int id;
        private final Position position;
        private final int radius;
        private SiteState state;

        private Site(int id, Position position, int radius) {
            this.id = id;
            this.position = position;
            this.radius = radius;
        }

        public boolean isNeutral() {
            return this.state.getTeam() == Team.NEUTRAL;
        }

        public boolean isFriendly() {
            return this.state.getTeam() == Team.FRIENDLY;
        }

        public boolean isEnemy() {
            return this.state.getTeam() == Team.ENEMY;
        }

        public boolean isReady() {
            return this.state.isReady();
        }

        public boolean isTraining() {
            return this.state.getRemainTurn() > 0;
        }

        public boolean isBarrack() {
            return this.state.getStructureType() == StructureType.BARRACKS;
        }

        public boolean isMine() {
            return this.state.getStructureType() == StructureType.MINE;
        }

        public boolean hasNoRemainingGold() {
            return this.state.getGold() <= 0;
        }

        public boolean isTower() {
            return this.state.getStructureType() == StructureType.TOWER;
        }

        public boolean isEnemyTower() {
            return isTower() && isEnemy();
        }

        public boolean isMineUpgradable() {
            return isMine() && state.getRemainTurn() < state.getMaxMineSize();
        }

        public boolean isTowerUpgradable() {
            return isTower() && state.getRemainTurn() <= 500;
        }

        public int getId() {
            return this.id;
        }

        public Position getPosition() {
            return this.position;
        }

        public void update(Scanner in) {
            if (in.nextInt() != id) {
                throw new IllegalStateException("id of site not ordered !!");
            }
            this.state = SiteState.read(in);
        }

        public static Site read(Scanner in) {
            return new Site(in.nextInt(), Position.read(in), in.nextInt());
        }

        public String toString() {
            return "Site{" +
                    "id=" + id +
                    ", position=" + position +
                    ", radius=" + radius +
                    ", state=" + state +
                    '}';
        }

    }

    static class UnitManager {
        private final List<Unit> units;
        private final Unit myQueen;
        private final Unit hisQueen;
        private final List<Unit> mySoldiers;
        private final List<Unit> hisSoldiers;

        public UnitManager(List<Unit> units) {
            this.units = units;
            this.myQueen = units.stream().filter(Unit::isMyQueen).findFirst().orElseThrow(() -> new IllegalStateException("No Friendly Queen"));
            this.hisQueen = units.stream().filter(Unit::isHisQueen).findFirst().orElseThrow(() -> new IllegalStateException("No Enemy Queen"));

            this.mySoldiers = units.stream().filter(Unit::isMySoldier).collect(Collectors.toList());
            this.hisSoldiers = units.stream().filter(Unit::isHisSoldier).collect(Collectors.toList());
        }

        public Unit getMyQueen() {
            return myQueen;
        }

        public Unit getHisQueen() {
            return hisQueen;
        }

        public List<Unit> getHisSoldiers() {
            return hisSoldiers;
        }

        public static UnitManager read(Scanner in) {
            int numSites = in.nextInt();
            List<Unit> units = new ArrayList<>();
            while (numSites-- > 0) {
                units.add(Unit.read(in));
            }
            return new UnitManager(units);
        }

    }

    static final class MapInfos {
        private static final int WIDTH = 1920;
        private static final int HEIGHT = 1000;
        public static final Position TOP = new Position(WIDTH / 2, 0);
        public static final Position TOP_RIGHT = new Position(WIDTH, 0);
        public static final Position RIGHT = new Position(WIDTH, HEIGHT / 2);
        public static final Position BOTTOM_RIGHT = new Position(WIDTH, HEIGHT);
        public static final Position BOTTOM = new Position(WIDTH / 2, HEIGHT);
        public static final Position BOTTOM_LEFT = new Position(0, HEIGHT);
        public static final Position LEFT = new Position(0, HEIGHT / 2);
        public static final Position TOP_LEFT = new Position(0, 0);
        public static final Position MIDDLE = new Position(WIDTH / 2, HEIGHT / 2);
        public static final List<Position> CARDINALS = Arrays.asList(TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT);

        private MapInfos() {
            throw new IllegalStateException("Can't instantiate MapInfos utils class");
        }

    }

    static class TrainManager {
        private final Manager manager;

        public TrainManager(Manager manager) {
            this.manager = manager;
        }

        public void train(int gold) {
            System.out.println(this.getTrainString(gold));
        }

        private String getTrainString(int gold) {
            if (manager.getSiteManager().getMySites().isEmpty()) {
                return "TRAIN";
            }
            List<Site> sitesToTrain = this.getSitesToTrain(gold);
            if (sitesToTrain.isEmpty()) {
                return "TRAIN";
            }
            return "TRAIN " + sitesToTrain.stream()
                    .map(Site::getId)
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));
        }

        private List<Site> getSitesToTrain(int gold) {
            int cost = 0;
            List<Site> sitesToTrain = new ArrayList<>();
            List<Site> myReadySites = manager.getSiteManager().getMyReadySites().stream()
                    .sorted(new PositionableComparator(this.manager.getUnitManager().getHisQueen()))
                    .collect(Collectors.toList());

            for (Site site : myReadySites) {
                if (cost + 80 <= gold) {
                    sitesToTrain.add(site);
                    cost += 80;
                }
            }
            return sitesToTrain;
        }

    }

    static enum UnitType implements EnumCG {
        QUEEN(-1),
        KNIGHT(0),
        ARCHER(1),
        GIANT(2),
        ;
        private final int code;

        UnitType(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static UnitType read(Scanner in) {
            return EnumCG.find(in.nextInt(), values());
        }

    }

    static class PositionComparator implements Comparator<Position> {
        private final Position origin;

        public PositionComparator(Position origin) {
            this.origin = origin;
        }

        public int compare(Position p1, Position p2) {
            return (int) (origin.getDistance(p1) - origin.getDistance(p2));
        }

    }

    static class Finder<T extends Positionable> {
        private Stream<T> stream;

        public Finder(List<T> positionables) {
            this.stream = positionables.stream();
        }

        public Finder<T> sortByFarthestFrom(Positionable origin) {
            this.stream = this.stream.sorted((p1, p2) -> (int) (origin.getDistance(p2) - origin.getDistance(p1)));
            return this;
        }

        public Finder<T> sortByClosestFrom(Positionable origin) {
            this.stream = this.stream.sorted((p1, p2) -> (int) (origin.getDistance(p1) - origin.getDistance(p2)));
            return this;
        }

        public Optional<T> getOptional() {
            return this.stream.findFirst();
        }

        public T get() {
            return this.getOptional().orElseThrow(IllegalStateException::new);
        }

    }

    static enum Team implements EnumCG {
        NEUTRAL(-1),
        FRIENDLY(0),
        ENEMY(1),
        ;
        private final int code;

        Team(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static Team read(Scanner in) {
            return EnumCG.find(in.nextInt(), values());
        }

    }

    static class BuildManager {
        private final BuildLocationManager buildLocationManager;
        private final List<Site> sitesOfBo;

        public BuildManager(Manager manager) {
            this.buildLocationManager = new BuildLocationManager(manager);
            this.sitesOfBo = new ArrayList<>();
        }

        public Optional<String> build() {
            Optional<String> upgradeNext = this.upgradeNext();
            if (upgradeNext.isPresent()) {
                return upgradeNext;
            }

            Optional<String> rebuild = this.rebuild();
            if (rebuild.isPresent()) {
                return rebuild;
            }

            Optional<String> buildNext = this.buildNext();
            if (buildNext.isPresent()) {
                return buildNext;
            }


            return Optional.empty();
        }

        private Optional<String> rebuild() {
            for (int i = 0; i < sitesOfBo.size(); i++) {
                Site site = sitesOfBo.get(i);
                BuildRequest buildRequest = BuildOrder.BUILD_ORDER.get(i);
                if (!site.isFriendly() && !site.isEnemyTower()) {
                    if (isTryingToMineAnEmptySpot(site, buildRequest)) {
                        buildRequest.a(StructureType.TOWER);
                        System.err.println("Replace by a tower:" + buildRequest + " " + site);
                        return Optional.of(build(buildRequest, site));
                    }

                    System.err.println("Rebuild:" + buildRequest + " " + site);
                    return Optional.of(build(buildRequest, site));
                }
            }
            return Optional.empty();
        }

        private boolean isTryingToMineAnEmptySpot(Site site, BuildRequest buildRequest) {
            return buildRequest.getStructureType() == StructureType.MINE && site.hasNoRemainingGold();
        }

        private Optional<String> buildNext() {
            if (sitesOfBo.size() < BuildOrder.BUILD_ORDER.size()) {
                BuildRequest buildRequest = BuildOrder.BUILD_ORDER.get(sitesOfBo.size());
                System.err.println("Build next:" + buildRequest);
                return Optional.of(build(buildRequest));
            }
            return Optional.empty();
        }

        private Optional<String> upgradeNext() {
            for (int i = 0; i < sitesOfBo.size(); i++) {
                Site site = sitesOfBo.get(i);
                BuildRequest buildRequest = BuildOrder.BUILD_ORDER.get(i);
                if (site.isMineUpgradable()) {
                    System.err.println("Upgrade:" + buildRequest + " " + site);
                    return Optional.of(build(buildRequest, site));
                }

                if (site.isTowerUpgradable()) {
                    System.err.println("Upgrade:" + buildRequest);
                    return Optional.of(build(buildRequest, site));
                }
            }
            return Optional.empty();
        }

        private String build(BuildRequest request) {
            request.check();
            Site site = this.buildLocationManager.get(request.getOption());
            this.sitesOfBo.add(site);
            return build(request, site);
        }

        private String build(BuildRequest request, Site site) {
            if (request.getStructureType() == StructureType.BARRACKS) {
                return String.format("BUILD %d %s-%s", site.getId(), request.getStructureType(), request.getUnitType());
            }
            return String.format("BUILD %d %s", site.getId(), request.getStructureType());
        }

    }

    static class PositionableComparator implements Comparator<Positionable> {
        private final PositionComparator positionComparator;

        public PositionableComparator(Positionable origin) {
            this.positionComparator = new PositionComparator(origin.getPosition());
        }

        public PositionableComparator(Position position) {
            this.positionComparator = new PositionComparator(position);
        }

        public int compare(Positionable p1, Positionable p2) {
            return positionComparator.compare(p1.getPosition(), p2.getPosition());
        }

    }

    static class SiteManager {
        private final List<Site> sites;
        private List<Site> neutralSites;
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
            this.sites.forEach(site -> site.update(in));
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

        public static SiteManager read(Scanner in) {
            int numSites = in.nextInt();
            List<Site> sites = new ArrayList<>();
            while (numSites-- > 0) {
                sites.add(Site.read(in));
            }
            return new SiteManager(sites);
        }

    }

    static class SiteState {
        private final int gold;
        private final int maxMineSize;
        private final StructureType structureType;
        private final Team team;
        private final int remainTurn;
        private final int param2;

        private SiteState(int gold, int maxMineSize, StructureType structureType, Team team, int remainTurn, int param2) {
            this.gold = gold;
            this.maxMineSize = maxMineSize;
            this.structureType = structureType;
            this.team = team;
            this.remainTurn = remainTurn;
            this.param2 = param2;
        }

        public Team getTeam() {
            return team;
        }

        public boolean isReady() {
            return remainTurn == 0;
        }

        public int getRemainTurn() {
            return remainTurn;
        }

        public int getGold() {
            return gold;
        }

        public int getMaxMineSize() {
            return maxMineSize;
        }

        public StructureType getStructureType() {
            return structureType;
        }

        public static SiteState read(Scanner in) {
            return new SiteState(in.nextInt(), in.nextInt(), StructureType.read(in), Team.read(in), in.nextInt(), in.nextInt());
        }

        public String toString() {
            return "SiteState{" +
                    "gold=" + gold +
                    ", maxMineSize=" + maxMineSize +
                    ", structureType=" + structureType +
                    ", team=" + team +
                    ", remainTurn=" + remainTurn +
                    ", param2=" + param2 +
                    '}';
        }

    }

    static class QueenManager {
        private final BuildManager buildManager;
        private final Escaper escaper;

        public QueenManager(Manager manager) {
            this.buildManager = new BuildManager(manager);
            this.escaper = new Escaper(manager);
        }

        public void build() {
            Optional<String> toBuild = this.buildManager.build();

            if (toBuild.isPresent()) {
                System.out.println(toBuild.get());
                return;
            }
            System.err.println("Try to escape");
            System.out.println(move(this.escaper.getEscapePosition()));
        }

        private String move(Position position) {
            return String.format("MOVE %d %d", position.getX(), position.getY());
        }

    }

    static class Manager {
        private final SiteManager siteManager;
        private UnitManager unitManager;

        public Manager(Scanner in) {
            this.siteManager = SiteManager.read(in);
        }

        public void update(Scanner in) {
            this.siteManager.update(in);
            this.unitManager = UnitManager.read(in);
            if (this.siteManager.getMyStartSite() == null) {
                this.siteManager.setStartSites(
                        new Finder<>(this.siteManager.getSites()).sortByClosestFrom(unitManager.getMyQueen()).get(),
                        new Finder<>(this.siteManager.getSites()).sortByClosestFrom(unitManager.getHisQueen()).get()
                );
            }

        }

        public SiteManager getSiteManager() {
            return siteManager;
        }

        public UnitManager getUnitManager() {
            return unitManager;
        }

    }

    static interface EnumCG {
        int getCode();

        static <T extends EnumCG> T find(int code, T[] enumCGs) {
            for (T enumCG : enumCGs) {
                if (enumCG.getCode() == code) {
                    return enumCG;
                }
            }
            throw new IllegalArgumentException("No " + enumCGs[0].getClass().getName() + " found with code " + code);
        }

    }

    static class BuildOrder {
        static final List<BuildRequest> BUILD_ORDER = Arrays.asList(
                new BuildRequest().a(StructureType.MINE).in(BuildRequest.Option.CLOSEST),
                new BuildRequest().a(StructureType.MINE).in(BuildRequest.Option.CLOSEST),
                new BuildRequest().a(StructureType.MINE).in(BuildRequest.Option.CLOSEST),
                new BuildRequest().a(StructureType.TOWER).in(BuildRequest.Option.CLOSEST),
                new BuildRequest().a(StructureType.BARRACKS).of(UnitType.KNIGHT).in(BuildRequest.Option.CLOSEST),
                new BuildRequest().a(StructureType.MINE).in(BuildRequest.Option.SAFE_BASE),
                new BuildRequest().a(StructureType.MINE).in(BuildRequest.Option.SAFE_BASE),
                new BuildRequest().a(StructureType.MINE).in(BuildRequest.Option.SAFE_BASE)
        );

    }

    static class Engine {
        private final Manager manager;
        private final QueenManager queenManager;
        private final TrainManager trainManager;

        public Engine(Scanner in) {
            this.manager = new Manager(in);
            this.queenManager = new QueenManager(manager);
            this.trainManager = new TrainManager(manager);
        }

        public void update(Scanner in) {
            int gold = in.nextInt();
            int touchedSite = in.nextInt(); // -1 if none
            this.manager.update(in);
            this.queenManager.build();
            this.trainManager.train(gold);
        }

    }

    static enum StructureType implements EnumCG {
        NO_STRUCTURE(-1),
        MINE(0),
        TOWER(1),
        BARRACKS(2),
        ;
        private final int code;

        StructureType(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static StructureType read(Scanner in) {
            return EnumCG.find(in.nextInt(), values());
        }

    }

    static interface Positionable {
        Position getPosition();

        default double getDistance(Positionable positionable) {
            return getPosition().getDistance(positionable.getPosition());
        }

    }
}

