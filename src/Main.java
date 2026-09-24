import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Retention(RetentionPolicy.RUNTIME)
@interface Repeat {
  int value();
}

class MyClass {

  public void publicMethod1() {
    System.out.println("Public method 1");
  }

  public void publicMethod2(String text) {
    System.out.println("Public method 2: " + text);
  }

  @Repeat(3)
  protected void protectedMethod1() {
    System.out.println("Protected method 1");
  }

  @Repeat(2)
  protected void protectedMethod2(String text) {
    System.out.println("Protected method 2: " + text);
  }

  protected void protectedMethod3() {
    System.out.println("Protected method 3");
  }

  @Repeat(4)
  private void privateMethod1() {
    System.out.println("Private method 1");
  }

  @Repeat(2)
  private void privateMethod2(int number) {
    System.out.println("Private method 2: " + number);
  }

  private void privateMethod3() {
    System.out.println("Private method 3");
  }
}

public class Main {

  private static Object createParameter(Class<?> type) {

    if (type == String.class) {
      return "Hello";
    }

    if (type == int.class || type == Integer.class) {
      return 10;
    }

    if (type == long.class || type == Long.class) {
      return 10L;
    }

    if (type == double.class || type == Double.class) {
      return 10.0;
    }

    if (type == float.class || type == Float.class) {
      return 10.0f;
    }

    if (type == short.class || type == Short.class) {
      return (short) 10;
    }

    if (type == byte.class || type == Byte.class) {
      return (byte) 10;
    }

    if (type == boolean.class || type == Boolean.class) {
      return true;
    }

    if (type == char.class || type == Character.class) {
      return 'A';
    }

    return null;
  }

  public static void main(String[] args) throws Exception {

    MyClass object = new MyClass();

    Method[] methods = MyClass.class.getDeclaredMethods();

    for (Method method : methods) {

      if (!method.isAnnotationPresent(Repeat.class)) {
        continue;
      }

      // Проверяем, что метод protected или private
      int modifiers = method.getModifiers();

      if (!Modifier.isProtected(modifiers)
              && !Modifier.isPrivate(modifiers)) {
        continue;
      }

      Repeat repeat = method.getAnnotation(Repeat.class);
      int count = repeat.value();

      Class<?>[] types = method.getParameterTypes();

      Object[] parameters = new Object[types.length];

      for (int i = 0; i < types.length; i++) {
        parameters[i] = createParameter(types[i]);
      }

      method.setAccessible(true);

      for (int i = 0; i < count; i++) {
        method.invoke(object, parameters);
      }
    }
  }
}

