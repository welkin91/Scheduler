Problem statement:
* Implement a scheduler which can process at max of T process.
* Also process can be of n (right now n = 3) types and each process type run at max Ti processes that any particular time.

#

Solution purposed:
* System requirements:
    * Java 8.
    * Knowledge of firebase.
    
* pendingTasks consists of the tasks to be processed and a limit child event listener is used to get T tasks from the node at a time.
* Additional checks are made if any task type exceeded its limit(Ti).
* if task is processed completely, it is moved to "completedTasks" node, else status is marked as FAILED.

#

* shall you have any doubts/queries or you find any mistake please mail me at akash.bhushan.bhatia@gmail.com . 