package helpers;

import handlers.databaseHandlers.FirebaseHandler;
import models.Task;
import models.TaskType;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by akashbhatia on 3/16/18.
 */
public class DummyDataCreator {

    public Map createDummyData() {
        Map res = new HashMap();

        int n = 100;
        System.out.println("creating dummy data of size :: " + n);
        Map list = new HashMap();

        for (int i = 0 ;i < n; i++) {
            String id = UUID.randomUUID().toString();
            String name = UUID.randomUUID().toString();
            Long timestamp = new DateTime().getMillis() - new Random(86400).nextLong();
            TaskType type;

            int x = i % 3;
            switch (x) {
                case 0:
                    type = TaskType.SHORT;
                    break;

                case 1:
                    type = TaskType.MEDIUM;
                    break;

                case 2:
                    type = TaskType.LARGE;
                    break;

                default:
                    type = TaskType.MEDIUM;
            }

            Task task = new Task(id, name, timestamp, type);
            list.put(task.getId(), task);
        }

        res.put("pendingTasks", list);

        return res;
    }
}
