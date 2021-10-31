package ro.ase.csie.gabriel.pintea.ebuss.models.employee;

import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private long emplId;
    private String name;
    private emplType emplType;
    private String pinCode = "0000";

    public static long noEmployees = 0L;

    public Employee(String name, emplType emplType) {
        this.emplId = ++noEmployees;
        this.name = name;
        this.emplType = emplType;
    }

    public long getEmplId() {
        return emplId;
    }

    public String getName() {
        return name;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public boolean login(String name, String pinCode) {
        return name.equals(this.name) && pinCode.equals(this.pinCode);
    }

    @Override
    public String toString() {
        return emplType.toString() + ' ' + name;
    }
}
