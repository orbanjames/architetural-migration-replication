package benchmarks;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class LatencyStatistics {

    static class Record {
        int users;
        double blocking;
        double async;
        double reactive;

        Record(int users, double b, double a, double r) {
            this.users = users;
            this.blocking = b;
            this.async = a;
            this.reactive = r;
        }
    }

    public static void main(String[] args) throws Exception {

        List<String> lines = Files.readAllLines(
                Paths.get("benchmarks/raw-results/latency_scaling_10_runs.csv"));

        lines.remove(0); // header

        Map<Integer, List<Record>> grouped = new HashMap<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] t = line.split(",");

            Record r = new Record(
                    Integer.parseInt(t[1]),
                    Double.parseDouble(t[2]),
                    Double.parseDouble(t[3]),
                    Double.parseDouble(t[4])
            );

            grouped.computeIfAbsent(r.users, k -> new ArrayList<>()).add(r);
        }

        for (Integer users : grouped.keySet()) {

            List<Record> records = grouped.get(users);

            double avgBlocking = records.stream().mapToDouble(r -> r.blocking).average().orElse(0);
            double avgAsync = records.stream().mapToDouble(r -> r.async).average().orElse(0);
            double avgReactive = records.stream().mapToDouble(r -> r.reactive).average().orElse(0);

            double sdBlocking = stdDev(records.stream().map(r -> r.blocking).collect(Collectors.toList()));
            double sdAsync = stdDev(records.stream().map(r -> r.async).collect(Collectors.toList()));
            double sdReactive = stdDev(records.stream().map(r -> r.reactive).collect(Collectors.toList()));

            System.out.println("\nUsers: " + users);
            System.out.println("Blocking -> Mean: " + avgBlocking + " | SD: " + sdBlocking);
            System.out.println("Async    -> Mean: " + avgAsync + " | SD: " + sdAsync);
            System.out.println("Reactive -> Mean: " + avgReactive + " | SD: " + sdReactive);
        }
    }

    private static double stdDev(List<Double> values) {
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .sum() / (values.size() - 1);
        return Math.sqrt(variance);
    }
}
