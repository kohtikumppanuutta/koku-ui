package fi.arcusys.koku.palvelut.model.client;

import org.intalio.tempo.workflow.task.Task;

public class TaskHolder<T extends Task> {
    private T _task;

    private String _formManagerURL;

    public TaskHolder(T task, String formManagerURL) {
        super();
        _task = task;
        _formManagerURL = formManagerURL;
    }

    public String getFormManagerURL() {
        return _formManagerURL;
    }

    public void setFormManagerURL(String formManagerURL) {
        _formManagerURL = formManagerURL;
    }

    public T getTask() {
        return _task;
    }

    public void setTask(T task) {
        this._task = task;
    }

}
