package fr.lacaleche.pipe.common.tasks.interfaces;

import fr.lacaleche.pipe.common.tasks.impl.TaskBuilder;

public interface BuilderPredicate {

    TaskBuilder build(TaskBuilder taskBuilder);

}
