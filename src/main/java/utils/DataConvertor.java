package utils;

import com.firebase.client.DataSnapshot;
import models.Task;
import models.TaskStatus;
import models.TaskType;

/**
 * Created by akashbhatia on 3/16/18.
 */
public class DataConvertor {

    public Task datasnapshotToTask(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.child("id").getValue().toString();
        String name = dataSnapshot.child("name").getValue().toString();
        Long createdAt = (Long) dataSnapshot.child("createdAt").getValue();
        Long updatedAt = (Long) dataSnapshot.child("updatedAt").getValue();
        TaskStatus status = TaskStatus.valueOf(dataSnapshot.child("status").getValue().toString());
        TaskType type = TaskType.valueOf(dataSnapshot.child("type").getValue().toString());;

        Task task = new Task(id, name, createdAt, type);
        task.setUpdatedAt(updatedAt);
        task.setStatus(status);

        return task;
    }
}
