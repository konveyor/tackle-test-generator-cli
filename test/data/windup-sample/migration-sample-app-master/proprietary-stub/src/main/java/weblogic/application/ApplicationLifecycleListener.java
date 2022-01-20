package weblogic.application;

public abstract class ApplicationLifecycleListener {

    public abstract void preStart(ApplicationLifecycleEvent evt);
    public abstract void postStart(ApplicationLifecycleEvent evt);
    public abstract void postStop(ApplicationLifecycleEvent evt);
    public abstract void preStop(ApplicationLifecycleEvent evt);

}
