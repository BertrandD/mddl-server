package com.middlewar.api.manager;

import com.middlewar.api.executor.BuildingTaskExecutor;
import com.middlewar.api.executor.TaskExecutor;
import com.middlewar.api.services.InventoryService;
import com.middlewar.core.model.tasks.BuildingTask;
import com.middlewar.core.model.tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskExecutorManager {

    @Autowired
    private InventoryService inventoryService;

    public TaskExecutor getExecutor(Task task) {
        if (task instanceof BuildingTask) {
            return new BuildingTaskExecutor((BuildingTask)task, inventoryService);
        }
        return null;
    }

}
