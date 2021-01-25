package com.oc.hawk.project.port.driven.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public class GitProgressMonitor implements ProgressMonitor {
    private final LinkedList<TaskState> progress = Lists.newLinkedList();
    private boolean running = true;

    public List<TaskState> getTaskState() {
        return this.progress;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void complete() {
        this.running = false;
    }

    @Override
    public void start(int totalTasks) {
        log.info("job start " + totalTasks);
    }

    @Override
    public void beginTask(String title, int totalWork) {
        progress.add(new TaskState(title, totalWork));
        log.info("beginTask {} - {}", title, totalWork);
    }

    @Override
    public void update(int completed) {
        TaskState last = progress.getLast();
        last.add(completed);
    }

    @Override
    public void endTask() {
        progress.getLast().complete();
        log.info("endTask ");
    }

    @Override
    @JsonIgnore
    public boolean isCancelled() {
        return false;
    }

    @Getter
    public static class TaskState {
        private final String title;
        private final int total;
        private int complete;

        TaskState(String title, int total) {
            this.total = total;
            this.title = title;
        }

        @Override
        public String toString() {
            return complete + "/" + total;
        }

        void complete() {
            this.complete = this.total;
        }

        void add(int count) {
            this.complete += count;
        }
    }
}
