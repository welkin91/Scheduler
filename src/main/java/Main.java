import handlers.databaseHandlers.FirebaseHandler;
import helpers.DummyDataCreator;

import java.util.Map;

/**
 * Created by akashbhatia on 3/16/18.
 */
public class Main {
    FirebaseHandler firebaseHandler;
    DummyDataCreator dataCreator;

    public Main() {
        firebaseHandler = new FirebaseHandler();
        dataCreator = new DummyDataCreator();
    }

    public void store() {
        Map map = dataCreator.createDummyData();
        firebaseHandler.storeData("", map);
    }

    public static void main(String[] argv) {
        Main dataCreator = new Main();
        dataCreator.store();

        dataCreator.firebaseHandler.processTasks();
    }
}
