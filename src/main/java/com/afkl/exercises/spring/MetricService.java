package com.afkl.exercises.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.MetricReaderPublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MetricService {

    @Autowired
    private MetricReaderPublicMetrics publicMetrics;

    private List<ArrayList<Integer>> statusMetricsByMinute;
    private List<String> statusList;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Scheduled(fixedDelay = 60000)
    private void exportMetrics() {
        ArrayList<Integer> lastMinuteStatuses = initializeStatuses(statusList.size());
        for (Metric<?> counterMetric : publicMetrics.metrics()) {
            updateMetrics(counterMetric, lastMinuteStatuses);
        }
        statusMetricsByMinute.add(lastMinuteStatuses);
    }
    private ArrayList<Integer> initializeStatuses(int size) {
        ArrayList<Integer> counterList = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            counterList.add(0);
        }
        return counterList;
    }


    private void updateMetrics(Metric<?> counterMetric, ArrayList<Integer> statusCount) {
        String status = "";
        int index = -1;
        int oldCount = 0;

        if (counterMetric.getName().contains("counter.status.")) {
            status = counterMetric.getName().substring(15, 18); // example 404, 200
            appendStatusIfNotExist(status, statusCount);
            index = statusList.indexOf(status);
            oldCount = statusCount.get(index) == null ? 0 : statusCount.get(index);
            statusCount.set(index, counterMetric.getValue().intValue() + oldCount);
        }
    }

    private void appendStatusIfNotExist(String status, ArrayList<Integer> statusCount) {
        if (!statusList.contains(status)) {
            statusList.add(status);
            statusCount.add(0);
        }
    }

    public Object[][] getGraphData() {
        Date current = new Date();
        int colCount = statusList.size() + 1;
        int rowCount = statusMetricsByMinute.size() + 1;
        Object[][] result = new Object[rowCount][colCount];
        result[0][0] = "Time";

        int j = 1;
        for (String status : statusList) {
            result[0][j] = status;
            j++;
        }

        ArrayList<Integer> temp;
        for (int i = 1; i < rowCount; i++) {
            temp = statusMetricsByMinute.get(i - 1);
            result[i][0] = dateFormat.format
                    (new Date(current.getTime() - (60000 * (rowCount - i))));
            for (j = 1; j <= temp.size(); j++) {
                result[i][j] = temp.get(j - 1);
            }
            while (j < colCount) {
                result[i][j] = 0;
                j++;
            }
        }

        return result;
    }


}