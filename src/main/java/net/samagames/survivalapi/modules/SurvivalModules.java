package net.samagames.survivalapi.modules;

public enum SurvivalModules
{
    ULTRAHARDCORE(UltraHardCoreModule.class);

    private final Class<? extends ISurvivalModule> moduleClass;

    SurvivalModules(Class<? extends ISurvivalModule> moduleClass)
    {
        this.moduleClass = moduleClass;
    }

    public Class<? extends ISurvivalModule> getModuleClass()
    {
        return this.moduleClass;
    }
}
