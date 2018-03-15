package handlers.TaskHandlers;

import com.firebase.client.*;
import com.sun.tools.classfile.ConstantPool;
import constants.Constants;
import models.ProcessType;
import models.TaskStatus;
import models.TaskType;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akashbhatia on 3/16/18.
 */
public class TaskProcessor {

    private Firebase firebase;
    private int smallTaskCount;
    private int mediumTaskCount;
    private int largeTaskCount;

    public TaskProcessor() {
        this.firebase = new Firebase(Constants.FIREBASE_URL);
        this.smallTaskCount = 0;
        this.mediumTaskCount = 0;
        this.largeTaskCount = 0;
    }

    public void processTask(ProcessType processType, DataSnapshot snapshot) {
        switch (processType) {
            case ADDED:
                processAddedEvent(snapshot);
                break;

            case CHANGED:
                processChangedEvent(snapshot);
                break;

            case REMOVED:
                processRemovedEvent(snapshot);
                break;

            case MOVED:
                processMovedEvent(snapshot);
                break;
        }
    }

    private void processAddedEvent(DataSnapshot dataSnapshot) {
        System.out.println("task added :: " + dataSnapshot);

        Firebase ref = dataSnapshot.getRef();
        ref.runTransaction(new Transaction.Handler() {

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                try {
                    mutableData.child("status").setValue(TaskStatus.ACTIVE);
                    TaskType type = TaskType.valueOf(mutableData.child("type").getValue().toString());

                    Boolean check = incrementCount(type);
                    if (!check) {
                        System.out.println("Unable to process " + type + " process with id :: " + mutableData.child("id").getValue() + ". Process count exceeded!");
                        return Transaction.success(mutableData);
                    }

                    System.out.println("wait time :: " + type.getMaxProcessCount(type) * 100 + " milli seconds.");
                    Thread.sleep(type.getMaxProcessCount(type) * 100);

                    mutableData.child("updatedAt").setValue(new DateTime().getMillis());
                    mutableData.child("status").setValue(TaskStatus.COMPLETED);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    mutableData.child("updatedAt").setValue(new DateTime().getMillis());
                    mutableData.child("status").setValue(TaskStatus.FAILED);
                    return Transaction.abort();
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                System.out.println("Transaction processed for task :: " + dataSnapshot.getKey());
                TaskType type = TaskType.valueOf(dataSnapshot.child("type").getValue().toString());
                decrementCount(type);
                moveTaskToCompletedQueue(dataSnapshot);
            }
        });
    }

    private void processChangedEvent(DataSnapshot dataSnapshot) {
        System.out.println("task changed :: " + dataSnapshot);
    }

    private void processRemovedEvent(DataSnapshot dataSnapshot) {
        System.out.println("task removed :: " + dataSnapshot);
    }

    private void processMovedEvent(DataSnapshot dataSnapshot) {
        System.out.println("task moved :: " + dataSnapshot);
    }

    private void moveTaskToCompletedQueue(DataSnapshot dataSnapshot) {
        Firebase ref = this.firebase.child("completedTasks");
        String id = dataSnapshot.child("id").getValue().toString();

        Map map = new HashMap();
        map.put(id,dataSnapshot.getValue());
        ref.updateChildren(map);
        dataSnapshot.getRef().removeValue();
    }

    private boolean incrementCount(TaskType type) {
        switch (type) {
            case SHORT:
                if (smallTaskCount < Constants.SHORT_MAX_PROCESS_COUNT) {
                    smallTaskCount++;
                }
                else {
                    return false;
                }
                break;

            case MEDIUM:
                if (mediumTaskCount < Constants.MEDIUM_MAX_PROCESS_COUNT) {
                    mediumTaskCount++;
                }
                else {
                    return false;
                }
                break;

            case LARGE:
                if (largeTaskCount < Constants.LARGE_MAX_PROCESS_COUNT) {
                    largeTaskCount++;
                }
                else {
                    return false;
                }
                break;
        }

        return true;
    }

    private boolean decrementCount(TaskType type) {
        switch (type) {
            case SHORT:
                if (smallTaskCount > 0) {
                    smallTaskCount--;
                }
                else {
                    return false;
                }
                break;

            case MEDIUM:
                if (mediumTaskCount > 0) {
                    mediumTaskCount--;
                }
                else {
                    return false;
                }
                break;

            case LARGE:
                if (largeTaskCount > 0) {
                    largeTaskCount--;
                }
                else {
                    return false;
                }
                break;
        }

        return true;
    }
}
