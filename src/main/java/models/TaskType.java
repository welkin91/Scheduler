package models;

/**
 * Created by akashbhatia on 3/16/18.
 */
public enum TaskType {
    SHORT,
    MEDIUM,
    LARGE;

    public int getMaxProcessCount(TaskType type) {
        int res = 0;

        switch (type) {
            case SHORT:
                res = 5;
                break;

            case MEDIUM:
                res = 10;
                break;

            case LARGE:
                res = 15;
                break;
        }

        return res;
    }
}
