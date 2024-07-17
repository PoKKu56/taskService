package ru.cinimex.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cinimex.taskservice.domain.TaskEntity;
import ru.cinimex.taskservice.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaMessageScheduler {

    @Value("${scheduler.batch-size}")
    private int batchSize;

    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(cron = "${scheduler.cron}")
    @Transactional
    public void sendMessages() {
        List<TaskEntity> tasks = taskRepository.findAndLockTasks(Limit.of(batchSize));
        for (TaskEntity task : tasks) {
            try {
                kafkaTemplate.send("tasks-topic" ,task.getTitle());
                task.setStatus("DONE");
            } catch (Exception e) {
                task.setStatus("ERROR");
            }
        }
        taskRepository.saveAll(tasks);
    }
}
