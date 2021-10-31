package ro.ase.csie.gabriel.pintea.ebuss.models.employee;

import java.util.ArrayList;
import java.util.List;

public class Waiter extends Employee {
    private List<Integer> allocTables;

    public Waiter(String name, emplType emplType, boolean isBusy, List<Integer> allTables) {
        super(name, emplType);
        if(allTables == null) {
            this.allocTables = new ArrayList<Integer>();
        } else {
            this.allocTables = allTables;
        }
    }

    public void setAllocTable(int tableNo) {
        this.allocTables.add(tableNo);
    }


}
