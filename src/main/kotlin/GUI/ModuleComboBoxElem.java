package GUI;

import Timetable.Module;

public class ModuleComboBoxElem {
    public Module module;
    public ModuleComboBoxElem(Module module){
        this.module = module;
    }

    @Override
    public String toString() {
        return this.module.getName();
    }
}
