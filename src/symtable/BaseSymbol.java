package symtable;


import TypeSys.Type;

public class BaseSymbol implements Symbol {
    final String name;
    final Type type;

    public BaseSymbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}