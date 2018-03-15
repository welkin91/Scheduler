package handlers.databaseHandlers;

import com.firebase.client.*;
import constants.Constants;
import handlers.TaskHandlers.TaskProcessor;
import models.ProcessType;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by akashbhatia on 3/16/18.
 */
public class FirebaseHandler {
    Firebase firebase;
    TaskProcessor processor;

    public FirebaseHandler() {
        this.firebase = new Firebase(Constants.FIREBASE_URL);
        processor = new TaskProcessor();
    }

    public Boolean storeData(String path, Map dataMap) {
        Boolean res = false;

        try {
            Firebase firebaseHandler = firebase;
            firebaseHandler = firebaseHandler.child(path);
            firebaseHandler.setValue(dataMap);
        }
        catch (FirebaseException e) {
            e.printStackTrace();
        }

        return res;
    }

    public void processTasks() {
        try {
            final CountDownLatch latch = new CountDownLatch(1);

            Firebase ref = firebase;

            Query taskQuery = ref.child("pendingTasks").orderByChild("updatedAt").limitToFirst(Constants.TOTAL_MAX_PROCESS_COUNT);
            taskQuery.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    processor.processTask(ProcessType.ADDED, dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    processor.processTask(ProcessType.CHANGED, dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    processor.processTask(ProcessType.REMOVED, dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    processor.processTask(ProcessType.MOVED, dataSnapshot);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            latch.await();

        }
        catch (FirebaseException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
