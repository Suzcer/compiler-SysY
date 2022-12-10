package symtable;

import TypeSys.Type;

/**
 * 既是一个类型，也是一个符号 int double void
 */
public class BasicTypeSymbol extends BaseSymbol implements Type {

  public BasicTypeSymbol(String name) {
    super(name, null);
  }

  @Override
  public String toString() {
    return name;
  }
}
