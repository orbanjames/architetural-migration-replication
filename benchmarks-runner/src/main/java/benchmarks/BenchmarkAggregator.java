package benchmarks;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class BenchmarkAggregator {

    static class Metrics {
        double latency;
        double throughput;
        double cpu;
        double memory;

        Metrics(double l, double t, double c, double m) {
            latency = l;
            throughput = t;
            cpu = c;
            memory = m;
        }
    }

    public static void main(String[] args) throws Exception {

        String filePath = "benchmarks/raw-results/performance_500_users_10_runs.csv";

        Map<String, List<Metrics>> data = new HashMap<>();

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        lines.remove(0); // remove header

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] tokens = line.split(",");

            String architecture = tokens[1];

            Metrics m = new Metrics(
                    Double.parseDouble(tokens[2]),
                    Double.parseDouble(tokens[3]),
                    Double.parseDouble(tokens[4]),
                    Double.parseDouble(tokens[5])
            );

            data.computeIfAbsent(architecture, k -> new ArrayList<>()).add(m);
        }

        System.out.println("===== AVERAGED RESULTS =====");

        for (String arch : data.keySet()) {

            List<Metrics> list = data.get(arch);

            double avgLatency = list.stream().mapToDouble(m -> m.latency).average().orElse(0);
            double avgThroughput = list.stream().mapToDouble(m -> m.throughput).average().orElse(0);
            double avgCPU = list.stream().mapToDouble(m -> m.cpu).average().orElse(0);
            double avgMemory = list.stream().mapToDouble(m -> m.memory).average().orElse(0);

            System.out.println("\nArchitecture: " + arch);
            System.out.println("Avg Latency: " + avgLatency);
            System.out.println("Avg Throughput: " + avgThroughput);
            System.out.println("Avg CPU: " + avgCPU);
            System.out.println("Avg Memory: " + avgMemory);
        }
    }
}