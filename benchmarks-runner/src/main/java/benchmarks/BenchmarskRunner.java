package benchmarks;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

public class BenchmarskRunner {

    private static final String BLOCKING_URL = "http://localhost:8081/api/test";
    private static final String ASYNC_URL = "http://localhost:8082/api/test";
    private static final String REACTIVE_URL = "http://localhost:8083/api/test";

    private static final int[] CONCURRENCY_LEVELS = {50, 100, 200, 500, 1000};
    private static final int RUNS = 10;

    public static void main(String[] args) throws Exception {

        Files.createDirectories(Paths.get("../benchmarks/raw-results/"));

        FileWriter writer = new FileWriter("../benchmarks/raw-results/latency_scaling_10_runs.csv");
        writer.write("run,users,blocking_ms,async_ms,reactive_ms\n");

        for (int users : CONCURRENCY_LEVELS) {

            for (int run = 1; run <= RUNS; run++) {

                double blocking = measureLatency(BLOCKING_URL, users);
                double async = measureLatency(ASYNC_URL, users);
                double reactive = measureLatency(REACTIVE_URL, users);

                writer.write(run + "," + users + "," +
                        blocking + "," + async + "," + reactive + "\n");

                System.out.println("Users=" + users + " Run=" + run + " completed.");
            }
        }

        writer.close();

        System.out.println("Raw benchmark completed.");
        System.out.println("Generating statistical appendices...");

        StatisticalAppendixGenerator.generate();

        System.out.println("All statistical reports generated.");
    }

    private static double measureLatency(String url, int concurrentUsers) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(concurrentUsers);
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        List<Callable<Long>> tasks = new ArrayList<>();

        for (int i = 0; i < concurrentUsers; i++) {
            tasks.add(() -> {
                long start = System.nanoTime();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                client.send(request, HttpResponse.BodyHandlers.ofString());
                long end = System.nanoTime();
                return (end - start) / 1_000_000;
            });
        }

        List<Future<Long>> results = executor.invokeAll(tasks);
        executor.shutdown();

        long total = 0;
        for (Future<Long> f : results)
            total += f.get();

        return (double) total / concurrentUsers;
    }
}
