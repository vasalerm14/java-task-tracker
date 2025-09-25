import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void start(){
        super.taskManager = new InMemoryTaskManager();
    }

}


