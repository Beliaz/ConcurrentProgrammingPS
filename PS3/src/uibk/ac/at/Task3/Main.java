package uibk.ac.at.Task3;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {

    static class Measurement
    {
        private String mapType;
        private int threadCount;
        private int accessCount;
        private int inserts;
        private int contains;
        private int removes;
        private float accessesPerSecond;

        public Measurement(String mapType, int threadCount, int accessCount, int inserts, int contains, int removes, float accessesPerSecond)
        {
            this.mapType = mapType;
            this.threadCount = threadCount;
            this.accessCount = accessCount;
            this.inserts = inserts;
            this.contains = contains;
            this.removes = removes;
            this.accessesPerSecond = accessesPerSecond;
        }

        public String getMapType() {
            return mapType;
        }

        public int getThreadCount() {
            return threadCount;
        }

        public int getAccessCount() {
            return accessCount;
        }

        public int getInserts() {
            return inserts;
        }

        public int getContains() {
            return contains;
        }

        public int getRemoves() {
            return removes;
        }

        public float getAccessesPerSecond() {
            return accessesPerSecond;
        }
    }

    private static void test(AbstractMap<Integer, Integer> map, int count, int inserts, int contains, int removes)
    {
        assert(inserts + contains + removes == 100);

        Random random = new Random();
        for(int i = 0; i < count; i++)
        {
            int rnd = random.nextInt(100);
            if(rnd <= inserts)
            {
                map.put(rnd, rnd);
            }
            else if(rnd <= inserts + contains)
            {
                map.containsKey(rnd);
            }
            else
            {
                map.remove(rnd);
            }
        }
    }

    private static Measurement spawnThreads(AbstractMap map, int threadCount, int accessCount, int inserts, int contains, int removes)
    {
        long start = System.nanoTime();

        List<Thread> threads = new LinkedList<>();
        for(int i = 0; i < threadCount; i++)
        {
            threads.add(new Thread(() -> test(map, accessCount, inserts, contains, removes)));
        }

        threads.forEach(Thread::start);

        for(Thread th : threads)
        {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        float elapsed = (System.nanoTime() - start) / 1e9f;

        return new Measurement(map.getClass().toString(),
                               threadCount,
                               accessCount,
                               inserts,
                               contains,
                               removes,
                               accessCount / elapsed);
    }

    private static void doTests(int inserts, int contains, int removes)
    {
        final List<Integer> threadCounts = new LinkedList<>();
        threadCounts.add(1);
        threadCounts.add(2);
        threadCounts.add(4);
        threadCounts.add(8);
        threadCounts.add(16);

        final List<Integer> accessCounts = new LinkedList<>();
        accessCounts.add(10);
        accessCounts.add(100);
        accessCounts.add(1000);
        accessCounts.add(10000);
        accessCounts.add(100000);

        final List<AbstractMap<Integer, Integer>> maps = new LinkedList<>();
        maps.add(new HashMap<>());
        maps.add(new SynchronizedMap<>(new HashMap<>()));
        maps.add(new ConcurrentHashMap<>());

        final List<Measurement> measurements = new LinkedList<>();

        // warm up
        spawnThreads(maps.get(0),
                threadCounts.get(threadCounts.size() - 1),
                accessCounts.get(accessCounts.size() - 1),
                50, 25, 25);

        for(AbstractMap map : maps)
        {
            for(int threadCount : threadCounts)
            {
                measurements.addAll(accessCounts.stream()
                        .map(accessCount -> spawnThreads(map, threadCount, accessCount, inserts, contains, removes))
                        .collect(Collectors.toList()));
            }
        }

        final int maxAccessCountStringLength = accessCounts.get(accessCounts.size() - 1).toString().length();


        System.out.format("insertions: %d\n", inserts);
        System.out.format("reads: %d\n", contains);
        System.out.format("deletions: %d\n\n", removes);

        for(int i = 0; i < maps.size(); i++)
        {
            System.out.format("%c := %s\n", (char)((int)('A') + i), maps.get(i).getClass().toString());
        }

        System.out.println();

        for(int i = 0; i < maxAccessCountStringLength; i++)
            System.out.print(" ");

        for(int i = 0; i < maps.size(); i++)
        {
            System.out.format("|    %c     ", (char)((int)('A') + i));
        }

        System.out.println();

        for(int threadCount : threadCounts)
        {
            System.out.format("%d: \n", threadCount);

            for(Integer accessCount : accessCounts)
            {
                System.out.print(accessCount);

                for(int i = accessCount.toString().length();
                    i < maxAccessCountStringLength; i++)
                {
                    System.out.print(" ");
                }

                for(AbstractMap<Integer, Integer> map : maps)
                {
                    String mapType = map.getClass().toString();

                    measurements.stream().filter(
                            m ->
                            {
                                return Objects.equals(m.getMapType(), mapType) &&
                                        m.getThreadCount() == threadCount &&
                                        m.getAccessCount() == accessCount;
                            }).forEach(
                            m -> {
                                System.out.format("| %.2e ", m.getAccessesPerSecond());
                            });
                }

                System.out.println();
            }

            System.out.println();
        }

        System.out.println();
    }

    public static void main(String[] args) {

        doTests(50, 25, 25);
        doTests(80, 15, 5);
        doTests(5, 90, 5);
    }
}
